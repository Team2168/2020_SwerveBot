/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.auto.robotFunctions;

import org.team2168.RobotMap;
import org.team2168.commands.hopper.DriveHopperWithConstant;
import org.team2168.commands.indexer.DriveIndexerWithConstant;
import org.team2168.commands.indexer.DriveIndexerWithConstantNoStop;
import org.team2168.commands.intakeMotor.DriveIntakeWithConstant;
import org.team2168.commands.shooter.WaitForShooterAtSpeed;
import org.team2168.subsystems.Shooter;
import org.team2168.subsystems.Shooter.FiringLocation;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class FireBalls extends CommandGroup {
  /**
   * Add your docs here.
   */
  public FireBalls() {
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
    FiringLocation fl = Shooter.getInstance().getFiringLocation();

    addSequential(new WaitForShooterAtSpeed());
    addSequential(new DriveIndexerWithConstantNoStop(RobotMap.INDEXER_SPEED), 0.5);
    if (fl == FiringLocation.WALL) {
      addParallel(new DriveIndexerWithConstant(0.75));
      addParallel(new DriveHopperWithConstant(0.6));
    } else {
      addParallel(new DriveIndexerWithConstant(RobotMap.INDEXER_SPEED));
      addParallel(new DriveHopperWithConstant(RobotMap.HOPPER_SPEED));
    }
    addParallel(new DriveIntakeWithConstant(RobotMap.INTAKE_SPEED_SLOW));
  }
}

