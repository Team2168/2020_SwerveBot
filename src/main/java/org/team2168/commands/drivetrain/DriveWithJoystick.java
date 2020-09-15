/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import org.team2168.subsystem.Drivetrain;
import org.team2168.OI;
import java.lang.Math;

public class DriveWithJoystick extends Command {
  /**
   * Creates a new DriveWithJoystick.
   */
  private OI oi;
  private Drivetrain dt;
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
    //dw.set(oi.getGunStyleXValue(), Math.abs(oi.getGunStyleYValue())); // abs for testing purposes; set doesn't take negative values
  }

  // Called once the command ends
  @Override
  public void end() {
    //dw.stop();
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
