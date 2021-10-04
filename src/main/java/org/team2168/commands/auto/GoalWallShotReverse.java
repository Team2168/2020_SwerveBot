// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class GoalWallShotReverse extends CommandGroup {
  /** Add your docs here. */
  public GoalWallShotReverse() {
    // Path moves backwards from scoring wall
    addSequential(new PathCommand("goal_wall_shot_reverse", 0));
    addSequential(new DriveWithLimelight());
  }
}
