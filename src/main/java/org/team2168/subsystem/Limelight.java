// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.subsystem;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Subsystem;

/** A helper class for getting values from a limelight.
 * This doesn't *do* much, but it rather acts as a convenient
 * way to get/set things in NetworkTables.
 */
public class Limelight extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  public static final int DEFAULT_PIPELINE = 0;
  public static final int DEFAULT_CAMMODE = 0;
  public static final int DEFAULT_LEDMODE = 0;

  /* NetworkTables Entries */
  private NetworkTable citrusTable;
  private NetworkTableEntry tv;  // Has valid targets; [0, 1]; integer
  private NetworkTableEntry tx;  // Horizontal offset from the target; [-27, 27]; degrees
  private NetworkTableEntry ty;  // Vertical offset from the target; [-20.5, 20.5]; degrees
  private NetworkTableEntry ta;  // Target Area; [0, 100]; percent
  

  private static Limelight instance = null;

  private Limelight() {
    citrusTable = NetworkTableInstance.getDefault().getTable("limelight");
    setLEDMode(DEFAULT_LEDMODE);
    setCamMode(DEFAULT_CAMMODE);
    setPipeline(DEFAULT_PIPELINE);
    tv = citrusTable.getEntry("tv");
    tx = citrusTable.getEntry("tx");
    ty = citrusTable.getEntry("ty");
    ta = citrusTable.getEntry("ta");


  }

  /**
   * Sets the LED state.  The values are as follows:
   * 0: use the LED Mode set in the current pipeline
   * 1: force off
   * 2: force blink
   * 3: force on
   * @param ledMode A value between 0 and 3
   */
  public void setLEDMode(int ledMode) {
    citrusTable.getEntry("ledMode").setNumber(ledMode);
  }

  /**
   * Sets the operation mode.  The values are as follows:
   * 0: Vision processor
   * 1: Driver Camera (Disables vision processing and related tweaks)
   * @param camMode A value between 0 and 1
   */
  public void setCamMode(int camMode) {
    citrusTable.getEntry("camMode").setNumber(camMode);
  }

  /**
   * Sets the current pipeline.  These are defined on the limelight
   * itself.
   * 
   * @param pipelineIndex A value between 0 and 9
   */
  public void setPipeline(int pipelineIndex) {
    citrusTable.getEntry("pipeline").setNumber(pipelineIndex);
  }

  /**
   * Returns a boolean based on whether the limelight has acquired a target
   *  
   * @return A boolean based on whether the limelight has a target
   */
  public boolean hasTarget() {
    return ((tv.getNumber(0).equals(1)) ? true : false);
  }

  /**
   * Gets the x offset from the target in degrees
   * @return A value between -27 and 27
   */
  public double getXOffset() {
    return tx.getDouble(0.0);
  }

  /**
   * Gets the y offset from the target in degrees
   * @return A value between -20.5 and 20.5
   */
  public double getYOffset() {
    return ty.getDouble(0.0);
  }

  /**
   * Gets the target area in percent of the image
   * @return A value between 0 and 100
   */
  public double getTargetArea() {
    return ta.getDouble(0.0);
  }

  /**
   * Gets an instance of a limelight object
   * 
   * @return Limelight object
   */
  public static Limelight getInstance() {
    if (instance == null) 
      instance = new Limelight();
    return instance;
  }


  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
