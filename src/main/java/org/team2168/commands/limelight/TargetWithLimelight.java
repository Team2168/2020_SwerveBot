// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.limelight;

import edu.wpi.first.wpilibj.controller.PIDController;

import org.team2168.subsystems.Drivetrain;
import org.team2168.subsystems.Limelight;

import edu.wpi.first.wpilibj.command.Command;

public class TargetWithLimelight extends Command {
  private PIDController pid;
  private Drivetrain dt;
  private Limelight lime;

  public TargetWithLimelight() {
    dt = Drivetrain.getInstance();
    lime = Limelight.getInstance();
    pid = new PIDController(DriveWithLimelight.P, DriveWithLimelight.I, DriveWithLimelight.D);

    pid.setTolerance(DriveWithLimelight.DEADZONE);
    pid.setIntegratorRange(-DriveWithLimelight.MAX_INTEGRATOR, DriveWithLimelight.MAX_INTEGRATOR);

    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(dt);
    requires(lime);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {}

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
