/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.team2168.Gains;
import org.team2168.RobotMap;
import org.team2168.PID.sensors.CanDigitalInput;
import org.team2168.commands.climber.DriveClimberWithJoystick;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climber extends Subsystem {

  public static Climber instance = null;

  public static double holdingSpeed;
  private TalonSRX climberMotor1;
  private TalonSRX climberMotor2;
  public DoubleSolenoid climberSolenoid;
  private CanDigitalInput hallEffectSensor;

  private final boolean CLIMBER_MOTOR_1_REVERSE = true;
  private final boolean CLIMBER_MOTOR_2_REVERSE = false;
  public static final boolean CLIMBER_ENABLE_HIGHT_HOLD = true;

  private SupplyCurrentLimitConfiguration talonCurrentLimit;
  private final boolean ENABLE_CURRENT_LIMIT = true;
  private final double CONTINUOUS_CURRENT_LIMIT = 20; //amps
  private final double TRIGGER_THRESHOLD_LIMIT = 30; //amp
  private final double TRIGGER_THRESHOLD_TIME = 0.2; //s

  private boolean lastCall;

  	/**
	 * Which PID slot to pull gains from. Starting 2018, you can choose from
	 * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
	 * configuration.
	 */
	public static final int kSlotIdx = 0;

	/**
	 * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
	 * now we just want the primary one.
	 */
	public static final int kPIDLoopIdx = 0;

	/**
	 * set to zero to skip waiting for confirmation, set to nonzero to wait and
	 * report to DS if action fails.
	 */
	public static final int kTimeoutMs = 30;

	/**
	 * Gains used in Motion Magic, to be adjusted accordingly
     * Gains(kp, ki, kd, kf, izone, peak output);
     */
  static final Gains kGainsUp = new Gains(1.6, 0.0, 0.0, 0.0, 0, 1.0);
  static final Gains kGainsDown = new Gains(1.6, 0.0, 0.0, 0.0, 0, 1.0);
  static final double ARB_FEEDFORWARD_UP = 0.2;
  static final double ARB_FEEDFORWARD_DOWN = 0.0;
  private static final double MIN_UPWARDS_SPEED = 0.025;


  /**
   * Convert target RPM to ticks / 100ms.
   * 256*4x (quadrature encoder) Ticks/Rev *  RPM / 600 100ms/min in either direction:
   * velocity setpoint is in units/100ms
   */
  final double TICKS_PER_REV = 8192; //one event per edge on each quadrature channel
  final double TICKS_PER_100MS = TICKS_PER_REV / 10.0;
  final double GEAR_RATIO = 42.0;
  final double SPOOL_CIRCUMFERENCE = 3.625; //TODO CHECK
  final double TICKS_PER_INCH = 2245.0; //TICKS_PER_REV * GEAR_RATIO / SPOOL_CIRCUMFERENCE;
  final double TICKS_PER_INCH_PER_100MS = 2245.0 / 10.0; //TICKS_PER_100MS * GEAR_RATIO / SPOOL_CIRCUMFERENCE;

  private double setPoint_sensorunits;


  private Climber() {
    climberMotor1 = new TalonSRX(RobotMap.CLIMBER_1_CAN_ID);
    climberMotor2 = new TalonSRX(RobotMap.CLIMBER_2_CAN_ID);
    climberSolenoid = new DoubleSolenoid(RobotMap.CLIMBER_RATCHET_ENGAGE_PCM,RobotMap.CLIMBER_RATCHET_DISENGAGE_PCM);

    /* Factory Default all hardware to prevent unexpected behavior */
    climberMotor1.configFactoryDefault();
    climberMotor2.configFactoryDefault();

    climberMotor1.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
    hallEffectSensor = new CanDigitalInput(climberMotor1);

    climberMotor1.setNeutralMode(NeutralMode.Brake);
    climberMotor2.setNeutralMode(NeutralMode.Brake);


    /* Configure the left Talon's selected sensor as local QuadEncoder */
    climberMotor1.configSelectedFeedbackSensor(	FeedbackDevice.QuadEncoder,				// Local Feedback Source
                          kPIDLoopIdx,					// PID Slot for Source [0, 1]
                          kTimeoutMs);					// Configuration Timeout



    /**
    * Phase sensor accordingly.
    * Positive Sensor Reading should match Green (blinking) Leds on Talon
    */
    climberMotor1.setSensorPhase(true);

    /***
     * invert motors if necessary
     */
    climberMotor1.setInverted(CLIMBER_MOTOR_1_REVERSE);
    climberMotor2.setInverted(CLIMBER_MOTOR_2_REVERSE);

        /* Set relevant frame periods to be at least as fast as periodic rate */
    climberMotor1.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, kTimeoutMs);
    climberMotor1.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, kTimeoutMs);

    /* Set the peak and nominal outputs */
    climberMotor1.configNominalOutputForward(0, kTimeoutMs);
    climberMotor1.configNominalOutputReverse(0, kTimeoutMs);
    climberMotor1.configPeakOutputForward(1, kTimeoutMs);
    climberMotor1.configPeakOutputReverse(-1, kTimeoutMs);

    /* Set Motion Magic gains in slot0 - see documentation */
    climberMotor1.selectProfileSlot(kSlotIdx, kPIDLoopIdx);
    climberMotor1.config_kF(kSlotIdx, kGainsUp.kF, kTimeoutMs);
    climberMotor1.config_kP(kSlotIdx, kGainsUp.kP, kTimeoutMs);
    climberMotor1.config_kI(kSlotIdx, kGainsUp.kI, kTimeoutMs);
    climberMotor1.config_kD(kSlotIdx, kGainsUp.kD, kTimeoutMs);
    climberMotor1.config_kF(kSlotIdx, kGainsUp.kF, kTimeoutMs);
    climberMotor1.config_IntegralZone(kSlotIdx, kGainsUp.kIzone, kTimeoutMs);
    climberMotor1.configClosedLoopPeakOutput(kSlotIdx, kGainsUp.kPeakOutput, kTimeoutMs);

    /* Set acceleration and vcruise velocity - see documentation */
    climberMotor1.configMotionCruiseVelocity((int) (24*TICKS_PER_INCH_PER_100MS), kTimeoutMs); //todo set
    climberMotor1.configMotionAcceleration((int) (24*TICKS_PER_INCH_PER_100MS), kTimeoutMs); //todo set

    talonCurrentLimit = new SupplyCurrentLimitConfiguration(ENABLE_CURRENT_LIMIT,
    CONTINUOUS_CURRENT_LIMIT, TRIGGER_THRESHOLD_LIMIT, TRIGGER_THRESHOLD_TIME);

    climberMotor1.configSupplyCurrentLimit(talonCurrentLimit);
    climberMotor2.configSupplyCurrentLimit(talonCurrentLimit);

    /* Zero the sensor */
    climberMotor1.setSelectedSensorPosition(0, kPIDLoopIdx, kTimeoutMs);

    ConsolePrinter.putBoolean("Lift is down", () -> {return isLiftDown();}, true, false);

    lastCall = isLiftDown();
    ConsolePrinter.putNumber("Climber Position", ()->{return getPosition();}, true, false);
    ConsolePrinter.putNumber("Climber Position Error", ()->{return getErrorPosition();}, true, false);
    ConsolePrinter.putNumber("Climber Velocity", ()->{return getVelocity();}, true, false);
  }

  /**
   * This method creates a new instance of the climber, allowing other
   * programs, i.e. the commands, to utilize it.
   * @return - Returns the new instance of the climber.
   */
  public static Climber getInstance()
  {
    if (instance == null)
      instance = new Climber();
    return instance;
  }

  /**
   *  This method will set the climbers motors to a new speed, allowing
   * the motors to be operated.
   * @param speed - 1 is to raise the climber, and -1 is to lower it.
   */

  public void driveClimberMotors(double speed){
    if(speed > MIN_UPWARDS_SPEED) {
    disengageRatchet();
    } else {
     engageRatchet();
    }
    driveClimberMotor1(speed);
    driveClimberMotor2(speed);
  }

  /**
   * This method is mostly used for testing only motor one.
   * @param speed - 1 is to raise the climber, and -1 is to lower it.
   */
  public void driveClimberMotor1(double speed){
    // if(CLIMBER_MOTOR_1_REVERSE){
    //   speed = -speed;
    // }

    climberMotor1.set(ControlMode.PercentOutput, speed);
  }
  /**
   * This method is used for testing only motor two.
   * @param speed - 1 is to raise the climber, and -1 is to lower it.
   */
  public void driveClimberMotor2(double speed){
    // if(CLIMBER_MOTOR_2_REVERSE){
    //   speed = -speed;
    // }
    climberMotor2.set(ControlMode.PercentOutput, speed);
  }

  /**
   * This method allows the ratchet to extend, preventing the climber
   * from moving from the lowered position.
   */
  public void disengageRatchet(){
    climberSolenoid.set(DoubleSolenoid.Value.kForward);
  }

  /**
   * This method allows the rathcet to retract, allowing the climber to move
   *  to a raised position.
   */
  public void engageRatchet(){
    climberSolenoid.set(DoubleSolenoid.Value.kReverse);
  }

  /**
   * This will allow the program to return whether the ratchet is extended or not.
   * @return
   */
  public boolean isRatchetEngaged(){
    return climberSolenoid.get() == DoubleSolenoid.Value.kReverse;
  }

  public boolean isRatchetDisengaged(){
    return climberSolenoid.get() == DoubleSolenoid.Value.kForward;

  }


  public double getPosition()
  {
    return climberMotor1.getSelectedSensorPosition(kPIDLoopIdx) /(TICKS_PER_INCH);
  }

  public double getVelocity()
  {
    return climberMotor1.getSelectedSensorVelocity(kPIDLoopIdx) /(TICKS_PER_INCH_PER_100MS);
  }

  public void setGains(double setPoint)
  {
    Gains gains;
    if(setPoint>getPosition())
    {
      gains = kGainsUp;
    }
    else
    {
      gains = kGainsDown;
    }
    climberMotor1.config_kF(kSlotIdx, gains.kF, kTimeoutMs);
    climberMotor1.config_kP(kSlotIdx, gains.kP, kTimeoutMs);
    climberMotor1.config_kI(kSlotIdx, gains.kI, kTimeoutMs);
    climberMotor1.config_kD(kSlotIdx, gains.kD, kTimeoutMs);
    climberMotor1.config_kF(kSlotIdx, gains.kF, kTimeoutMs);
    climberMotor1.config_IntegralZone(kSlotIdx, gains.kIzone, kTimeoutMs);
    climberMotor1.configClosedLoopPeakOutput(kSlotIdx, gains.kPeakOutput, kTimeoutMs);

  }

  public void setSetPoint(double setPoint)
  {
    double arbFeedForward;
    if(setPoint>getPosition())
    {
      arbFeedForward = ARB_FEEDFORWARD_UP;
    }
    else
    {
      arbFeedForward = ARB_FEEDFORWARD_DOWN;
    }
    setPoint_sensorunits = setPoint *TICKS_PER_INCH;
    climberMotor1.set(ControlMode.MotionMagic, setPoint_sensorunits, DemandType.ArbitraryFeedForward, arbFeedForward);
    climberMotor2.follow(climberMotor1, FollowerType.PercentOutput);
  }

  public double getErrorPosition()
  {
    return (setPoint_sensorunits-climberMotor1.getSelectedSensorPosition(kPIDLoopIdx)) / (TICKS_PER_INCH);
    //return climberMotor1.getClosedLoopError(kPIDLoopIdx)/TICKS_PER_REV;--only for nonMotionMagic or nonMotion Profile
  }

  public void zeroEncoder()
  {
    climberMotor1.setSelectedSensorPosition(0);
  }

   /**
   * @return true if magnet is sensed, otherwise returns false
   * magnet sensed if lift is down
   */

  public boolean isLiftDown()
  {
    return hallEffectSensor.getForwardLimit();
  }

  /**
   * Checks the current hall effect sensor value against the last cycle's value
   * if the previous value false(the lift was up), but the current value is true(the lift is down),
   * sets the encoder value 0
   * updates "lastCall" variable to the current value of the sensor
   */

  public void zeroEncoderWhenLiftIsDown()
  {
    if (lastCall == false && isLiftDown())
    {
      zeroEncoder();
    }
    lastCall = isLiftDown();
  }

  @Override
  /**
   * This sets the default command to drive via a joystick.
   */
  public void initDefaultCommand() {
    setDefaultCommand(new DriveClimberWithJoystick());
  }

}