// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.auto;

import org.team2168.RobotMap;
import org.team2168.commands.auto.robotFunctions.FinishFiring;
import org.team2168.commands.auto.robotFunctions.FireBalls;
import org.team2168.commands.auto.robotFunctions.FireBallsAuto;
import org.team2168.commands.drivetrain.DriveWithFixedAzimuth;
import org.team2168.commands.hood_adjust.MoveToFiringLocation;
import org.team2168.commands.hood_adjust.MoveToFrontTrench;
import org.team2168.commands.intakeMotor.DriveIntakeWithConstant;
import org.team2168.commands.intakePivot.ExtendIntakePneumatic;
import org.team2168.commands.intakePivot.RetractIntakePneumatic;
import org.team2168.commands.limelight.DriveWithLimelight;
import org.team2168.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class OurTrench extends CommandGroup {
  private static final double INTAKE_RETRACT_SPEED = 0.3;

  /** Add your docs here. */
  public OurTrench() {
    addParallel(new MoveToFrontTrench()); // extend hood
    addParallel(new ExtendIntakePneumatic());
    addParallel(new DriveIntakeWithConstant(RobotMap.INTAKE_SPEED));

    addSequential(new PathCommand("our_trench.1", -10));  // drive to front of trench

    addSequential(new DriveWithLimelight());
    addParallel(new DriveIntakeWithConstant(INTAKE_RETRACT_SPEED));
    addParallel(new RetractIntakePneumatic());
    addSequential(new FireBallsAuto(5), 3);

    addSequential(new DriveWithFixedAzimuth(0.0));

    addSequential(new MoveToFiringLocation(Shooter.FiringLocation.WALL)); // needs to be sequential so the robot doesn't ram into the color wheel

    // These could be one path, but this way you don't have to deal with weird turnarounds
    addParallel(new DriveIntakeWithConstant(RobotMap.INTAKE_SPEED));
    addParallel(new ExtendIntakePneumatic());
    addSequential(new PathCommand("our_trench.2", 0));
    addSequential(new PathCommand("our_trench.3", 0));
    
    addParallel(new RetractIntakePneumatic());
    addParallel(new DriveIntakeWithConstant(INTAKE_RETRACT_SPEED));
    addParallel(new MoveToFiringLocation(Shooter.FiringLocation.FRONT_TRENCH));
    addSequential(new DriveWithFixedAzimuth(-10));
    addSequential(new DriveWithLimelight());

    addSequential(new Sleep(), 0.5);
    // addSequential(new FireBallsAuto(5));
    addSequential(new FireBalls());  // number fired doesn't particularly matter when this is the last command in sequence
    
    // addSequential(new MoveToFrontTrench());
    // addSequential(new DriveWithLimelight(), 5.0);
    // addSequential(new FireBallsAuto(5), 4.0);

    
  
  }

  @Override
  protected void end() {
    addSequential(new FinishFiring());
    super.end();
  }

  @Override
  protected void interrupted() {
    end();
    super.interrupted();
  }
}
