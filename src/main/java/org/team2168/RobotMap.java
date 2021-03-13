package org.team2168;

import com.ctre.phoenix.motorcontrol.RemoteSensorSource;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around
 */
public class RobotMap {

    public static final double MAIN_PERIOD_S = 1.0/50.0; // Main loop 200Hz

	/*************************************************************************
	 *                        ROBORIO WIRING MAP                             *
	 *************************************************************************/

	// Joysticks///////////////////////////////////////////////////////////////
	public static final int DRIVER_JOYSTICK = 0;
	public static final int TEST_JOYSTICK = 5;


	// Joystick Control Styles/////////////////////////////////////////////////


	// PWM (0 to 9) on RoboRio/////////////////////////////////////////////////


	// Digital IO Channels//////////////////////////////////////////////////////


	//Analog Input Channels////////////////////////////////////////////////////
  // Channels 0-3 on Roborio
  public static final int SWERVE_MODULE_0_AI = 0;
  public static final int SWERVE_MODULE_1_AI = 1;
  public static final int SWERVE_MODULE_2_AI = 2;
  public static final int SWERVE_MODULE_3_AI = 3;

  public static final int[] SWERVE_ENCODER_AI = {SWERVE_MODULE_0_AI, SWERVE_MODULE_1_AI,
                                                 SWERVE_MODULE_2_AI, SWERVE_MODULE_3_AI};

	// Channels 4-7 on MXP


	/*************************************************************************
	*                         Solenoids                                      *
	*************************************************************************/



	/*************************************************************************
	*                         PDP/CAN DEVICES                                *
  *************************************************************************/

  public static final int DRIVE_3_ID = 0;
  public static final int DRIVE_1_ID = 1;
  public static final int CLIMBER_1_ID = 2;
  public static final int CLIMBER_2_ID = 3;
  public static final int BALANCER_ID = 4;
  public static final int INTAKE_ID = 5;
  public static final int AZIMUTH_3_ID = 6;
  public static final int AZIMUTH_1_ID = 7;
  public static final int AZIMUTH_2_ID = 8;
  public static final int AZIMUTH_0_ID = 9;
  public static final int HOPPER_INDEXER_ID = 10;
  public static final int SHOOTER_1_ID = 12;
  public static final int SHOOTER_2_ID = 13;
  public static final int DRIVE_2_ID = 14;
  public static final int DRIVE_0_ID = 15;
  public static final int PIGEON_IMU_ID = 17;

	/**
	 * ORIENTATION OF SWERVE MODULES:
	 * 		Front (shooter)
	 * 				0		1
	 *				2		3
	 *		Back (hopper)
	*/
  public static final int[] AZIMUTH_TALON_ID = {AZIMUTH_0_ID, AZIMUTH_1_ID,
                                                AZIMUTH_2_ID, AZIMUTH_3_ID};
  public static final int[] DRIVE_TALON_ID   = {DRIVE_0_ID, DRIVE_1_ID,
                                                DRIVE_2_ID, DRIVE_3_ID};


	// Relay Channels///////////////////////////////////////////////////////////


	/******************************************************************
	 *                         Lights I2C                             *
	 ******************************************************************/


    /*************************************************************************
	 *                         SPI DEVICES                                   *
	 *************************************************************************/



	/*************************************************************************
	 *                         PBOT DIFFERENCES  PARAMETERS                  *
	 *************************************************************************/

}
