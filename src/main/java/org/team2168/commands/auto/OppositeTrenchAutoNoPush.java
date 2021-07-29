// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.auto;

import org.team2168.RobotMap;
import org.team2168.commands.auto.robotFunctions.FireBallsAuto;
import org.team2168.commands.hood_adjust.MoveToFrontTrench;
import org.team2168.commands.hood_adjust.MoveToWhiteLine;
import org.team2168.commands.hopper.DriveHopperWithConstant;
import org.team2168.commands.intakeMotor.DriveIntakeWithConstant;
import org.team2168.commands.intakeMotor.IntakeBallStart;
import org.team2168.commands.intakeMotor.IntakeBallStop;
import org.team2168.commands.intakePivot.ExtendIntakePneumatic;
import org.team2168.commands.limelight.DriveWithLimelight;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class OppositeTrenchAutoNoPush extends CommandGroup {
  /** A recreation of an auto done by Team 9889 at the Texas Cup*/
  public OppositeTrenchAutoNoPush() {
    // start shooter
    addParallel(new MoveToFrontTrench());

    // start intake and drive to trench
    addParallel(new DriveIntakeWithConstant(RobotMap.INTAKE_SPEED));
    addParallel(new ExtendIntakePneumatic());
    addSequential(new PathCommand("to_opposite_trench", 0.0));

    // drive to shooting location
    addSequential(new PathCommand("opposite_trench_to_shoot", 20.0));
    addSequential(new DriveWithLimelight(), 5.0);

    // stop intake and fire balls
    //addParallel(new IntakeBallStop());
    addSequential(new FireBallsAuto(5), 4.0);
    addSequential(new DriveHopperWithConstant(0.0), 0.1);
    addSequential(new DriveIntakeWithConstant(0.0), 0.0);
  }
}
