/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoNavSlalom extends CommandGroup {
  /**
   * AutoNav path 2: Slalom
   */
  public AutoNavSlalom() {
    addSequential(new PathCommand("slalom1", 0));
    addSequential(new PathCommand("slalom2", 0));
    addSequential(new PathCommand("slalom3", 0));
  }
}
