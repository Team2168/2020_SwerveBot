// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.subsystems;

import org.team2168.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/** Add your docs here. */
public class PBotJumper extends Subsystem {

  private DigitalInput jumper;
  private static PBotJumper instance = null;

  private PBotJumper() {
    jumper = new DigitalInput(RobotMap.PRACTICE_BOT_JUMPER);
  }

  public boolean get() {
    return jumper.get();
  }

  public static PBotJumper getInstance() {
    if (instance == null)
      instance = new PBotJumper();
    
    return instance;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
