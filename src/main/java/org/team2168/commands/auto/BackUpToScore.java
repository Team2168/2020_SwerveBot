// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class BackUpToScore extends CommandGroup {
  /** Add your docs here. */
  public BackUpToScore() {
    // Path moves backwards from shooter
    addSequential(PathCommand("back_up_to_score", 0));
  }
}
