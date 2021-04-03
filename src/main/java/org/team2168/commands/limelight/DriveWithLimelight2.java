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
  private static final double P = -0.015;
  private static final double I = 0.0;
  private static final double D = 0.0;  
  private static final double MINIMUM_COMMAND = 0.05;

  private PIDController calculator = null; // so that the integrator works
  private Drivetrain dt;
  private Limelight lime;
  private OI oi;
  public DriveWithLimelight2() {
    dt = Drivetrain.getInstance();
    lime = Limelight.getInstance();
    calculator = new PIDController(P, I, D);

    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    oi = OI.getInstance();
    calculator.setTolerance(0.25);
    calculator.setIntegratorRange(-1.0, 1.0);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    var error = -lime.getXOffset();
    // var steering_adjust = 0.0;
    // if (error > 1.0) {
    //   steering_adjust = P * error - MINIMUM_COMMAND;
    // } else if (error < 1.0) {
    //   steering_adjust = P * error + MINIMUM_COMMAND;
    // }
    var steering_adjust = calculator.calculate(error);
    dt.drive(oi.getDriverJoystickYValue(), oi.getDriverJoystickXValue(), steering_adjust);
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
