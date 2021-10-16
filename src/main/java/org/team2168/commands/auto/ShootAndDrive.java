// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ShootAndDrive extends CommandGroup {
  /** Add your docs here. */
  public ShootAndDrive() {
    addParallel(new MoveToWhiteLine());// open up shooter hood and set shooter wheel speed to white line position
    addSequential(new Sleep(), 5.0); // wait five seconds
    addSequential(new FireBallsAuto(3)); // run the fire balls auto (shoots 3 balls)
    addSequential(new PathCommand("shoot_and_drive_path", 0)); // drive 5 feet forward
  }
}
