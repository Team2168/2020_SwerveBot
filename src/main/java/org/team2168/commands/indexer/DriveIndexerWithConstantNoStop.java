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

public class DriveIndexerWithConstantNoStop extends Command { 
  private double _speed;
  private Indexer _indexer;
  public DriveIndexerWithConstantNoStop(double speed) {
    _indexer = Indexer.getInstance();
    _speed = speed;
    requires(_indexer);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(_speed != 0.0) {
      Robot.setCompressorOn(false);
    }
    else {
      Robot.setCompressorOn(true);
    }
    _indexer.drive(_speed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.setCompressorOn(true);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
