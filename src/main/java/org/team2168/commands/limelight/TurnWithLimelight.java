/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.limelight;

import edu.wpi.first.wpilibj.command.Command;

import org.team2168.OI;
import org.team2168.subsystem.Drivetrain;
import org.team2168.subsystem.Limelight;
import org.team2168.utils.LinearInterpolator;

public class TurnWithLimelight extends Command {
private Drivetrain dt;
private Limelight lime;
private OI oi;
private LinearInterpolator scalar;
private static final double MAX_SPEED = 0.20;
private static final double MIN_SPEED = 0.10;
private static final double DEADZONE = 0.45;
private static final double LOW_POINT = 0.65;
private static double[][] scaling = {
  {-27.00, -MAX_SPEED},
  {-LOW_POINT, -MIN_SPEED},
  {-DEADZONE, 0},
  {DEADZONE, 0},
  {LOW_POINT, MIN_SPEED},
  {27.00, MAX_SPEED}
};

  public TurnWithLimelight() {
    dt = Drivetrain.getInstance();
    lime = Limelight.getInstance();
    scalar = new LinearInterpolator(scaling);
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(dt);
    requires(lime);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    oi = OI.getInstance();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    dt.drive(oi.getDriverJoystickYValue(), oi.getDriverJoystickXValue(), scalar.interpolate(lime.getXOffset()));
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
