package org.team2168;

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
  public static final int OPERATOR_JOYSTICK = 1;
	public static final int BUTTON_BOX_1 = 2;
	public static final int BUTTON_BOX_2 = 3;
	public static final int DRIVER_OPERATOR_E_BACKUP = 4;
	public static final int TEST_JOYSTICK = 5;

	// Joystick Control Styles/////////////////////////////////////////////////


	// PWM (0 to 9) on RoboRio/////////////////////////////////////////////////
  public static final int PWM_LIGHTS = 9;

  // Digital IO Channels//////////////////////////////////////////////////////
  // Channels 0-9 on RoboRio
	public static final int ENTRANCE_LINE_BREAK = 5; //TODO SET
	public static final int EXIT_LINE_BREAK = 6;

	//Channels 10-25 on MXP (PWM and DIO)
  public static final int PRACTICE_BOT_JUMPER = 24;


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
  //Double Soldenoids PCM ID = 0 ///////////////////////////////////////////
	public static final int INTAKE_RETRACT_PCM = 0;
	public static final int INTAKE_EXTEND_PCM = 1;
	public static final int CLIMBER_RATCHET_ENGAGE_PCM = 2;
	public static final int CLIMBER_RATCHET_DISENGAGE_PCM = 3;
	public static final int PANCAKE_SOLENOID_OUT = 4;
	public static final int PANCAKE_SOLENOID_IN = 5;
	public static final int HOOD_SOLENOID_EXTEND = 6;
	public static final int HOOD_SOLENOID_RETRACT = 7;

	/*************************************************************************
	*                         PDP/CAN DEVICES                                *
  *************************************************************************/

  public static final int DRIVE_3_CAN_ID = 0;
  public static final int DRIVE_1_CAN_ID = 1;
  public static final int CLIMBER_1_CAN_ID = 2;
  public static final int CLIMBER_2_CAN_ID = 3;
  public static final int INDEXER_CAN_ID = 4;
  public static final int INTAKE_CAN_ID = 5;
  public static final int AZIMUTH_3_CAN_ID = 6;
  public static final int AZIMUTH_1_CAN_ID = 7;
  public static final int AZIMUTH_2_CAN_ID = 8;
  public static final int AZIMUTH_0_CAN_ID = 9;
  public static final int HOPPER_BALANCER_CAN_ID = 10; //hopper balancer?
  public static final int SHOOTER_1_CAN_ID = 12;
  public static final int SHOOTER_2_CAN_ID = 13;
  public static final int DRIVE_2_CAN_ID = 14;
  public static final int DRIVE_0_CAN_ID = 15;
  public static final int PIGEON_IMU_CAN_ID = 17;

  public static final int CANCODER_0_CAN_ID = 0;
  public static final int CANCODER_1_CAN_ID = 1;
  public static final int CANCODER_2_CAN_ID = 2;
  public static final int CANCODER_3_CAN_ID = 3;

  public static final int PCM_CAN_ID_BELLYPAN = 0;

	/**
	 * ORIENTATION OF SWERVE MODULES:
	 * 		Front (shooter)
	 * 				0		1
	 *				2		3
	 *		Back (hopper)
	*/
  public static final int[] AZIMUTH_TALON_ID = {AZIMUTH_0_CAN_ID, AZIMUTH_1_CAN_ID,
                                                AZIMUTH_2_CAN_ID, AZIMUTH_3_CAN_ID};
  public static final int[] DRIVE_TALON_ID   = {DRIVE_0_CAN_ID, DRIVE_1_CAN_ID,
                                                DRIVE_2_CAN_ID, DRIVE_3_CAN_ID};

  public static final int[] CANCODER_ID = {CANCODER_0_CAN_ID, CANCODER_1_CAN_ID,
                                          CANCODER_2_CAN_ID, CANCODER_3_CAN_ID};

	// Relay Channels///////////////////////////////////////////////////////////
  public static final int LED_RELAY_CHANNEL = 3;

	/******************************************************************
	 *                         Lights I2C                             *
	 ******************************************************************/


    /*************************************************************************
	 *                         SPI DEVICES                                   *
	 *************************************************************************/



	/*************************************************************************
	 *                         PBOT DIFFERENCES  PARAMETERS                  *
	 *************************************************************************/

	 
	/*************************************************************************
	 *                      				   SPEEDS              							    *
	 *************************************************************************/
	public static final double HOPPER_SPEED = 0.8;
	public static final double INDEXER_SPEED = 1.0;
	public static final double INTAKE_SPEED = 0.95;  // needs to be >0.88 to be faster than the NEO
	public static final double INTAKE_SPEED_SLOW = 0.2;
	public static final double SHOOTER_SPEED = 1.0;


}
