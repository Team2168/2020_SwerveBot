// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.limelight;

import org.team2168.subsystem.Drivetrain;
import org.team2168.subsystem.Limelight;
import org.team2168.utils.LinearInterpolator;

import edu.wpi.first.wpilibj.command.Command;

public class DriveWithLimelight extends Command {
  private Limelight lime;
  private Drivetrain dt;

  private LinearInterpolator limeInterpolator;
  private double[][] limeScaling = {
    {-27.00, -0.50},
    {-0.25, 0.00},
    {0.25, 0.00},
    {27.00, 0.50}
  };
  public DriveWithLimelight() {
    lime = Limelight.getInstance();
    dt = Drivetrain.getInstance();
    limeInterpolator = new LinearInterpolator(limeScaling);

    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(lime);
    requires(dt);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {}

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    dt.drive(0.0, limeInterpolator.interpolate(lime.getXOffset()), 0.0);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {}

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {}
}
