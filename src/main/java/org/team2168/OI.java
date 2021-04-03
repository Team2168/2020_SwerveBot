
package org.team2168;

import org.team2168.commands.drivetrain.ZeroEncoders;
import org.team2168.commands.drivetrain.ZeroGyro;
import org.team2168.commands.limelight.DriveWithLimelight;
import org.team2168.commands.limelight.TurnWithLimelight;
import org.team2168.commands.limelight.DriveWithLimelight2;
import org.team2168.utils.F310;
import org.team2168.utils.LinearInterpolator;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	private static OI instance = null;

	public F310 driverJoystick = new F310(RobotMap.DRIVER_JOYSTICK);
	public F310 testJoystick = new F310(RobotMap.TEST_JOYSTICK);
	// public F310 operatorJoystick = new F310(RobotMap.OPERATOR_JOYSTICK);

	// public F310 driverOperatorEJoystick = new
	// F310(RobotMap.DRIVER_OPERATOR_E_BACKUP);

	// public F310 testJoystick = new F310(RobotMap.COMMANDS_TEST_JOYSTICK);
	//public F310 pidTestJoystick = new F310(RobotMap.PID_TEST_JOYSTICK);

	private LinearInterpolator driverJoystickYInterpolator;
	private LinearInterpolator driverJoystickXInterpolator;
	private LinearInterpolator driverJoystickZInterpolator;
	private LinearInterpolator driverFlightStickZInterpolator;
	private double[][] driverJoystickYArray = {
		{-1.0, -1.0},  //don't scale turning max
		{-0.15, 0.00}, //set neutral deadband to 15%
		{+0.15, 0.00},
		{+1.00,+1.00}
	};
	private double[][] driverJoystickXArray = {
		{-1.0, -1.0},  //don't scale turning max
		{-0.15, 0.00}, //set neutral deadband to 15%
		{+0.15, 0.00},
		{+1.00,+1.00}
	};
	private double[][] driverJoystickZArray = {
		{-1.0, -0.50},  //scale down turning to max 50%
		{-0.05, 0.00},  //set neutral deadband to 5%
		{+0.05, 0.00},
		{+1.00,+0.50}
	};
	private double[][] driverFlightStickZArray = {
		{-1.0, -0.50},
		{-0.08, 0.00},
		{+0.05, 0.00},
		{+1.00, +0.50}
	};

	public static final SendableChooser<String> joystickChooser = new SendableChooser<>();

	/**
	 * Private constructor for singleton class which instantiates the OI object
	 */
	private OI() {
		driverJoystickYInterpolator = new LinearInterpolator(driverJoystickYArray);
		driverJoystickXInterpolator = new LinearInterpolator(driverJoystickXArray);
		driverJoystickZInterpolator = new LinearInterpolator(driverJoystickZArray);
		driverFlightStickZInterpolator = new LinearInterpolator(driverFlightStickZArray);

		SmartDashboard.putData("Driver Joystick Chooser", joystickChooser);
		joystickChooser.setDefaultOption("Flight Joystick", "flight");
    joystickChooser.addOption("F310 Joystick", "F310");

		driverJoystick.ButtonA().whileHeld(new TurnWithLimelight());
		driverJoystick.ButtonB().whileHeld(new DriveWithLimelight2());
		testJoystick.ButtonBack().whenPressed(new ZeroGyro());
		testJoystick.ButtonStart().whenPressed(new ZeroEncoders());
	}
	
	/**
	 * Returns an instance of the Operator Interface.
	 * 
	 * @return is the current OI object
	 */
	public static OI getInstance() {
		if (instance == null)
			instance = new OI();

		return instance;
	}

	/*************************************************************************
	 * Drivetrain *
	 *************************************************************************/

	/**
	 * Get the value of the left stick's x-axis after being put through the interpolator
	 * @return a value from -0.1 to 0.1
	 */
	public double getDriverJoystickXValue() {
		return driverJoystickXInterpolator.interpolate(driverJoystick.getLeftStickRaw_X());
	}

	/**
	 * Get the value of the left stick's y-axis after being put through the interpolator
	 * @return a value from -0.1 to 0.1
	 */
	public double getDriverJoystickYValue() {
		return driverJoystickYInterpolator.interpolate(driverJoystick.getLeftStickRaw_Y());
	}

	/**
	 * Get the value of the right stick's x-axis after being put through the interpolator
	 * @return a value from -0.5 to 0.5
	 */
	public double getDriverJoystickZValue() {
		if (joystickChooser.getSelected().equals("flight"))
			return driverJoystickZInterpolator.interpolate(driverJoystick.getRawAxis(2));
		else
			return driverFlightStickZInterpolator.interpolate(driverJoystick.getRightStickRaw_X());
	}

}