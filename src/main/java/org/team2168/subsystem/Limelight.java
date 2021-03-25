// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team2168.subsystem;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Subsystem;

/** Add your docs here. */
public class Limelight extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  /* NetworkTables Entries */
  private NetworkTable citrusTable;
  private NetworkTableEntry tv;  // Has valid targets; [0, 1]; integer
  private NetworkTableEntry tx;  // Horizontal offset from the target; [-27, 27]; degrees
  private NetworkTableEntry ty;  // Vertical offset from the target; [-20.5, 20.5]; degrees
  private NetworkTableEntry ta;  // Target Area; [0, 100]; percent
  

  private static Limelight instance = null;

  private Limelight() {
    citrusTable = NetworkTableInstance.getDefault().getTable("limelight");
    citrusTable.getEntry("ledMode").setNumber(0);
    citrusTable.getEntry("camMode").setNumber(0);
    citrusTable.getEntry("pipeline").setNumber(0);
    tv = citrusTable.getEntry("tv");
    tx = citrusTable.getEntry("tx");
    ty = citrusTable.getEntry("ty");
    ta = citrusTable.getEntry("ta");


  }

  public boolean hasTarget() {
    return ((tv.getNumber(0).equals(1)) ? true : false);
  }

  public double getXOffset() {
    return tx.getDouble(0.0);
  }

  public double getYOffset() {
    return ty.getDouble(0.0);
  }

  public double getTargetArea() {
    return ta.getDouble(0.0);
  }

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
