/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.BaseTalon;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMax.IdleMode;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;
// import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame;

import org.team2168.RobotMap;
import org.team2168.commands.intakeMotor.DriveIntakeWithJoystick;

import edu.wpi.first.wpilibj.command.Subsystem;


/**
 * Add your docs here.
 */
public class IntakeMotor extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  
  //private CANSparkMax intakeMotor;
  private TalonFX intakeMotor;
  public boolean INTAKE_MOTOR_REVERSE = false; //change manually
  public static final double MAX_SPEED = 1.0;

  private static IntakeMotor _instance = null;

  private SupplyCurrentLimitConfiguration talonCurrentLimit;
  private final boolean ENABLE_CURRENT_LIMIT_SUPPLY = true;
  private final double CONTINUOUS_DRIVE_CURRENT_LIMIT = 40.0; // amps
  private final double TRIGGER_DRIVE_THRESHOLD_LIMIT = 60.0; // amps
  private final double TRIGGER_DRIVE_THRESHOLD_TIME = 0.2; // seconds


  /**
   * Default constructors for Intake
   */
  private IntakeMotor() {
    
    intakeMotor = new TalonFX(RobotMap.INTAKE_CAN_ID);
    //intakeMotor = new CANSparkMax(RobotMap.INTAKE_CAN_ID, MotorType.kBrushless);

    //intakeMotor.setIdleMode(IdleMode.kBrake);
    intakeMotor.setNeutralMode(NeutralMode.Brake);

    
    talonCurrentLimit = new SupplyCurrentLimitConfiguration(ENABLE_CURRENT_LIMIT_SUPPLY,
    CONTINUOUS_DRIVE_CURRENT_LIMIT, TRIGGER_DRIVE_THRESHOLD_LIMIT, TRIGGER_DRIVE_THRESHOLD_TIME);
    // intakeMotor.setSmartCurrentLimit(25); //TODO SET
    // intakeMotor.setControlFramePeriodMs(20);
    // intakeMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus0, 500);
    // intakeMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus1, 500);
    // intakeMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 500);
    intakeMotor.setInverted(INTAKE_MOTOR_REVERSE);
    //TODO is there a way/need to set neutral deadband
  }

  /**
   * @return an instance of the Intake Subsystem
   */
  public static IntakeMotor getInstance() {
    if (_instance == null)
    {
      _instance = new IntakeMotor();
    }
    return _instance;
  }

    /**
   * sets motor speed to input, postive is towards the robot
   * @param speed -1.0 to 1.0. negative is away from the robot, 0 is stationary, positive is towards the robot
   */
  public void driveMotor(double speed)
  {
    //intakeMotor.set(speed);
    intakeMotor.set(ControlMode.PercentOutput,speed);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new DriveIntakeWithJoystick());
  }
}