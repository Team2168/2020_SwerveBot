/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.shooter;

import org.team2168.subsystems.HoodAdjust;
import org.team2168.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;

public class DriveShooterSpeedHoodPosition extends Command {

    private Shooter shooter;
    private HoodAdjust pos;
    /**target position */
    private double _targetVelocity;
    private static final double WALL_VEL = 7160.0; //TODO SET ALL
    private static final double WHITE_LINE_VEL = 3580.0;
    private static final double FRONT_TRENCH_VEL = 4655.0;
    private static final double BACK_TRENCH_VEL = 5000.0; //7160.0


  public DriveShooterSpeedHoodPosition() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    shooter = Shooter.getInstance();
    pos = HoodAdjust.getInstance();
    requires(shooter);
  }


  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    switch(pos.getHoodPosition()){  
      //setting target_velocity based on current position of the hood
      case WALL : 
        _targetVelocity = WALL_VEL;
        break;
      case WHITE_LINE :
        _targetVelocity = WHITE_LINE_VEL;
        break;
      case FRONT_TRENCH :
        _targetVelocity = FRONT_TRENCH_VEL;
        break;
      case BACK_TRENCH :
        _targetVelocity = BACK_TRENCH_VEL;
        break;
      default : //We should never get here
        _targetVelocity = 0.0;
        break;
    }

    shooter.setSpeed(_targetVelocity);
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
    end();
  }
}