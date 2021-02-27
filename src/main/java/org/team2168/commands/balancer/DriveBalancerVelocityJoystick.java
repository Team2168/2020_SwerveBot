/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.balancer;

import org.team2168.OI;
import org.team2168.subsystems.Balancer;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveBalancerVelocityJoystick extends Command {

  private static Balancer balancer;
  private static OI oi;
  private double _setPoint;
  private boolean _readPIDFromDashboard = false;

  public DriveBalancerVelocityJoystick() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    balancer = Balancer.getInstance();
    requires(balancer);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    oi = OI.getInstance();

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(_readPIDFromDashboard) {
      balancer.updatePIDValues();
    }
    if(Math.abs(oi.getBalancerJoystickValue()) > 0.05) {
      _setPoint = oi.getBalancerJoystickValue() * balancer.getMaxVelocity();
    }
    else {
      _setPoint = 0.0;
    }
    // System.out.println(_setPoint + " " + oi.getBalancerJoystickValue() + " " + balancer.getMaxVelocity());
    balancer.setVelocitySetPoint(_setPoint);

    SmartDashboard.putNumber("SetPoint", _setPoint);
    SmartDashboard.putNumber("Position", balancer.getPosition());
    SmartDashboard.putNumber("Velocity", balancer.getVelocity());
    SmartDashboard.putNumber("Velocity Error", balancer.getVelocityError());
    SmartDashboard.putNumber("Output", balancer.getMotorOutput());

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