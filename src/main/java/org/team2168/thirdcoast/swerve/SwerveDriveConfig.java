package org.team2168.thirdcoast.swerve;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.TimedRobot;

public class SwerveDriveConfig {

  /**
   * NavX gyro connected to MXP SPI port, used for field-oriented driving. If null, field-oriented
   * driving is disabled.
   */
  public AHRS gyro;

  /** Initialize with four initialized wheels, in order from wheel 0 to wheel 3.
   *  0 is front left, 1 is front right, 2 is back left, and 3 is back right
   */
  public Wheel[] wheels;

  /** Wheel base length from front to rear of robot. */
  public double length = 25.0; // inches

  /** Wheel base width from left to right of robot. */
  public double width = 21.5; // inches

  /**
   * Robot period is the {@code TimedRobot} period in seconds, defaults to {@code
   * TimedRobot.kDefaultPeriod}.
   */
  public double robotPeriod = TimedRobot.kDefaultPeriod;

  /** Factor to correct gyro lag when simultaneously applying azimuth and drive. */
  public double gyroRateCoeff = 0.0;

  /** Log gyro errors, set to false if too spammy. */
  public boolean gyroLoggingEnabled = true;

  /**
   * Summarize Talon configuration errors. If false, will log error messages as each error is
   * encountered.
   */
  public boolean summarizeTalonErrors = false;
}
