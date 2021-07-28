// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.auto;

import org.team2168.commands.auto.robotFunctions.FireBalls;
import org.team2168.commands.hood_adjust.MoveToWhiteLine;
import org.team2168.commands.indexer.DriveIndexerWithConstant;
import org.team2168.commands.intakeMotor.IntakeBallStart;
import org.team2168.commands.intakeMotor.IntakeBallStop;
import org.team2168.commands.limelight.DriveWithLimelight;
import org.team2168.commands.shooter.WaitForShooterAtSpeed;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class YeehawAuto extends CommandGroup {
  /** A recreation of an auto done by Team 9889 at the Texas Cup*/
  public YeehawAuto() {
    addSequential(new PathCommand("to_trench", 0.0));
    addSequential(new IntakeBallStart(), 1.0);
    addSequential(new IntakeBallStop());
    addSequential(new PathCommand("trench_to_shoot", 0.0));
    addSequential(new DriveWithLimelight());
    addSequential(new MoveToWhiteLine());
    addSequential(new FireBalls());
  }
}
