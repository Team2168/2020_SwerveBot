// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.team2168.commands.drivetrain.ZeroGyro;
import org.team2168.subsystems.Drivetrain;

public class GoalWallShotReverse extends CommandGroup {
  Drivetrain drive = Drivetrain.getInstance();
  public GoalWallShotReverse() {
    addSequential(new ZeroGyro());
    addSequential(new PathCommand("goal_wall_shot_reverse", 0.0));
  }
}