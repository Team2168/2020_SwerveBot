// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.limelight;

import org.team2168.OI;
import org.team2168.subsystem.Limelight;
import org.team2168.subsystem.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveWithLimelight extends Command {
  private static final double P = 0.035;
  private static final double I = 0.98;  // I don't think there's a reason there would be steady-state error - nothing is resisting the setpoint
  private static final double D = 0.0038;
  private static final double MINIMUM_COMMAND = 0.05;
  private static final double MAX_INTEGRATOR = 0.1;
  private static final double DEADZONE = 0.40;

  private static final boolean useNTValues = false; // used for debugging because pid tuning is painful and compile-test-compile is stupid for 3 constants
  

  private PIDController pid;
  private Drivetrain dt;
  private Limelight lime;
  private OI oi;

  public DriveWithLimelight() {
    dt = Drivetrain.getInstance();
    lime = Limelight.getInstance();
    if (useNTValues)
      System.out.println("********** WARNING: GETTING TURNWITHLIMELIGHT GAINS FROM NETWORKTABLES **********");

    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(dt);
    requires(lime);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    lime.setToDriveWithLimelight();
    // TODO make this a helper class
    if (useNTValues) {
      if (!SmartDashboard.containsKey("turn_limelight_P"))
        SmartDashboard.putNumber("turn_limelight_P", P);
      if (!SmartDashboard.containsKey("turn_limelight_I"))
        SmartDashboard.putNumber("turn_limelight_I", I);
      if (!SmartDashboard.containsKey("turn_limelight_D"))
        SmartDashboard.putNumber("turn_limelight_D", D);

      double ntP = (double) SmartDashboard.getNumber("turn_limelight_P", P);
      double ntI = (double) SmartDashboard.getNumber("turn_limelight_I", I);
      double ntD = (double) SmartDashboard.getNumber("turn_limelight_D", D);
      pid = new PIDController(ntP, ntI, ntD);
      
    } else {
      pid = new PIDController(P, I, D);
    }
    oi = OI.getInstance(); // prevents a loop; oi constructs drivewithlimelight2 when constructed
    pid.setTolerance(DEADZONE);
    pid.setIntegratorRange(-MAX_INTEGRATOR, MAX_INTEGRATOR);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double error = lime.getPosition();
    double steering_adjust = 0.0;
    if (error < -DEADZONE) {
      steering_adjust = pid.calculate(error) - MINIMUM_COMMAND;
    } else if (error > DEADZONE) {
      steering_adjust = pid.calculate(error) + MINIMUM_COMMAND;
    }
// 
    // dt.drive(oi.getDriverJoystickYValue(), oi.getDriverJoystickXValue(), -steering_adjust);
    dt.drive(0.0, 0.0, -steering_adjust);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return pid.atSetpoint();
    // return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    // lime.pauseLimelight();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {}
}
