// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.auto;

import org.team2168.subsystems.Shooter;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.command.Command;

public class InitShooter extends Command {
  private Shooter shoot;
  private boolean hasCommandRun;
  public InitShooter() {
    shoot = Shooter.getInstance();
    hasCommandRun = false;
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(shoot);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {}

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    ConsolePrinter.putNumber("shooter vel (used for testing!!)", () -> shoot.getVelocity(), true, false);
    hasCommandRun = true;
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return hasCommandRun;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {}

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {}
}
