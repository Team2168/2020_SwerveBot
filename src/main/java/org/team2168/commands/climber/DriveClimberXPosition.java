/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.climber;

import org.team2168.subsystems.Climber;

import edu.wpi.first.wpilibj.command.Command;

public class DriveClimberXPosition extends Command {

  private Climber climber;
  /**target position */
  private double _targetPos;
  
  private final static double DEFAULT_ERROR_TOLERANCE = 1.5;
  private double _errorTolerance; //inches
  private double _loopsToSettle = 5;
  private int _withinThresholdLoops = 0;

  public DriveClimberXPosition(double setPoint) {
    this(setPoint, DEFAULT_ERROR_TOLERANCE);
  }

  public DriveClimberXPosition(double setPoint, double errorTolerance) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    climber = Climber.getInstance();
    requires(climber);
    _errorTolerance = errorTolerance;
    _targetPos = setPoint;
  }

  // public DriveClimberXPosition(double setPoint)
  // {
  //   this.DriveClimberXPosition(setPoint, DEFAULT_ERROR_TOLERANCE);
  // }
  
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    // climber.zeroEncoder(); //don't do this except for testing
    climber.setGains(_targetPos);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    climber.setSetPoint(_targetPos);
    /* Check if closed loop error is within the threshld */
    if (Math.abs(climber.getErrorPosition()) < _errorTolerance) {
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
    climber.driveClimberMotors(0.0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}