/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class GalacticSearchBBlue extends CommandGroup {
  /**
   * The Galactic Search B: Blue path
   */
  public GalacticSearchBBlue() {
    addSequential(new PathCommand("gs-b-blue", 0));
  }
}
