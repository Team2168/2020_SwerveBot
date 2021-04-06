/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.limelight;

import org.team2168.subsystem.Limelight;

import edu.wpi.first.wpilibj.command.Command;

public class ToggleLimelightPipeline extends Command {
  private Limelight lime;
  private int pipeline;
  private boolean isFinished = false;
  public ToggleLimelightPipeline(int pipeline) {
    lime = Limelight.getInstance();
    this.pipeline = pipeline;
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(lime);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (lime.getPipeline() == pipeline) {
      lime.pauseLimelight();
    } else {
      lime.setPipeline(pipeline);
      isFinished = true; // so that it continues execution and isn't overriden by the default command
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return isFinished;
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
