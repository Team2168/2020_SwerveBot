/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.subsystems;

import org.team2168.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class IntakePivot extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private DoubleSolenoid intakePivot;

  private static IntakePivot _instance = null;

  /**
   * Default constructors for Intake
   */
  private IntakePivot() {
    intakePivot = new DoubleSolenoid(RobotMap.INTAKE_EXTEND_PCM, RobotMap.INTAKE_RETRACT_PCM);
  }

  /**
   * @return an instance of the Intake Subsystem
   */
  public static IntakePivot getInstance() {
    if (_instance == null)
    {
      _instance = new IntakePivot();
    }
    return _instance;
  }

    /**
   * extends intake pneumatic
   */
  public void extendIntake() {
    intakePivot.set(DoubleSolenoid.Value.kForward);
  }

  /**
   * retracts intake pneumatic
  */
  public void retractIntake() {
    intakePivot.set(DoubleSolenoid.Value.kReverse);
  }

  /**
   * checks if pneumatic is extended
   * @return a boolean, whether or not the pneumatic is extended
   */
  public boolean isIntakeExtended() {
    return intakePivot.get() == DoubleSolenoid.Value.kForward;
  }

  /**
   * checks if pneumatic is retracted
   * @return a boolean, whether or not the pneumatic is retracted
  */
  public boolean isIntakeRetracted() {
    return intakePivot.get() == DoubleSolenoid.Value.kReverse;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
  }
}