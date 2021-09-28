// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveDistance extends CommandBase {
  /** Creates a new DriveDistance. */
  private double m_distance;
  private double m_speed;
  private Drivetrain m_drive;
  public DriveDistance(double feet, double speed, Drivetrain drive) {
    addRequirements(drive);
    m_distance = feet;
    m_speed = speed;
    m_drive = drive;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_drive.arcadeDrive(0, 0);
    m_drive.resetEncoders();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    DriveWithConstant(m_speed, 0, 0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.arcadeDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_drive._wheels.externalEncoder.getInternalEncoderPos >= m_distance * m_drive.FEET_PER_TICKS;
  }
}
