/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.shooter;

import org.team2168.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

public class WaitForShooterAtSpeed extends Command {
  
  private Shooter shooter;
  private double _errorTolerance;
  private double _loopsToSettle = 10;
  private int _withinThresholdLoops = 0;
  

  public WaitForShooterAtSpeed(double errorTolerance) {
    // this can't require the shooter because we want shooter to keep running independently
    shooter = Shooter.getInstance();
    this._errorTolerance = errorTolerance;
  }

  public WaitForShooterAtSpeed() {
    this(Shooter.DEFAULT_ERROR_TOLERANCE);
  }
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    /* Check if closed loop error is within the threshld */
    if (Math.abs(shooter.getError()) < _errorTolerance) {
      ++_withinThresholdLoops;
    } 
    else {
      _withinThresholdLoops = 0;
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return _withinThresholdLoops > _loopsToSettle;
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
