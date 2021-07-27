/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoNavBounce extends CommandGroup {
  /**
   * AutoNav path 3: Bounce
   */
  public AutoNavBounce() {
    addSequential(new PathCommand("bounce", 0));
  }
}
