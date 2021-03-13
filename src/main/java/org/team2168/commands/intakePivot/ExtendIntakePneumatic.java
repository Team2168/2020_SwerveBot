/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
/* this command allows the pneumatic to be extended*/
package org.team2168.commands.intakePivot;

import org.team2168.subsystems.IntakePivot;

import edu.wpi.first.wpilibj.command.Command;

public class ExtendIntakePneumatic extends Command {
  private IntakePivot intakePivot;
  public ExtendIntakePneumatic() {
    intakePivot = IntakePivot.getInstance();
    requires(intakePivot); //command for intake
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  /**
   * extends intake pneumatic
   * 
   * @author Ian
   */
  @Override
  protected void execute() {
    intakePivot.extendIntake();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return intakePivot.isIntakeExtended(); //checks if intake pneumatic is extended
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end(); //for if there is ever anything added
  }
}