/**
 * This code was written primarily as a part of FRC Team 2767's Third Coast Library
 * It has been adapted to be used by 2168
 */
package org.team2168.thirdcoast.swerve;

import static com.ctre.phoenix.motorcontrol.ControlMode.*;
import static org.team2168.thirdcoast.swerve.SwerveDrive.DriveMode.TELEOP;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.BaseTalon;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import java.util.Objects;
import java.util.function.DoubleConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team2168.thirdcoast.swerve.SwerveDrive.DriveMode;
import org.team2168.thirdcoast.talon.Errors;

/**
 * Controls a swerve drive wheel azimuth and drive motors.
 *
 * <p>The swerve-drive inverse kinematics algorithm will always calculate individual wheel angles as
 * -0.5 to 0.5 rotations, measured clockwise with zero being the straight-ahead position. Wheel
 * speed is calculated as 0 to 1 in the direction of the wheel angle.
 *
 * <p>This class will calculate how to implement this angle and drive direction optimally for the
 * azimuth and drive motors. In some cases it makes sense to reverse wheel direction to avoid
 * rotating the wheel azimuth 180 degrees.
 *
 * <p>Hardware assumed by this class includes a CTRE magnetic encoder on the azimuth motor and no
 * limits on wheel azimuth rotation. Azimuth Talons have an ID in the range 0-3 with corresponding
 * drive Talon IDs in the range 10-13.
 */
public class Wheel {
  private static final Logger logger = LoggerFactory.getLogger(Wheel.class);
  private static final double AZIMUTH_GEAR_RATIO = (60.0/10.0) * (45.0/15.0); // defined as module input/motor output; placeholder
  private static final double DRIVE_GEAR_RATIO = (60.0/15.0) * (20.0/24.0) * (38.0/18.0);
  private static final int INTERNAL_ENCODER_TICKS = 2048;
  private static final int EXTERNAL_ENCODER_TICKS = 4096;
  private static final double TICKS_PER_DEGREE_AZIMUTH = ((1.0/360.0) * AZIMUTH_GEAR_RATIO * INTERNAL_ENCODER_TICKS);
  private static final double TICKS_PER_DEGREE_DW = ((1.0/360.0) * DRIVE_GEAR_RATIO * INTERNAL_ENCODER_TICKS);
  private static final double INTERNAL_ENCODER_TICKS_PER_REV = 360 * TICKS_PER_DEGREE_AZIMUTH;
  private final double driveSetpointMax;
  private final BaseTalon driveTalon;
  private final TalonFX azimuthTalon;
  protected DoubleConsumer driver;
  private boolean isInverted = false;
  private boolean absoluteEncoderInverted = false;
  private int primaryPID = 0;
  private int auxPID = 1; //specifies the auxiliary pid loop for any method that takes in a pididx


  /**
   * This constructs a wheel with supplied azimuth and drive talons.
   *
   * <p>Wheels will scale closed-loop drive output to {@code driveSetpointMax}. For example, if
   * closed-loop drive mode is tuned to have a max usable output of 10,000 ticks per 100ms, set this
   * to 10,000 and the wheel will send a setpoint of 10,000 to the drive talon when wheel is set to
   * max drive output (1.0).
   *
   * @param azimuth the configured azimuth TalonFX
   * @param drive the configured drive TalonFX
   * @param driveSetpointMax scales closed-loop drive output to this value when drive setpoint = 1.0
   */
  public Wheel(TalonFX azimuth, BaseTalon drive, double driveSetpointMax, boolean absoluteEncoderInverted) {
    this.driveSetpointMax = driveSetpointMax;
    azimuthTalon = Objects.requireNonNull(azimuth);
    driveTalon = Objects.requireNonNull(drive);
    this.absoluteEncoderInverted = absoluteEncoderInverted;

    setDriveMode(TELEOP);

    logger.debug("azimuth = {} drive = {}", azimuthTalon.getDeviceID(), driveTalon.getDeviceID());
    logger.debug("driveSetpointMax = {}", driveSetpointMax);
    if (driveSetpointMax == 0.0) logger.warn("driveSetpointMax may not have been configured");
  }

  /**
   * This method calculates the optimal driveTalon settings and applies them.
   *
   * @param azimuth -0.5 to 0.5 rotations, measured clockwise with zero being the wheel's zeroed
   *     position
   * @param drive 0 to 1.0 in the direction of the wheel azimuth
   */
  public void set(double azimuth, double drive) {
    // don't reset wheel azimuth direction to zero when returning to neutral
    if (drive == 0) {
      driver.accept(0d);
      //return;  commented for testing purposes
    }
    azimuth *= -INTERNAL_ENCODER_TICKS_PER_REV; // flip azimuth, hardware configuration dependent

    double azimuthPosition = azimuthTalon.getSelectedSensorPosition(0);
    double azimuthError = Math.IEEEremainder((azimuth - azimuthPosition), INTERNAL_ENCODER_TICKS_PER_REV);

    // minimize azimuth rotation, reversing drive if necessary
    isInverted = Math.abs(azimuthError) > 0.25 * INTERNAL_ENCODER_TICKS_PER_REV;
    if (isInverted) {
      azimuthError -= Math.copySign(0.5 * INTERNAL_ENCODER_TICKS_PER_REV, azimuthError);
      drive = -drive;
    }

    azimuthTalon.set(MotionMagic, azimuthPosition + azimuthError);
    driver.accept(drive);
  }

  /**
   * Set azimuth motor to encoder position.
   *
   * @param position position in encoder ticks.
   */
  public void setAzimuthMotorPosition(int position) {
    azimuthTalon.set(MotionMagic, position);
  }

  /**
   * Set module heading
   * 
   * @param position position in motor ticks
   */
  public void setAzimuthPosition(int position) {
    setAzimuthMotorPosition((int)(position / AZIMUTH_GEAR_RATIO));
  }

  public void disableAzimuth() {
    azimuthTalon.neutralOutput();
  }

  /**
   * Set the operating mode of the wheel's drive motors. In this default wheel implementation {@code
   * OPEN_LOOP} and {@code TELEOP} are equivalent and {@code CLOSED_LOOP}, {@code TRAJECTORY} and
   * {@code AZIMUTH} are equivalent.
   *
   * <p>In closed-loop modes, the drive setpoint is scaled by the drive Talon {@code
   * driveSetpointMax} parameter.
   *
   * <p>This method is intended to be overridden if the open or closed-loop drive wheel drivers need
   * to be customized.
   *
   * @param driveMode the desired drive mode
   */
  public void setDriveMode(DriveMode driveMode) {
    switch (driveMode) {
      case OPEN_LOOP:
      case TELEOP:
        driver = (setpoint) -> driveTalon.set(PercentOutput, setpoint);
        break;
      case CLOSED_LOOP:
      case TRAJECTORY:
      case AZIMUTH:
        driver = (setpoint) -> driveTalon.set(Velocity, setpoint * driveSetpointMax);
        break;
    }
  }

  /**
   * Stop azimuth and drive movement. This resets the azimuth setpoint and relative encoder to the
   * current position in case the wheel has been manually rotated away from its previous setpoint.
   */
  public void stop() {
    azimuthTalon.set(MotionMagic, azimuthTalon.getSelectedSensorPosition(0));
    driver.accept(0d);
  }

  /**
   * Set the azimuthTalon encoder relative to wheel zero alignment position. For example, if current
   * absolute encoder = 0 and zero setpoint = 2767, then current relative setpoint = -2767.
   *
   * <pre>
   *
   * relative:  -2767                               0
   *           ---|---------------------------------|-------
   * absolute:    0                               2767
   *
   * </pre>
   *
   * @param zero zero setpoint, absolute encoder position (in ticks) where wheel is zeroed.
   */
  public void setAzimuthZero(int zero) {
    int azimuthSetpoint = getAzimuthAbsolutePosition() - zero;
    // ErrorCode err = azimuthTalon.setSelectedSensorPosition(externalToInternalTicks(azimuthSetpoint), primaryPID, 10);
    // Errors.check(err, logger);
    System.out.println("zero: " + zero);
    System.out.println("current pos: " + getAzimuthAbsolutePosition());
    azimuthTalon.setSelectedSensorPosition(externalToInternalTicks(azimuthSetpoint), primaryPID, 10);
    azimuthTalon.set(MotionMagic, azimuthSetpoint);
    System.out.println("SETPOINT: " + azimuthSetpoint);
  }

  /**
   * Takes in a number of ticks from the external encoder of a module, and estamates a number of internal
   * ticks based off the number
   * @param externalTicks a number of ticks from the external encoder
   * @return a proportional number of estamated internal ticks
   */
  public static int externalToInternalTicks(int externalTicks) {
    return (int) Math.round((double) externalTicks*((double) INTERNAL_ENCODER_TICKS/(double) EXTERNAL_ENCODER_TICKS)*AZIMUTH_GEAR_RATIO);
  }

  /**
   * Takes in a number of degrees that we want to rotate the azimuth motor by and converts it to the number of ticks
   * the internal encoder should move by
   * 
   * @param degrees number of degrees the wheel needs to rotate
   * @return the number of ticks the internal encoder should rotate in order to rotate the azimuth motor
   */
  public static int degreesToTicksAzimuth(double degrees) {
    return (int) (degrees * TICKS_PER_DEGREE_AZIMUTH);
  }

  /**
   * Takes in a number of ticks the internal encoder has moved and calculates the number of degrees
   * the azimuth wheel rotated
   * 
   * @param ticks number of ticks the internal encoder has rotated
   * @return number of degrees the wheel moved
   */
  public static double ticksToDegreesAzimuth(double ticks) {
    return (ticks / TICKS_PER_DEGREE_AZIMUTH);
  }

  /**
   * Takes in the number of degrees the wheel has/needs to rotate and calculates the 
   * the number of internal encoder ticks the movement equals
   * 
   * @param degrees number of degrees the drive wheel has/needs to rotate
   * @return number of ticks for the drive wheel's internal encoder
   */
  public static int degreesToTicksDW(double degrees) {
    return (int) (degrees * TICKS_PER_DEGREE_DW);
  }

  /**
   * Takes in the number of ticks the internal encoder has moved and calculates the number of degrees 
   * the drive wheel has/needs to rotate
   * 
   * @param ticks number of ticks the drive wheel has/needs to rotate
   * @return number of degrees for the movement of the drivewheel
   */
  public static double ticksToDegreesDW(double ticks) {
    return (ticks / TICKS_PER_DEGREE_DW);
  }

  /**
   * Takes in the desired degrees per second (DPS) for the drive wheel and calculates the number of ticks
   * per 100 ms (units ctre wants for rate limits)
   * 
   * @param degrees number of degrees per second 
   * @return number of ticks per 100 ms
   */
  public static int DPSToTicksPer100msDW(double degrees) {
    return (int) (degreesToTicksDW(degrees) / 10.0);
  }

  
  /**
   * Takes in the desired degrees per second (DPS) for the module azimuth and calculates the number of ticks
   * per 100 ms (units ctre wants for rate limits)
   * 
   * @param degrees number of degrees per second
   * @return number of ticks per 100 ms
   */
  public static int DPSToTicksPer100msAzimuth(double degrees) {
    return (int) (degreesToTicksAzimuth(degrees) / 10.0);
  }


  /**
   * Sets the azimuth internal encoder's current position to the given absolute encoder position,
   * taking difference in resolution and gear ratio into account
   * @param position position in absolute encoder ticks
   */
  public void setAzimuthInternalEncoderPosition(int position) {
    azimuthTalon.setSelectedSensorPosition((int)((position * ((double)INTERNAL_ENCODER_TICKS/(double)EXTERNAL_ENCODER_TICKS)) * AZIMUTH_GEAR_RATIO), primaryPID, 0);
  }

  /**
   * Returns the wheel's azimuth absolute position in encoder ticks.
   * This method is primarily used for zeroing the
   *
   * @return 0 - 4095, corresponding to one full revolution.
   */
  public int getAzimuthAbsolutePosition() {
    if (this.absoluteEncoderInverted)
      return -azimuthTalon.getSelectedSensorPosition(auxPID);
    else
      return azimuthTalon.getSelectedSensorPosition(auxPID);
  }

  /**
   * Returns the module heading, taking into account the gear ratio.
   * 
   * @return position in motor ticks
   */
  public double getAzimuthPosition() {
    return azimuthTalon.getSelectedSensorPosition(primaryPID) * AZIMUTH_GEAR_RATIO;
  }

  /**
   * Get the azimuth Talon controller.
   *
   * @return azimuth Talon instance used by wheel
   */
  public TalonFX getAzimuthTalon() {
    return azimuthTalon;
  }

  /**
   * Get the drive Talon controller.
   *
   * @return drive Talon instance used by wheel
   */
  public BaseTalon getDriveTalon() {
    return driveTalon;
  }

  public double getDriveSetpointMax() {
    return driveSetpointMax;
  }

  /**
   * Get the position of the azimuth's internal encoder
   * 
   * @return position in encoder ticks
   */
  public int getInternalEncoderPos() {
    return azimuthTalon.getSelectedSensorPosition(primaryPID);
  }

  public boolean isInverted() {
    return isInverted;
  }

  @Override
  public String toString() {
    return "Wheel{"
        + "azimuthTalon="
        + azimuthTalon
        + ", driveTalon="
        + driveTalon
        + ", driveSetpointMax="
        + driveSetpointMax
        + '}';
  }
}
