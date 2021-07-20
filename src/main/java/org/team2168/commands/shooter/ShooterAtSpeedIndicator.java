// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.shooter;

import org.team2168.subsystems.Shooter;
import org.team2168.utils.smartdashboarddatatypes.SmartDashboardBoolean;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterAtSpeedIndicator extends Command {
  private Shooter shooter;
  private double errorTolerance;
  private SmartDashboardBoolean atSpeedIndicator;

  private boolean atSpeed = false;
  private double loopsToSettle = 10.0;
  private int withinThresholdLoops = 0;
  private static final double DEFAULT_ERROR_TOLERANCE = 50.0;

  public ShooterAtSpeedIndicator() {
    this(DEFAULT_ERROR_TOLERANCE);
  }
  public ShooterAtSpeedIndicator(double errorTolerance) {
    // don't require shooter so the shooter can still run
    atSpeedIndicator = new SmartDashboardBoolean("shooterAtSpeed");

    shooter = Shooter.getInstance();
    this.errorTolerance = errorTolerance;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {}

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (Math.abs(shooter.getError()) < errorTolerance)
      ++withinThresholdLoops;
    else
      withinThresholdLoops = 0;
    
    var isShooterAtSpeed = (withinThresholdLoops > loopsToSettle);

    // doing this to minimize the amount of data sent over the network
    if (isShooterAtSpeed != atSpeed)
      atSpeed = isShooterAtSpeed;
      atSpeedIndicator.set(isShooterAtSpeed);

      
    
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
