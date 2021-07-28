/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.commands.hood_adjust;

import org.team2168.Robot;
import org.team2168.commands.auto.Sleep;
import org.team2168.commands.shooter.DriveToXSpeed;
import org.team2168.subsystems.HoodAdjust;
import org.team2168.subsystems.HoodAdjust.HoodPosition;
import org.team2168.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class MoveToWhiteLine extends CommandGroup {
HoodAdjust pos = HoodAdjust.getInstance();
  public MoveToWhiteLine() {
  //By not using a break in the switch statement, the cases will
  //follow through, allowing for less lines of code.
    // switch(pos.shooterPosition){
    //   // This code will carry the hood from the front trench position 
    //   // to the back trench position.
    //   case FRONT_TRENCH :
    //     addSequential(new RetractShooterHood());
    //     addSequential(new Sleep(), 0.5);
    //     addSequential(new RetractShooterHardstop());
    //     addSequential(new Sleep(), 0.5);
    //   // This code will bring the hood from the back trench to the wall
    //   // position.
    //   case BACK_TRENCH :
    //     addSequential(new ExtendShooterHood());
    //     addSequential(new Sleep(), 0.5);
    //   // This final part will carry the hood from the wall position to the 
    //   // white line position.
    //   case WALL :
    //     addSequential(new ExtendShooterHardstop());
    //     addSequential(new Sleep(), 0.5);
    //     addSequential(new RetractShooterHood());
    //     addSequential(new Sleep(), 0.1);
    //   // If the hood starts in the white line position, 
    //   // no change is needed.
    //   case WHITE_LINE :
    //     break;
    // }

    //updated---allows for any possible position, allows retract pancake under load;
    addParallel(new DriveToXSpeed(Shooter.FiringLocation.WHITE_LINE));
    
    addSequential(new RetractShooterHardstop());
    addSequential(new Sleep(), 0.1);
    addSequential(new ExtendShooterHood());
    addSequential(new Sleep(), 0.5); //TODO tune later
    addSequential(new ExtendShooterHardstop());
    addSequential(new Sleep(), 0.1);
    addSequential(new RetractShooterHood());
    addSequential(new Sleep(), 0.1);
  }
}