package org.team2168.thirdcoast.swerve;

import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.PigeonIMU_StatusFrame;
import com.ctre.phoenix.sensors.PigeonIMU.PigeonState;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team2168.RobotMap;
import org.team2168.thirdcoast.talon.Errors;
import org.team2168.utils.consoleprinter.ConsolePrinter;

/**
 * Control a Third Coast swerve drive.
 *
 * <p>Wheels are a array numbered 0-3 from front to back, with even numbers on the left side when
 * facing forward.
 *
 * <p>Derivation of inverse kinematic equations are from Ether's <a
 * href="https://www.chiefdelphi.com/media/papers/2426">Swerve Kinematics and Programming</a>.
 *
 * @see Wheel
 */
@SuppressWarnings("unused")
public class SwerveDrive {

  public static final int DEFAULT_ABSOLUTE_AZIMUTH_OFFSET = 200;
  private static final Logger logger = LoggerFactory.getLogger(SwerveDrive.class);
  private static final int WHEEL_COUNT = 4;
  private static final double GYRO_INIT_MAX_WAIT_S = 5.0; //seconds
  private static final double GYRO_POLL_DELAY_S = 0.25; //wait (sec) between checking if the gyro is ready on init
  private final PigeonIMU gyro;
  private final double kLengthComponent;
  private final double kWidthComponent;
  private final double kGyroRateCorrection;
  private final Wheel[] wheels;
  private final double[] ws = new double[WHEEL_COUNT];
  private final double[] wa = new double[WHEEL_COUNT];
  private boolean isFieldOriented;

  public SwerveDrive(SwerveDriveConfig config) {
    gyro = config.gyro;
    wheels = config.wheels;

    final boolean summarizeErrors = config.summarizeTalonErrors;
    Errors.setSummarized(summarizeErrors);
    Errors.setCount(0);
    logger.debug("TalonFX configuration errors summarized = {}", summarizeErrors);

    double length = config.length;
    double width = config.width;
    double radius = Math.hypot(length, width);
    kLengthComponent = length / radius;
    kWidthComponent = width / radius;

    boolean gyroIsConnected = false;

    if(gyro != null) {
      double delayed_time = 0.0;

      //The NavX needed some time to initialize on startup.
      //Wait for up to GYRO_INIT_MAX_WAIT_S, checking every GYRO_POLL_DELAY_S if it's ready yet.
      while(gyro.getState() != PigeonState.Ready && delayed_time <= GYRO_INIT_MAX_WAIT_S) {
        delayed_time += GYRO_POLL_DELAY_S;
        Timer.delay(GYRO_POLL_DELAY_S); //Seconds
      }
      gyroIsConnected = gyro.getState() == PigeonState.Ready;
    }

    setFieldOriented(gyroIsConnected);
    SmartDashboard.putBoolean("field oriented?", isFieldOriented);

    logger.debug("gyro is configured {}", gyro != null);
    logger.debug("gyro is connected {}", gyroIsConnected);
    setFieldOriented(gyroIsConnected);
    logger.debug("field oriented? {}", isFieldOriented);

    if (isFieldOriented) {
      // gyro.enableLogging(config.gyroLoggingEnabled);
      double robotPeriod = config.robotPeriod;
      double gyroRateCoeff = config.gyroRateCoeff;
      int rate = gyro.getStatusFramePeriod(PigeonIMU_StatusFrame.CondStatus_9_SixDeg_YPR);
      double gyroPeriod = 1.0 / rate;
      kGyroRateCorrection = (robotPeriod / gyroPeriod) * gyroRateCoeff;
      logger.debug("gyro frequency = {} Hz", rate);
    }
    else {
      logger.warn("gyro is missing or not enabled");
      kGyroRateCorrection = 0;
    }

    logger.debug("length = {}", length);
    logger.debug("width = {}", width);
    logger.debug("enableGyroLogging = {}", config.gyroLoggingEnabled);
    logger.debug("gyroRateCorrection = {}", kGyroRateCorrection);

    double ticksPerSecMax = Wheel.getDriveSetpointMax() * 10.0;
    double maxVelocityFtSec = ticksPerSecMax / Wheel.TICKS_PER_FOOT_DW;

    ConsolePrinter.putNumber("Commanded yaw 0", () -> {return wa[0] * 360;}, false, true);
    ConsolePrinter.putNumber("Commanded speed 0", () -> {return ws[0] * maxVelocityFtSec;}, false, true);
    ConsolePrinter.putNumber("Commanded yaw 1", () -> {return wa[1] * 360;}, false, true);
    ConsolePrinter.putNumber("Commanded speed 1", () -> {return ws[1] * maxVelocityFtSec;}, false, true);
    ConsolePrinter.putNumber("Commanded yaw 2", () -> {return wa[2] * 360;}, false, true);
    ConsolePrinter.putNumber("Commanded speed 2", () -> {return ws[2] * maxVelocityFtSec;}, false, true);
    ConsolePrinter.putNumber("Commanded yaw 3", () -> {return wa[3] * 360;}, false, true);
    ConsolePrinter.putNumber("Commanded speed 3", () -> {return ws[3] * maxVelocityFtSec;}, false, true);
  }

  /**
   * Return key that wheel zero information is stored under in WPI preferences.
   *
   * @param wheel the wheel number
   * @return the String key
   */
  public static String getPreferenceKeyForWheel(int wheel) {
    return String.format("%s/wheel.%d", SwerveDrive.class.getSimpleName(), wheel);
  }

  /**
   * Set the drive mode.
   *
   * @param driveMode the drive mode
   */
  public void setDriveMode(DriveMode driveMode) {
    for (Wheel wheel : wheels) {
      wheel.setDriveMode(driveMode);
    }
    logger.info("drive mode = {}", driveMode);
  }

  /**
   * Set all four wheels to specified values.
   *
   * @param azimuth -0.5 to 0.5 rotations, measured clockwise with zero being the robot
   *     straight-ahead position
   * @param drive 0 to 1 in the direction of the wheel azimuth
   */
  public void set(double azimuth, double drive) {
    for (Wheel wheel : wheels) {
      wheel.set(azimuth, drive);
    }
  }

  /**
   * Drive the robot in given field-relative direction and with given rotation.
   *
   * @param forward Y-axis movement, from -1.0 (reverse) to 1.0 (forward)
   * @param strafe X-axis movement, from -1.0 (left) to 1.0 (right)
   * @param azimuth robot rotation, from -1.0 (CCW) to 1.0 (CW)
   */
  public void drive(double forward, double strafe, double azimuth) {
    double ypr[] = new double[3];

    // Use gyro for field-oriented drive. We use getAngle instead of getYaw to enable arbitrary
    // autonomous starting positions.
    if (isFieldOriented) {
      gyro.getYawPitchRoll(ypr);
      double angle = -ypr[0];
      // angle += gyro.getRate() * kGyroRateCorrection; // Disable this, as we aren't actually using this feature
      angle = Math.IEEEremainder(angle, 360.0);

      angle = Math.toRadians(angle);
      final double temp = forward * Math.cos(angle) + strafe * Math.sin(angle);
      strafe = strafe * Math.cos(angle) - forward * Math.sin(angle);
      forward = temp;
    }

    final double a = strafe - azimuth * kLengthComponent;
    final double b = strafe + azimuth * kLengthComponent;
    final double c = forward - azimuth * kWidthComponent;
    final double d = forward + azimuth * kWidthComponent;

    // wheel speed
    ws[0] = Math.hypot(b, d);
    ws[1] = Math.hypot(b, c);
    ws[2] = Math.hypot(a, d);
    ws[3] = Math.hypot(a, c);

    // wheel azimuth
    wa[0] = Math.atan2(b, d) * 0.5 / Math.PI;
    wa[1] = Math.atan2(b, c) * 0.5 / Math.PI;
    wa[2] = Math.atan2(a, d) * 0.5 / Math.PI;
    wa[3] = Math.atan2(a, c) * 0.5 / Math.PI;

    // normalize wheel speed
    final double maxWheelSpeed = Math.max(Math.max(ws[0], ws[1]), Math.max(ws[2], ws[3]));
    if (maxWheelSpeed > 1.0) {
      for (int i = 0; i < WHEEL_COUNT; i++) {
        ws[i] /= maxWheelSpeed;
      }
    }

    // set wheels
    for (int i = 0; i < WHEEL_COUNT; i++) {
      wheels[i].set(wa[i], ws[i]);
      // SmartDashboard.putNumber("Commanded position (percent of a rotation) module " + i, wa[i]);
    }
  }

  /**
   * Stops all wheels' azimuth and drive movement. Calling this in the robots {@code teleopInit} and
   * {@code autonomousInit} will reset wheel azimuth relative encoders to the current position and
   * thereby prevent wheel rotation if the wheels were moved manually while the robot was disabled.
   */
  public void stop() {
    for (Wheel wheel : wheels) {
      wheel.stop();
    }
    logger.info("stopped all wheels");
  }

  /**
   * Save the wheels' azimuth current position as read by absolute encoder. These values are saved
   * persistently on the roboRIO and are normally used to calculate the relative encoder offset
   * during wheel initialization.
   *
   * <p>The wheel alignment data is saved in the WPI preferences data store and may be viewed using
   * a network tables viewer.
   *
   * @see #zeroAzimuthEncoders()
   */
  public void saveAzimuthPositions() {
    for (int encoderID : RobotMap.CANCODER_ID) {
    CANCoder canCoder = new CANCoder(encoderID);
    double zeroPos = canCoder.getAbsolutePosition();
    canCoder.configMagnetOffset(zeroPos);
    System.out.println(String.format("azimuth id %d: saved zero %f", encoderID, zeroPos));
    }
  }


  /**
   * Returns the wheels of the swerve drive.
   *
   * @return the Wheel array.
   */
  public Wheel[] getWheels() {
    return wheels;
  }

  /**
   * Returns the number of wheels on the swerve drive.
   *
   * @return the number of wheels.
   */
  public static int getWheelCount() {
    return WHEEL_COUNT;
  }

  /**
   * Get the gyro instance being used by the drive.
   *
   * @return the gyro instance.
   */
  public PigeonIMU getGyro() {
    return gyro;
  }

  /**
   * Get status of field-oriented driving.
   *
   * @return status of field-oriented driving.
   */
  public boolean isFieldOriented() {
    return isFieldOriented;
  }

  /**
   * Enable or disable field-oriented driving. Enabled by default if connected gyro is passed in via
   * {@code SwerveDriveConfig} during construction.
   *
   * @param enabled true to enable field-oriented driving.
   */
  public void setFieldOriented(boolean enabled) {
    isFieldOriented = enabled;
    logger.info("field orientation driving is {}", isFieldOriented ? "ENABLED" : "DISABLED");
  }

  /**
   * Unit testing
   *
   * @return length
   */
  double getLengthComponent() {
    return kLengthComponent;
  }

  /**
   * Unit testing
   *
   * @return width
   */
  double getWidthComponent() {
    return kWidthComponent;
  }

  /** Swerve Drive drive mode */
  public enum DriveMode {
    OPEN_LOOP,
    CLOSED_LOOP,
    TELEOP,
    TRAJECTORY,
    AZIMUTH
  }
}
