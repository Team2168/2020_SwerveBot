// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.commands.auto;

import org.team2168.RobotMap;
import org.team2168.commands.auto.robotFunctions.FireBallsAuto;
import org.team2168.commands.hood_adjust.MoveToFrontTrench;
import org.team2168.commands.hopper.DriveHopperWithConstant;
import org.team2168.commands.indexer.DriveIndexerWithConstant;
import org.team2168.commands.intakeMotor.DriveIntakeWithConstant;
import org.team2168.commands.intakePivot.ExtendIntakePneumatic;
import org.team2168.commands.intakePivot.RetractIntakePneumatic;
import org.team2168.commands.limelight.DriveWithLimelight;
import org.team2168.commands.shooter.WaitForShooterAtSpeed;
import org.team2168.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class WhiteLineToRendezvousAuto extends CommandGroup {
  /** Add your docs here. */
  Drivetrain drive = Drivetrain.getInstance();

  public WhiteLineToRendezvousAuto() {
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

    // start shooter
    addParallel(new ExtendIntakePneumatic()); // move off stow
    addParallel(new MoveToFrontTrench());

    // start intake and drive to 5 feet from white line
    addSequential(new PathCommand("white_line_to_rendezvous1", 0));
    addSequential(new RetractIntakePneumatic(), 0.1);

    // stop intake and fire balls
    addSequential(new WaitForShooterAtSpeed());
    addParallel(new DriveHopperWithConstant(RobotMap.HOPPER_SPEED));
    addSequential(new FireBallsAuto(3), 2);

    addParallel(new ExtendIntakeWithDelay(1));
    addSequential(new PathCommand("white_line_to_rendezvous2", 80));

    //drive into rendezvous to pick up balls
    addSequential(new PathCommand("white_line_to_rendezvous3", 90.0));

    // back up, turn, and shoot
    addSequential(new PathCommand("white_line_to_rendezvous4", 0.0));
    addSequential(new DriveWithLimelight(), 1.0);
    addParallel(new DriveHopperWithConstant(RobotMap.HOPPER_SPEED));
    addParallel(new DriveIntakeWithConstant(0.3));
    addParallel(new RetractIntakeWithDelay(1.0));
    addSequential(new FireBallsAuto(5), 5.0);
  }  
}

/**
 * Extend intake and run motor after a specified fixed delay (sec)
 */
class ExtendIntakeWithDelay extends CommandGroup {
  public ExtendIntakeWithDelay(double delay) {
    addSequential(new Sleep(), delay);
    addParallel(new DriveIntakeWithConstant(RobotMap.INTAKE_SPEED));
    addSequential(new ExtendIntakePneumatic());
  }
}

/**
 * Retract intake after a specified fixed delay (sec)
 */
class RetractIntakeWithDelay extends CommandGroup {
  public RetractIntakeWithDelay(double delay) {
    addSequential(new Sleep(), delay);
    addSequential(new RetractIntakePneumatic());
  }
}
