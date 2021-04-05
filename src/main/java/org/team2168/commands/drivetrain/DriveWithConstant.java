/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.drivetrain;

import org.team2168.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

public class DriveWithConstant extends Command {
  private Drivetrain dt;
  private int forward;
  private int strafe;
  private int azimuth;

  public DriveWithConstant(int forward, int strafe, int azimuth) {
    dt = Drivetrain.getInstance();
    this.forward = forward;
    this.strafe = strafe;
    this.azimuth = azimuth;
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
    dt.drive(forward, strafe, azimuth);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
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
