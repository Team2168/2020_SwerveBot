/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.auto;

import org.team2168.RobotMap;
import org.team2168.commands.auto.robotFunctions.FireBalls;
import org.team2168.commands.auto.robotFunctions.FireBallsAuto;
import org.team2168.commands.drivetrain.DriveWithFixedAzimuth;
import org.team2168.commands.flashlight.RunFlashlight;
import org.team2168.commands.hood_adjust.MoveToFrontTrench;
import org.team2168.commands.hood_adjust.MoveToWhiteLine;
import org.team2168.commands.hopper.DriveHopperWithConstant;
import org.team2168.commands.indexer.DriveUntilBall;
import org.team2168.commands.indexer.DriveUntilNoBall;
import org.team2168.commands.intakeMotor.DriveIntakeWithConstant;
import org.team2168.commands.intakeMotor.IntakeBallStart;
import org.team2168.commands.intakeMotor.IntakeBallStop;
import org.team2168.commands.intakePivot.ExtendIntakePneumatic;
import org.team2168.commands.intakePivot.RetractIntakePneumatic;
import org.team2168.commands.limelight.DriveWithLimelight;
import org.team2168.commands.shooter.WaitForShooterAtSpeed;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class OppositeTrenchAutoExtendedNoPush extends CommandGroup {
  /**
   * Add your docs here.
   */
  public OppositeTrenchAutoExtendedNoPush() {
    // start shooter
    addParallel(new MoveToFrontTrench());

    // start intake and drive to trench
    addParallel(new RunFlashlight(1.0));
    addParallel(new DriveIntakeWithConstant(RobotMap.INTAKE_SPEED));
    addParallel(new ExtendIntakePneumatic());
    addSequential(new PathCommand("to_opposite_trench", 0.0));
    addParallel(new DriveIntakeWithConstant(0.3));
    addSequential(new RetractIntakePneumatic());

    // drive to shooting location
    addSequential(new PathCommand("opposite_trench_to_shoot", 20.0));
    addSequential(new DriveWithLimelight(), 5.0);

    // stop intake and fire balls
    addSequential(new FireFiveBalls(), 4.0);

    // drive around into rendezvous, turn and extend intake, and drive to pick up balls
    addSequential(new PathCommand("opposite_trench_to_rendezvous", 0.0));
    addSequential(new DriveWithFixedAzimuth(135.0, false));
    addParallel(new DriveIntakeWithConstant(RobotMap.INTAKE_SPEED));
    addSequential(new ExtendIntakePneumatic());
    addSequential(new PathCommand("opposite_trench_to_rendezvous2", 0.0));

    // drive back, turn, and shoot
    addSequential(new PathCommand("opposite_trench_to_rendezvous_3", 0.0));
    addSequential(new DriveWithFixedAzimuth(0.0, false));
    addSequential(new DriveWithLimelight(), 5.0);
    addSequential(new FireBallsAuto(5), 5.0);
    
    addSequential(new DriveHopperWithConstant(0.0), 0.1);
    addSequential(new DriveIntakeWithConstant(0.0), 0.0);
  }
}

class FireFiveBalls extends CommandGroup {
  public FireFiveBalls() {
    addSequential(new WaitForShooterAtSpeed());
    addParallel(new DriveHopperWithConstant(RobotMap.HOPPER_SPEED));
    for (int i = 0; i<5; i++) {
      addSequential(new DriveUntilBall(RobotMap.INDEXER_SPEED));
      addSequential(new DriveUntilNoBall(RobotMap.INDEXER_SPEED));
    }
    addParallel(new DriveHopperWithConstant(0.0), 0.0);
    addParallel(new DriveIntakeWithConstant(0.0), 0.0);
  }
}