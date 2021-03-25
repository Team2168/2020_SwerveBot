/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.limelight;

import edu.wpi.first.wpilibj.command.Command;
import org.team2168.subsystem.Drivetrain;
import org.team2168.subsystem.Limelight;
import org.team2168.utils.LinearInterpolator;

public class TurnWithLimelight extends Command {
private Drivetrain dt;
private Limelight lime;
private static LinearInterpolator scalar = null;

private static double[][] scaling = {
  {-27.00, -0.10},
  {-0.25, -0.10},
  {-0.05, 0},
  {0.05, 0},
  {0.25, 0.10},
  {27.00, 0.10}
};

private static LinearInterpolator getScalar() {
  if (scalar == null)
    scalar = new LinearInterpolator(scaling);
  return scalar;
}

  public TurnWithLimelight() {
    dt = Drivetrain.getInstance();
    lime = Limelight.getInstance();
    scalar = getScalar();
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(dt);
    requires(lime);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    dt.drive(0.0, 0.0, scalar.interpolate(lime.getXOffset()));
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
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
