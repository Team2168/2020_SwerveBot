package org.team2168;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around\][\
 */
public class RobotMap {

    public static final double MAIN_PERIOD_S = 1.0/50.0; // Main loop 200Hz

	/*************************************************************************
	 *                        ROBORIO WIRING MAP                             *
	 *************************************************************************/

	// Joysticks///////////////////////////////////////////////////////////////
	public static final int DRIVER_JOYSTICK = 0;
	


	// Joystick Control Styles/////////////////////////////////////////////////
	

	// PWM (0 to 9) on RoboRio/////////////////////////////////////////////////


	// Digital IO Channels//////////////////////////////////////////////////////


	//Analog Input Channels////////////////////////////////////////////////////
	//Channels 0-3 on Roborio

	// Channels 4-7 on MXP


	/*************************************************************************
	*                         Solenoids                                      *
	*************************************************************************/

	

	/*************************************************************************
	*                         PDP/CAN DEVICES                                 *
	*************************************************************************/
	public static final int[] AZIMUTH_TALON_ID = {12, 2, 14, 0};
	public static final int[] DRIVE_TALON_ID = {13, 3, 15, 1};


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
