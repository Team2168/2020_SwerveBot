/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class GalacticSearchABlue extends CommandGroup {
  /**
   * The Galactic Search A: Blue path
   */
  public GalacticSearchABlue() {
    addSequential(new PathCommand("gs-a-blue", 0));
  }
}
