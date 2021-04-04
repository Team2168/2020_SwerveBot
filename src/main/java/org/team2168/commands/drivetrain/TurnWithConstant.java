/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.drivetrain;

import org.team2168.subsystem.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

public class TurnWithConstant extends Command {
  private Drivetrain dt;
  private double turningVel;

  public TurnWithConstant(double turningVel) {
    this.turningVel = turningVel;
    dt = Drivetrain.getInstance();
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(dt);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    dt.drive(0.0, 0.0, turningVel);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
