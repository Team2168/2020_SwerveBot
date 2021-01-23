/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168;

import org.team2168.commands.drivetrain.DoNothing;
import org.team2168.commands.drivetrain.SwerveDriveTestsPathCommandGroup;
import org.team2168.subsystem.Drivetrain;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  static Command autonomousCommand;
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  private static Drivetrain dt;
  private static OI oi;

  static boolean autoMode;

  // private static DriveWithJoystick drivewithjoystick;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    autoSelectInit();
    SmartDashboard.putData("Auto Chooser", autoChooser);

    SmartDashboard.putString("Control Mode", "Joystick");

    SmartDashboard.putNumber("Drive Forward", 0.0);
    SmartDashboard.putNumber("Drive Strafe", 0.0);
    SmartDashboard.putNumber("Drive Azimuth", 0.0);

    dt = Drivetrain.getInstance();
    oi = OI.getInstance();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Gyro heading", dt.getGyro().getAngle());
  }

  /** Adds autos to the selector
   */
  public void autoSelectInit() {
    autoChooser.setDefaultOption("Default Auto", new DoNothing());
    autoChooser.addOption("Drive Straight", new SwerveDriveTestsPathCommandGroup());
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    dt.getGyro().reset();
    autoMode = true;
    autonomousCommand = (Command) autoChooser.getSelected();

    if (autonomousCommand != null)
      autonomousCommand.start();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    autoMode = true;
    Scheduler.getInstance().run();
  }

  /**
   * This function is called once when teleop is enabled.
   */
  @Override
  public void teleopInit() {
    autoMode = false;
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    autoMode = false;
    Scheduler.getInstance().run();
  }

  /**
   * This function is called once when the robot is disabled.
   */
  @Override
  public void disabledInit() {
    autoMode = false;
  }

  /**
   * This function is called periodically when disabled.
   */
  @Override
  public void disabledPeriodic() {
    autoMode = false;
    Scheduler.getInstance().run();

    SmartDashboard.putData("Auto Chooser", autoChooser);
    autonomousCommand = (Command) autoChooser.getSelected();

    // TODO: put this on a test joystick
    if (oi.driverJoystick.isPressedButtonStart()) {
      dt.saveAzimuthPositions();
    }
  }

  /**
   * This function is called once when test mode is enabled.
   */
  @Override
  public void testInit() {
    autoMode = false;
  }

  public static boolean isAutoMode() {
    return autoMode;
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    autoMode = false;
  }
}
