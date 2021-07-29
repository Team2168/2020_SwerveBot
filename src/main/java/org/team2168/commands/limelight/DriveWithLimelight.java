// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.limelight;

import org.team2168.OI;
import org.team2168.subsystems.Limelight;
import org.team2168.utils.smartdashboarddatatypes.SmartDashboardDouble;
import org.team2168.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveWithLimelight extends Command {
  public static final double P = 0.025;
  public static final double I = 0.98;  // I don't think there's a reason there would be steady-state error - nothing is resisting the setpoint
  public static final double D = 0.0038;
  private static final double MINIMUM_COMMAND = 0.05;
  public static final double MAX_INTEGRATOR = 0.1;
  public static final double DEADZONE = 0.40;

  private static final boolean useNTValues = false; // used for debugging because pid tuning is painful and compile-test-compile is stupid for 3 constants

  private PIDController pid;
  private Drivetrain dt;
  private Limelight lime;
  private OI oi;

  private SmartDashboardDouble p;
  private SmartDashboardDouble i;
  private SmartDashboardDouble d;

  private double angularOffset = 0.0;

  /**
   *
   * @param offset the angular position offset (degrees) from the limelights reported angle
   *    to target reported by the getPosition() method. Positive values are clockwise.
   *    Dumb way to handle when the center of the target isn't what you want to aim for
   *    due to parallax of a goal with depth.
   */
  public DriveWithLimelight(double offset) {
    dt = Drivetrain.getInstance();
    lime = Limelight.getInstance();
    angularOffset = offset;

    p = new SmartDashboardDouble("turn_limelight_P", P);
    i = new SmartDashboardDouble("turn_limelight_I", I);
    d = new SmartDashboardDouble("turn_limelight_D", D);

    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(dt);
    requires(lime);
  }

  public DriveWithLimelight() {
    this(0.0);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    lime.setToDriveWithLimelight();
    if (useNTValues)
      pid = new PIDController(p.get(), i.get(), d.get());
    else
      pid = new PIDController(P, I, D);
    oi = OI.getInstance(); // prevents a loop; oi constructs DriveWithLimelight when constructed
    pid.setTolerance(DEADZONE);
    pid.setIntegratorRange(-MAX_INTEGRATOR, MAX_INTEGRATOR);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double error = lime.getPosition();
    SmartDashboard.putNumber("Limelight Error", error);
    double steering_adjust = 0.0;

    //Optionally set through constructor. Add in a fixed offset from the LL's
    // reported target center.
    error = error - angularOffset;

    if (error < -DEADZONE) {
      steering_adjust = pid.calculate(error) - MINIMUM_COMMAND;
    } else if (error > DEADZONE) {
      steering_adjust = pid.calculate(error) + MINIMUM_COMMAND;
    }

    // dt.drive(oi.getDriverJoystickYValue(), oi.getDriverJoystickXValue(), -steering_adjust);
    dt.drive(0.0, 0.0, -steering_adjust);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Math.abs(lime.getPosition()) < DEADZONE;
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
