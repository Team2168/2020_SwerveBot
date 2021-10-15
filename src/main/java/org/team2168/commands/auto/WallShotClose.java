// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.auto;

import org.team2168.commands.auto.robotFunctions.FireBalls;
import org.team2168.commands.auto.robotFunctions.FireBallsAuto;
import org.team2168.commands.hood_adjust.MoveToFiringLocation;
import org.team2168.commands.hood_adjust.MoveToWall;
import org.team2168.commands.limelight.DriveWithLimelight;
import org.team2168.subsystems.Shooter.FiringLocation;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class WallShotClose extends CommandGroup {
  /** Add your docs here. */
  public WallShotClose() {
    // Add Commands here:
    // e.g. addSequential(new Command1());
    // addSequential(new Command2());
    // these will run in order.

    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
    addParallel(new MoveToWall());
    addParallel(new MoveToFiringLocation(FiringLocation.WALL));

    addSequential(new PathCommand("wallshot_close", 0d));

    addSequential(new DriveWithLimelight(), 5);
    addSequential(new Sleep(), 0.5);
    addSequential(new FireBallsAuto(3));
  }
}
