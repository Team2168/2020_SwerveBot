// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.drivetrain;

import org.team2168.OI;
import org.team2168.utils.smartdashboarddatatypes.SmartDashboardDouble;
import org.team2168.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.controller.PIDController;

public class DriveWithFixedAzimuth extends Command {
  public static final double P = 0.025;
  public static final double I = 0.98;
  public static final double D = 0.0038;
  private static final double MINIMUM_COMMAND = 0.05;
  public static final double MAX_INTEGRATOR = 0.1;
  public static final double DEADZONE = 0.40;

  private static final boolean useNTValues = false; // used for debugging because pid tuning is painful and compile-test-compile is stupid for 3 constants

  private PIDController pid;
  private Drivetrain dt;
  private OI oi;

  private SmartDashboardDouble p;
  private SmartDashboardDouble i;
  private SmartDashboardDouble d;
  private boolean useJoystick;
  private double targetAzimuth = 0.0;

  /**
   *
   * @param azimuth the target heading for the chassis (fwd the direction the shooter faces).
   *    Degrees relative to gyro zero.
   * @param useJoystick true to allow driving the robot with the in X/Y while the gyro controls yaw,
   *    otherwise sit still while rotating
   */
  public DriveWithFixedAzimuth(double azimuth, boolean useJoystick) {
    dt = Drivetrain.getInstance();
    targetAzimuth = azimuth;
    this.useJoystick = useJoystick;

    p = new SmartDashboardDouble("turn_limelight_P", P);
    i = new SmartDashboardDouble("turn_limelight_I", I);
    d = new SmartDashboardDouble("turn_limelight_D", D);

    requires(dt);
  }

  /**
   * Sit still and rotate to the target heading.
   *
   * @param azimuth the target heading for the chassis (fwd the direction the shooter faces).
   *    Degrees relative to gyro zero.
   */
  public DriveWithFixedAzimuth(double azimuth) {
    this(azimuth, false);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
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
    double error = getPositionError();
    double steering_adjust = 0.0;

    if (error < -DEADZONE) {
      steering_adjust = pid.calculate(error) - MINIMUM_COMMAND;
    } else if (error > DEADZONE) {
      steering_adjust = pid.calculate(error) + MINIMUM_COMMAND;
    }

    if (useJoystick)
      dt.drive(oi.getDriverJoystickYValue(), oi.getDriverJoystickXValue(), -steering_adjust);
    else
      dt.drive(0.0, 0.0, -steering_adjust);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if (useJoystick) {
      return false;
    } else {
      return Math.abs(getPositionError()) < DEADZONE;
    }
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

  /**
   *
   * @return how far off we are from the target position (degrees, positive clockwise)
   */
  private double getPositionError() {
    return targetAzimuth - (dt.getHeading() % 360.0);
  }
}
