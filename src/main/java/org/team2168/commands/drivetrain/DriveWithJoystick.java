/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.team2168.OI;
import org.team2168.subsystems.Drivetrain;

public class DriveWithJoystick extends Command {
  private OI oi;
  private Drivetrain dt;

  /**
   * Creates a new DriveWithJoystick.
   */
  public DriveWithJoystick() {
    // Use addRequirements() here to declare subsystem dependencies.

    dt = Drivetrain.getInstance();

    requires(dt);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    oi = OI.getInstance();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (SmartDashboard.getString("Control Mode", "Joystick").equals("Joystick")) {
      dt.drive(oi.getDriverJoystickYValue(), oi.getDriverJoystickXValue(), oi.getDriverJoystickZValue());
    }
    else {
      dt.drive(SmartDashboard.getNumber("Drive Forward", 0.0), SmartDashboard.getNumber("Drive Strafe", 0.0), SmartDashboard.getNumber("Drive Azimuth", 0.0));
    }
    SmartDashboard.putNumber("Joystick Y", oi.getDriverJoystickYValue());
    SmartDashboard.putNumber("Joystick X", oi.getDriverJoystickXValue());
    SmartDashboard.putNumber("Joystick Z", oi.getDriverJoystickZValue());
  }

  // Called once the command ends
  @Override
  public void end() {
    dt.stop();
  }

  @Override
  public void interrupted() {
    end();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
