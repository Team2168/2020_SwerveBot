// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.limelight;

import org.team2168.subsystem.Limelight;

import edu.wpi.first.wpilibj.command.Command;

public class RefreshValuesLimelight extends Command {
  private Limelight lime;
  public RefreshValuesLimelight() {
    // Use requires() here to declare subsystem dependencies
    lime = Limelight.getInstance();
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {}

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    lime.refreshDashboardValues();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {}

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {}
}
