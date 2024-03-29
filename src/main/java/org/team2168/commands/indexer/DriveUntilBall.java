/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.indexer;

import org.team2168.Robot;
import org.team2168.subsystems.Indexer;

import edu.wpi.first.wpilibj.command.Command;

public class DriveUntilBall extends Command {
  private Indexer indexer;
  private double _speed;
  public DriveUntilBall(double speed) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    indexer = Indexer.getInstance();
    requires(indexer);
    this._speed = speed;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    // Robot.setCompressorOn(false);
    indexer.drive(_speed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return indexer.isBallExiting(); // || indexer.isBallExiting();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    // Robot.setCompressorOn(true);
    //indexer.drive(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
