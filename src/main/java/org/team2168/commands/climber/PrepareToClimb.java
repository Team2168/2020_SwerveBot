/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.climber;

import org.team2168.commands.hood_adjust.MoveToWallNoShoot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PrepareToClimb extends CommandGroup {
  /**
   * Add your docs here.
   */
  public PrepareToClimb() {
    addParallel(new MoveToWallNoShoot());
    addSequential(new DisengageRatchet());
    addSequential(new DriveClimberXPosition(46.5, 0.1));
    addSequential(new EngageRatchet());
  }
}
