/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.drivetrain;

import org.team2168.subsystems.Drivetrain;
import org.team2168.thirdcoast.swerve.SwerveDrive;
import org.team2168.thirdcoast.swerve.Wheel;
import org.team2168.thirdcoast.swerve.SwerveDrive.DriveMode;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveAzimuthWithConstant extends Command {
  private Drivetrain dt;
  DriveMode lastMode;

  public DriveAzimuthWithConstant() {
    dt = Drivetrain.getInstance();
    requires(dt);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    SmartDashboard.putNumber("Azimuth 0", 0.0);
    lastMode = dt.getDriveMode();
    dt.setDriveMode(DriveMode.MANUAL_AZIMUTH_TEST);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double percent = SmartDashboard.getNumber("Azimuth 0", 0.0) / 360;
    dt.getWheels()[0].set(percent, 0.0);
    SmartDashboard.putNumber("Desired position", Wheel.externalToInternalTicks(Preferences.getInstance().getInt(SwerveDrive.getPreferenceKeyForWheel(0), SwerveDrive.DEFAULT_ABSOLUTE_AZIMUTH_OFFSET)) + Wheel.degreesToTicksAzimuth(SmartDashboard.getNumber("Azimuth 0", 0.0)));
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    dt.setDriveMode(lastMode);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
