/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.subsystems;

import org.team2168.RobotMap;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class Flashlight extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private static Victor _light;
  private static Flashlight _instance;

  private Flashlight() {
    _light = new Victor(RobotMap.PWM_LIGHTS);

  }

  public static Flashlight getInstance() {
    if (_instance == null)
      _instance = new Flashlight();

    return _instance;  
  }

  	/**
	 * Set light speed where postive turns them on and negative turns them off
	 * @param set
	 */
	public void setFlashlight(double set) {
		_light.set(set);
	}


  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
