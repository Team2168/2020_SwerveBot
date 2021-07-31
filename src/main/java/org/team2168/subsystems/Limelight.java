/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168.subsystems;

import org.team2168.Robot;
import org.team2168.PID.sensors.LimelightSensor;
import org.team2168.commands.limelight.UpdatePipeline;
import org.team2168.subsystems.Shooter.FiringLocation;

import edu.wpi.first.wpilibj.command.Subsystem;


/**
 * Add your docs here.
 */
public class Limelight extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private static LimelightSensor limelight;
  private static Limelight _instance = null;

  /* Public so they can be used elsewhere; idk if this is good design but I need it for right now because
  not every ll pipeline is correlated to a shooter speed anymore and I don't want to overhaul the whole subsystem :/ */
  public static final int PIPELINE_FORWARD_BLUE = 0;
  public static final int PIPELINE_FORWARD_RED = 2;
  public static final int PIPELINE_BACK_TRENCH_BLUE = 1;
  public static final int PIPELINE_BACK_TRENCH_RED = 3;
  public static final int PIPELINE_DRIVER_VIEW = 4;
  public static final int PIPELINE_DRIVE_WITH_LIMELIGHT = 9;



  private boolean isLimelightEnabled;

  private Limelight() {
    //set up limelight
    limelight = new LimelightSensor();
    // limelight.setCamMode(0);
    limelight.enableVisionProcessing(true);
    limelight.setLedMode(1);
    limelight.setPipeline(PIPELINE_DRIVE_WITH_LIMELIGHT);
    isLimelightEnabled = false;
  }

  /**
   * @return an instance of the Intake Subsystem
   */
  public static Limelight getInstance() {
    if (_instance == null) {
      _instance = new Limelight();
    }
    return _instance;
  }

  /**
   * Gets x position of limelight
   * @return x position, in degrees
   */
  public double getPosition() {
    return limelight.getPos();
  }

  public void enableLimelight() {
    // limelight.setCamMode(0);
    limelight.enableVisionProcessing(true);
    limelight.setLedMode(0); // set to current pipeline setting
    setPipeline(PIPELINE_DRIVE_WITH_LIMELIGHT);
    isLimelightEnabled = true;
  }

  public void setPipeline(int pipeline) {
    limelight.setPipeline(pipeline);
  }

  public int getPipeline() {
    return limelight.getPipeline();
  }

  public void pauseLimelight()
  {
    // limelight.setCamMode(1);
    limelight.enableVisionProcessing(false);
    limelight.setLedMode(1); // force off
    isLimelightEnabled = false;

  }

  public boolean isLimelightEnabled() {
    return isLimelightEnabled;
  }


  /**
  * Sets the LED mode
  * @param ledNumber is an int from 0 to 3
  * <ol>
  *   <li>use the LED Mode set in the current pipeline</li>
  *   <li>force off</li>
  *   <li>force blink</li>
  *   <li>force on</li>
  * </ol>
  */
  public void setLedMode(int ledNumber) {
    limelight.setLedMode(ledNumber);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    // setDefaultCommand(new UpdatePipeline());
  }
}
