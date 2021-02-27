/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
/* this method allows the driver to operate the intake motor with a joystick*/
package org.team2168.commands.intakeMotor;

import org.team2168.OI;
import org.team2168.subsystems.IntakeMotor;

import edu.wpi.first.wpilibj.command.Command;

public class DriveIntakeWithJoystick extends Command {
  private IntakeMotor intakeMotor;
  private OI oi;
  
  public DriveIntakeWithJoystick() {
    intakeMotor = IntakeMotor.getInstance();
    requires(intakeMotor);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    oi = OI.getInstance();
  }

  /**
   * Gets joystick positions from OI.
   * checks if it's below the maximum speed allowed, which is static and in the intake subsystem
   * if it is, sends joystick position to Intake
   * if it's above, sets motor speed to max speed
   * 
   * @author Ian
   */
  @Override
  protected void execute() {
    if (Math.abs(oi.getIntakeMotorJoyStick()) < IntakeMotor.MAX_SPEED)
    {
      intakeMotor.driveMotor(oi.getIntakeMotorJoyStick());
    }
    else
    {
      intakeMotor.driveMotor(IntakeMotor.MAX_SPEED);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    intakeMotor.driveMotor(0.0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}