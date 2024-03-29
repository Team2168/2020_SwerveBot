/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team2168;

import org.team2168.commands.auto.*;
import org.team2168.commands.drivetrain.DriveWithJoystick;
import org.team2168.commands.hood_adjust.MoveToFiringLocation;
import org.team2168.subsystems.Climber;
import org.team2168.subsystems.Drivetrain;
import org.team2168.subsystems.HoodAdjust;
import org.team2168.subsystems.Hopper;
import org.team2168.subsystems.Indexer;
import org.team2168.subsystems.IntakeMotor;
import org.team2168.subsystems.IntakePivot;
import org.team2168.subsystems.Limelight;
import org.team2168.subsystems.Shooter;
import org.team2168.thirdcoast.swerve.SwerveDrive.DriveMode;
import org.team2168.utils.consoleprinter.ConsolePrinter;
import org.team2168.thirdcoast.swerve.Wheel;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.Relay;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  static Command autonomousCommand;
  private final SendableChooser<Command> autoChooser = new SendableChooser<Command>();
  public static int pushRobot;
  public static SendableChooser<Number> pushRobotChooser;
  private static DigitalInput practiceBot = new DigitalInput(RobotMap.PRACTICE_BOT_JUMPER);

  // Subsystems
  private static Climber climber;
  private static IntakeMotor intakeMotor;
  private static IntakePivot intakePivot;
  private static Indexer indexer;
  private static Shooter shooter;
  private static HoodAdjust hoodAdjust;
  private static Drivetrain dt;
  private static Limelight limelight;
  private static Compressor compressor;
  private static Hopper hopper;
  private static Relay toggleLEDs;

  private static OI oi;

  static boolean autoMode;
  public static final boolean ENABLE_BUTTON_BOX = true;
  private static boolean lastCallHoodButtonA = false;
  private MoveToFiringLocation moveHood;

  // private static DriveWithJoystick drivewithjoystick;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    ConsolePrinter.init();
    ConsolePrinter.setRate(20);

    SmartDashboard.putData("Auto Chooser", autoChooser);
    SmartDashboard.putString("Control Mode", "Joystick");

    SmartDashboard.putNumber("Drive Forward", 0.0);
    SmartDashboard.putNumber("Drive Strafe", 0.0);
    SmartDashboard.putNumber("Drive Azimuth", 0.0);

    dt = Drivetrain.getInstance();
    intakeMotor = IntakeMotor.getInstance();
    intakePivot = IntakePivot.getInstance();
    indexer = Indexer.getInstance();
    shooter = Shooter.getInstance();
    hoodAdjust = HoodAdjust.getInstance();
    hopper = Hopper.getInstance();
    limelight = Limelight.getInstance();
    compressor = new Compressor();
    toggleLEDs = new Relay(RobotMap.LED_RELAY_CHANNEL);

    oi = OI.getInstance();

    pushRobotSelectInit();
    autoSelectInit();

    // I am making REALLY REALLY sure the limelight isn't on
    limelight.pauseLimelight();

    // ConsolePrinter.putBoolean("isPracticeBot", ()->{return isPracticeBot();}, true, false);
    // // SmartDashboard.putData("Push Robot Chooser", pushRobotChooser);
    // ConsolePrinter.putString("AutoName", () -> {return Robot.getAutoName();}, true, false);

    // ConsolePrinter.putNumber("Actual yaw 0", () -> {return Wheel.ticksToDegreesAzimuth(dt.getWheels()[0].getInternalEncoderPos());}, false, true);
    // ConsolePrinter.putNumber("Actual speed 0", () -> {return Wheel.TicksPer100msToFPSDW(dt.getWheels()[0].getDWSpeed());}, false, true);
    // ConsolePrinter.putNumber("Actual yaw 1", () -> {return Wheel.ticksToDegreesAzimuth(dt.getWheels()[1].getInternalEncoderPos());}, false, true);
    // ConsolePrinter.putNumber("Actual speed 1", () -> {return Wheel.TicksPer100msToFPSDW(dt.getWheels()[1].getDWSpeed());}, false, true);
    // ConsolePrinter.putNumber("Actual yaw 2", () -> {return Wheel.ticksToDegreesAzimuth(dt.getWheels()[2].getInternalEncoderPos());}, false, true);
    // ConsolePrinter.putNumber("Actual speed 2", () -> {return Wheel.TicksPer100msToFPSDW(dt.getWheels()[2].getDWSpeed());}, false, true);
    // ConsolePrinter.putNumber("Actual yaw 3", () -> {return Wheel.ticksToDegreesAzimuth(dt.getWheels()[3].getInternalEncoderPos());}, false, true);
    // ConsolePrinter.putNumber("Actual speed 3", () -> {return Wheel.TicksPer100msToFPSDW(dt.getWheels()[3].getDWSpeed());}, false, true);
    // ConsolePrinter.putNumber("Gyro heading", () -> {return dt.getHeading();}, false, true);

    //ConsolePrinter.startThread();
    // drivetrain.setDefaultBrakeMode();
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
    SmartDashboard.putNumber("Gyro heading", dt.getHeading());
    for (int i = 0; i < dt.getWheels().length; i++) {
      SmartDashboard.putNumber("Abs position module " + i, dt.getWheels()[i].getAzimuthPosition());
      SmartDashboard.putNumber("Probably incorrect module heading in degrees " + i, Wheel.ticksToDegreesAzimuth(dt.getWheels()[i].getAzimuthPosition()));
      SmartDashboard.putNumber("Module heading in degrees " + i, Wheel.ticksToDegreesAzimuth(dt.getWheels()[i].getAzimuthPosition()));
      SmartDashboard.putNumber("Speed of wheel (FPS) " + i, Wheel.TicksPer100msToFPSDW(dt.getWheels()[i].getDWSpeed()));
      SmartDashboard.putData(Scheduler.getInstance());
    }

    // zeroing on test joystick
    if (oi.testJoystick.isPressedButtonStart()) {
      dt.saveAzimuthPositions();
    }
    if (oi.testJoystick.isPressedButtonBack()) {
      dt.zeroGyro();
    }
  }

  /** Adds autos to the selector
   */
  public void autoSelectInit() {
    autoChooser.setDefaultOption("Do Nothing", new DoNothing());
    autoChooser.addOption("Opposite Trench (No Push)", new OppositeTrenchAutoNoPush());
    // autoChooser.addOption("Opposite Trench Extended (No Push)", new OppositeTrenchAutoExtendedNoPush());
    autoChooser.addOption("Our Trench", new OurTrench());
    autoChooser.addOption("White Line to Rendezvous", new WhiteLineToRendezvousAuto());
    autoChooser.addOption("Shoot Straight & Drive Fwd", new ShootAndDrive());
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
    limelight.enableLimelight();
    dt.zeroGyro();
    autoMode = true;
    dt.setDriveMode(DriveMode.AZIMUTH);
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
    setShooterAtSpeedLEDs();
    Scheduler.getInstance().run();
  }

  /**
   * This function is called once when teleop is enabled.
   */
  @Override
  public void teleopInit() {
    limelight.pauseLimelight();
    autoMode = false;
    dt.setDriveMode(DriveMode.TELEOP);
    Scheduler.getInstance().removeAll();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    autoMode = false;
    final boolean buttonBox2_buttonA = oi.buttonBox2.isPressedButtonA();

    if(!oi.driverJoystick.isPressedButtonLeftBumper()
        && (buttonBox2_buttonA && !lastCallHoodButtonA)) {
      //The driver isn't going under the trench
      //the operator pressed hood raise button
      //raise the hood to the firing position
      moveHood = new MoveToFiringLocation(shooter.getFiringLocation());
      moveHood.start();
    } else if (!buttonBox2_buttonA && lastCallHoodButtonA) {
      // or the operator isn't pressing hood raise button
      //lower the hood
      moveHood = new MoveToFiringLocation(Shooter.FiringLocation.WALL);
      moveHood.start();
    }

    lastCallHoodButtonA = buttonBox2_buttonA;

    setShooterAtSpeedLEDs();
    Scheduler.getInstance().run();
  }

  /**
   * This function is called once when the robot is disabled.
   */
  @Override
  public void disabledInit() {
    autoMode = false;
    limelight.pauseLimelight();
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
  }

  /**
   * This function is called once when test mode is enabled.
   */
  @Override
  public void testInit() {
    autoMode = false;
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    autoMode = false;
  }

  /**
   * Get the name of an autonomous mode command.
   *
   * @return the name of the auto command.
   */
  public static String getAutoName() {
    if (autonomousCommand != null) {
      return autonomousCommand.getName();
    } else {
      return "None";
    }
  }

  /**
   * Adds boolean choice of whether or not to push another robot off the line
   */
  public void pushRobotSelectInit() {
    pushRobotChooser = new SendableChooser<Number>();
    pushRobotChooser.setDefaultOption("DO NOT push robot", 0);
    pushRobotChooser.addOption("DO push robot", 1);
  }

  /**
   * Returns boolean for whether or not we want to push another robot off the line
   */
  public static boolean getPushRobot() {
    boolean retVal;
    switch(pushRobot) {
      case 0 :
        retVal = false;
        break;
      case 1 :
        retVal = true;
        break;
      default :
        retVal = false;
        break;
    }
    return retVal;
  }

  public void setShooterAtSpeedLEDs() {
    if (shooter.optimalSpeed()) {
      // Turn on GREEN LEDs
      toggleLEDs.set(Relay.Value.kForward);
  } else {
      //Turn on RED LEDs
      toggleLEDs.set(Relay.Value.kReverse);
    }
  }

  /**
   * TODO return jumper value from DIO 24
   */
  public static boolean isPracticeBot() {
    return !practiceBot.get();
  }

  public static boolean isAutoMode() {
    return autoMode;
  }

  public static boolean onBlueAlliance() {
    return DriverStation.getInstance().getAlliance() == Alliance.Blue;
  }

  public static void setCompressorOn(boolean on) {
    if(on) {
      compressor.start();
    }
    else {
      compressor.stop();
    }
  }
}
