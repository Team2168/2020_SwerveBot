// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.limelight;

import org.team2168.OI;
import org.team2168.subsystem.Drivetrain;
import org.team2168.subsystem.Limelight;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.controller.PIDController;

public class DriveWithLimelight2 extends Command {
  private static final double P = 0.032;
  private static final double I = 0.090;
  // private static final double I = 0.0;
  // private static final double D = 0.002;  
  private static final double D = 0.00;  
  private static final double MINIMUM_COMMAND = 0.05;
  private static final double MAX_INTEGRATOR = 0.09;
  private static final double DEADZONE = 0.25;
  

  private PIDController pid;
  private Drivetrain dt;
  private Limelight lime;
  private OI oi;

  public DriveWithLimelight2() {
    dt = Drivetrain.getInstance();
    lime = Limelight.getInstance();
    pid = new PIDController(P, I, D);

    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(dt);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    oi = OI.getInstance(); // prevents a loop; oi constructs drivewithlimelight2 when constructed
    pid.setTolerance(DEADZONE);
    pid.setIntegratorRange(-MAX_INTEGRATOR, MAX_INTEGRATOR);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    var error = lime.getXOffset();
    var steering_adjust = 0.0;
    if (error < -DEADZONE) {
      steering_adjust = pid.calculate(error) - MINIMUM_COMMAND;
    } else if (error > DEADZONE) {
      steering_adjust = pid.calculate(error) + MINIMUM_COMMAND;
    }

    dt.drive(oi.getDriverJoystickYValue(), oi.getDriverJoystickXValue(), -steering_adjust);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return pid.atSetpoint();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {}

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {}
}
