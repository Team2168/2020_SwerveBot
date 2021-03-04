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
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.team2168.RobotMap;
import org.team2168.commands.hopper.DriveHopperWithJoystick;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem class for the Hopper
 */
public class Hopper extends Subsystem {
  public static final boolean HOPPER_MOTOR_INVERTED = true;
  private TalonSRX hopperMotor;

  private static Hopper _instance = null;

  private SupplyCurrentLimitConfiguration talonCurrentLimit;
  private final boolean ENABLE_CURRENT_LIMIT = true;
  private final double CONTINUOUS_CURRENT_LIMIT = 20; //amps
  private final double TRIGGER_THRESHOLD_LIMIT = 30; //amp
  private final double TRIGGER_THRESHOLD_TIME = 0.2; //s

  private Hopper() {
    hopperMotor = new TalonSRX(RobotMap.HOPPER_CAN_ID);

    talonCurrentLimit = new SupplyCurrentLimitConfiguration(ENABLE_CURRENT_LIMIT,
    CONTINUOUS_CURRENT_LIMIT, TRIGGER_THRESHOLD_LIMIT, TRIGGER_THRESHOLD_TIME);

    hopperMotor.configFactoryDefault();
    hopperMotor.configSupplyCurrentLimit(talonCurrentLimit);

    hopperMotor.setNeutralMode(NeutralMode.Brake);
    hopperMotor.setInverted(HOPPER_MOTOR_INVERTED);
    hopperMotor.configNeutralDeadband(0.05);
  }

  /**
   * Returns a singleton instance of the Hopper
   */
  public static Hopper getInstance() {
    if(_instance == null) {
      _instance = new Hopper();
    }
    return _instance;
  }

  /**
   * Drives the hopper motor at the specified speed
   * @param speed is a double from -1 to 1; positive is heading towards the shooter
   */
  public void drive(double speed) {
    hopperMotor.set(ControlMode.PercentOutput, speed);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    //setDefaultCommand(new DriveHopperWithJoystick());
  }
}