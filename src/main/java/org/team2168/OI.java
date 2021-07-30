
package org.team2168;

import org.team2168.commands.auto.robotFunctions.FinishFiring;
import org.team2168.commands.auto.robotFunctions.FireBalls;
import org.team2168.commands.auto.robotFunctions.FireSingleBall;
import org.team2168.commands.climber.Climb;
import org.team2168.commands.climber.DisengageRatchet;
import org.team2168.commands.climber.DriveClimberWithTestJoystickUnSafe;
import org.team2168.commands.climber.EngageRatchet;
import org.team2168.commands.climber.PrepareToClimb;
import org.team2168.commands.climber.ResetClimberPosition;
import org.team2168.commands.drivetrain.DriveWithConstant;
import org.team2168.commands.drivetrain.DriveWithFixedAzimuth;
import org.team2168.commands.drivetrain.ZeroEncoders;
import org.team2168.commands.drivetrain.ZeroGyro;
import org.team2168.commands.flashlight.RunFlashlight;
import org.team2168.commands.hood_adjust.MoveToBackTrench;
import org.team2168.commands.hood_adjust.MoveToFiringLocation;
import org.team2168.commands.hood_adjust.MoveToFrontTrench;
import org.team2168.commands.hood_adjust.MoveToWLNoShoot;
import org.team2168.commands.hood_adjust.MoveToWall;
import org.team2168.commands.hood_adjust.MoveToWallNoShoot;
import org.team2168.commands.hood_adjust.MoveToWhiteLine;
import org.team2168.commands.hopper.DriveHopperWithConstant;
import org.team2168.commands.intakeMotor.DriveIntakeWithConstant;
import org.team2168.commands.intakeMotor.IntakeBallStart;
import org.team2168.commands.intakeMotor.IntakeBallStop;
import org.team2168.commands.intakePivot.ExtendIntakePneumatic;
import org.team2168.commands.intakePivot.RetractIntakePneumatic;
import org.team2168.commands.limelight.DriveWithLimelight;
import org.team2168.commands.limelight.ToggleLimelightPipeline;
import org.team2168.commands.shooter.BumpDownShooterSpeed;
import org.team2168.commands.shooter.BumpUpShooterSpeed;
import org.team2168.commands.shooter.BumpZeroShooterSpeed;
import org.team2168.commands.shooter.DriveShooterWithConstant;
import org.team2168.commands.shooter.DriveToXSpeed;
import org.team2168.subsystems.Limelight;
import org.team2168.subsystems.Shooter;
import org.team2168.utils.F310;
import org.team2168.utils.LinearInterpolator;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

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
  public F310 operatorJoystick = new F310(RobotMap.OPERATOR_JOYSTICK);
	public F310 testJoystick = new F310(RobotMap.TEST_JOYSTICK);
	public F310 buttonBox1 = new F310(RobotMap.BUTTON_BOX_1);
	public F310 buttonBox2 = new F310(RobotMap.BUTTON_BOX_2);

	private LinearInterpolator colorWheelInterpolator;
	private LinearInterpolator climberInterpolator;
	private LinearInterpolator balancerInterpolator;
  private LinearInterpolator climberResetInterpolator;
  private double[][] colorWheelArray = {
		{-1.0, -1.00},
		{-0.05, 0.00},  //set neutral deadband to 4%
		{+0.05, 0.00},
		{+1.0, +1.00}
	};

	private double[][] climberArray = {
		{-1.0, -0.80},
		{-0.50, 0.00},  //set neutral deadband to 4%
		{+0.50, 0.00},
		{+1.0, +0.80}
	};

	private double[][] balancerArray = {
		{-1.0, -1.00},
		{-0.05, 0.00},  //set neutral deadband to 4%
		{+0.05, 0.00},
		{+1.0, +1.00}
	};

	private double[][] climberResetArray = {
		{-1.0, -0.22},
		{-0.05, 0.00},
		{+0.05, 0.00},
		{+1.0, +0.22}
	};

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
		{-1.0, -0.50},  //scale down turning to max 50%
		{-0.09, 0.00},  //set neutral deadband to 5%
		{+0.09, 0.00},
		{+1.00,+0.50}
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

    if (Robot.ENABLE_BUTTON_BOX)
    {
      //******************************************************************* */
      //*							Button Box I
      //******************************************************************* */
      colorWheelInterpolator = new LinearInterpolator(colorWheelArray);


      // buttonBox1.ButtonUpDPad().whenPressed(new EngageColorWheel());
      // buttonBox1.ButtonDownDPad().whenPressed(new DisengageColorWheel());
      // buttonBox1.ButtonLeftDPad().whenPressed(new DriveColorWheelXRotations(-4.0*8.0)); 	//go opposite direction to protect limelight
      //Right D Pad, Position
      buttonBox1.ButtonA().whenPressed(new BumpUpShooterSpeed());
      buttonBox1.ButtonB().whenPressed(new BumpDownShooterSpeed());
      buttonBox1.ButtonX().whenPressed(new BumpZeroShooterSpeed());

      buttonBox1.ButtonY().whenPressed(new DriveToXSpeed(Shooter.FiringLocation.BACK_TRENCH));
      buttonBox1.ButtonLeftBumper().whenPressed(new DriveHopperWithConstant(-1.0));// Temporary value
      buttonBox1.ButtonLeftBumper().whenReleased(new DriveHopperWithConstant(0.0));
      buttonBox1.ButtonRightBumper().whenPressed(new MoveToWallNoShoot());
      // buttonBox1.ButtonRightBumper().whenPressed(new DisengageColorWheel());

      // buttonBox1.ButtonLeftStick().whenPressed(new DriveClimberWithConstant(0.0));
      // buttonBox1.ButtonLeftStick().whenReleased(new DriveClimberWithJoystick());

      //******************************************************************** */
      //*							Button Box II
      //******************************************************************** */
      climberInterpolator = new LinearInterpolator(climberArray);
      balancerInterpolator = new LinearInterpolator(balancerArray);

      buttonBox2.ButtonDownDPad().whenPressed(new DriveToXSpeed(Shooter.FiringLocation.WALL));
      buttonBox2.ButtonLeftDPad().whenPressed(new DriveToXSpeed(Shooter.FiringLocation.FRONT_TRENCH));
      buttonBox2.ButtonRightDPad().whenPressed(new DriveToXSpeed(Shooter.FiringLocation.WHITE_LINE));
			// buttonBox2.ButtonA().whenPressed(new MoveToFiringLocation(Shooter.getInstance().getFiringLocation()));
      // testJoystick.ButtonA().whenReleased(new MoveToWallNoShoot());

      buttonBox2.ButtonB().whenPressed(new FireBalls());
      buttonBox2.ButtonB().whenReleased(new FinishFiring());

      //spit button
      buttonBox2.ButtonLeftBumper().whenPressed(new DriveIntakeWithConstant(-1.0));
      buttonBox2.ButtonLeftBumper().whenPressed(new ExtendIntakePneumatic());
      buttonBox2.ButtonLeftBumper().whenReleased(new DriveIntakeWithConstant(0.0));
      buttonBox2.ButtonLeftBumper().whenReleased(new RetractIntakePneumatic()); //DO WE WANT THIS

      buttonBox2.ButtonRightBumper().whenPressed(new IntakeBallStart());
      buttonBox2.ButtonRightBumper().whenReleased(new IntakeBallStop());

      buttonBox2.ButtonBack().whenPressed(new PrepareToClimb());
      buttonBox2.ButtonBack().whenPressed(new DriveToXSpeed(0.0)); //Stop shooter and lower hood
      buttonBox2.ButtonBack().whenPressed(new MoveToWLNoShoot());
      buttonBox2.ButtonStart().whenPressed(new Climb());
      //right stick--auto balance
    }

    /*************************************************************************
     * Driver Joystick *
     *************************************************************************/

    // driverJoystick.ButtonLeftStick().whileHeld(new LimelightTurnTeleop(1.50));
    // driverJoystick.ButtonLeftStick().whenReleased(new DriveWithJoystick());

    driverJoystick.ButtonA().whenPressed(new RunFlashlight(1.0));
		driverJoystick.ButtonA().whenReleased(new RunFlashlight(-0.5));
		if (joystickChooser.getSelected().equals("flight")) {
			driverJoystick.ButtonX().whileHeld(new DriveWithLimelight());
		}
		else {
			driverJoystick.ButtonRightBumper().whileHeld(new DriveWithLimelight());
		}

    //When the red button on the handle of the controller is pressed get ready to go under the trench. Lower everything.
    // driverJoystick.ButtonLeftBumper().whileHeld(new DisengageColorWheel());
    driverJoystick.ButtonLeftBumper().whenPressed(new MoveToFiringLocation(Shooter.FiringLocation.WALL));
    driverJoystick.ButtonBack().whenPressed(new ZeroGyro()); //button 7 on flight stick, Back on F310

    // Rotate the chassis to fixed headings while still allowing the operator o control x & y from sticks
    new JoystickButton(driverJoystick, 12).whileHeld(new DriveWithFixedAzimuth(0.0, true)); // face shooter towards goal (button 12 on flight stick)
    new JoystickButton(driverJoystick, 11).whileHeld(new DriveWithFixedAzimuth(90.0, true)); // face shooter towards right side of field (button 11 on flight stick)
    new JoystickButton(driverJoystick, 9).whileHeld(new DriveWithFixedAzimuth(65.0, true)); // align for climb (button 9 on flight stick)

    /*************************************************************************
     * Operator Joystick *
     *************************************************************************/
		operatorJoystick.ButtonUpDPad().whenPressed(new MoveToBackTrench());
		operatorJoystick.ButtonLeftDPad().whenPressed(new MoveToFrontTrench());
		operatorJoystick.ButtonRightDPad().whenPressed(new MoveToWhiteLine());
		operatorJoystick.ButtonDownDPad().whenPressed(new MoveToWall());


    // operatorJoystick.ButtonY().whenPressed(new EngageColorWheel());
    // operatorJoystick.ButtonA().whenPressed(new DisengageColorWheel());
    // operatorJoystick.ButtonA().whenPressed(new MoveToWall());

    // operatorJoystick.ButtonStart().whenPressed(new DriveColorWheelXRotations(-4.0*8.0)); 	//go opposite direction to protect limelight


    // operatorJoystick.ButtonX().whenPressed(new DriveToXSpeed(Shooter.getInstance().WALL_VEL));
    operatorJoystick.ButtonX().whenPressed(new DriveShooterWithConstant(0.52));

    operatorJoystick.ButtonB().whenPressed(new FireBalls());
		operatorJoystick.ButtonB().whenReleased(new FinishFiring());

    operatorJoystick.ButtonLeftBumper().whenPressed(new IntakeBallStop());
		operatorJoystick.ButtonRightBumper().whenPressed(new IntakeBallStart());

    /***********************************************************************
     * Test Joystick
     ***********************************************************************/
    // // //leds testing
    // testJoystick.ButtonB().whenPressed(new DriveXDistance(60.0));
    // testJoystick.ButtonX().whenPressed(new DriveXDistance(-60.0));
    // testJoystick.ButtonRightDPad().whenPressed(new TurnXAngle(-9.0, 0.3));
    // testJoystick.ButtonLeftDPad().whenPressed(new TurnXAngle(+9.0,0.3));
    // testJoystick.ButtonUpDPad().whenPressed(new TurnXAngle(-90.0,0.3));
    // testJoystick.ButtonDownDPad().whenPressed(new TurnXAngle(+90.0, 0.3));
    // testJoystick.ButtonStart().whenPressed(new DefaultTrenchAuto());
    // testJoystick.ButtonBack().whenPressed(new OppositeTrenchAuto());

  	// for zeroing while the robot is enabled
	  // testJoystick.ButtonBack().whenPressed(new ZeroGyro());
    // testJoystick.ButtonStart().whenPressed(new ZeroEncoders());

    testJoystick.ButtonX().whenPressed(new ResetClimberPosition());
    testJoystick.ButtonY().whenPressed(new PrepareToClimb());
    testJoystick.ButtonA().whenPressed(new Climb());

    testJoystick.ButtonB().whenPressed(new ToggleLimelightPipeline(Limelight.PIPELINE_DRIVE_WITH_LIMELIGHT));
    testJoystick.ButtonLeftDPad().whileHeld(new DriveWithConstant(0.0, 0.0, -0.08));
    testJoystick.ButtonRightDPad().whileHeld(new DriveWithConstant(0.0, 0.0, 0.08));

    testJoystick.ButtonUpDPad().whenPressed(new EngageRatchet());
    testJoystick.ButtonDownDPad().whenPressed(new DisengageRatchet());

    climberResetInterpolator = new LinearInterpolator(climberResetArray);
		testJoystick.ButtonRightStick().whenPressed(new DriveClimberWithTestJoystickUnSafe());

		// testJoystick.ButtonLeftBumper().whenPressed(new FireBalls());
		// testJoystick.ButtonLeftBumper().whenReleased(new FinishFiring());
		testJoystick.ButtonRightBumper().whileHeld(new FireSingleBall());
		// testJoystick.ButtonRightBumper().whenReleased(new FinishFiring());

    // testJoystick.ButtonB().whenPressed(new FireBalls());
    // testJoystick.ButtonB().whenReleased(new FinishFiring());
    // testJoystick.ButtonDownDPad().whenPressed(new MoveToWall());
    // testJoystick.ButtonLeftBumper().whenPressed(new IntakeBallStop());
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
		//if (joystickChooser.getSelected().equals("flight"))
			return driverFlightStickZInterpolator.interpolate(driverJoystick.getRawAxis(2));
		// else
		// 	return driverJoystickZInterpolator.interpolate(driverJoystick.getRightStickRaw_X());
	}

  public double getColorWheelJoystick() {
		return colorWheelInterpolator.interpolate(buttonBox1.getRightStickRaw_X()); //operatorJoystick.getRightStickRaw_Y();
	}

	public double getIntakeMotorJoyStick() {
		return operatorJoystick.getRightTriggerAxisRaw() - operatorJoystick.getLeftTriggerAxisRaw();
	}

	/**
	 * Return value of axis for the indexer
	 *
	 * @return a double - value
	 */
	public double getIndexerJoystick() {
		return 0.0; //testJoystick.getRightStickRaw_Y();
	}

	/**
	 * Climber joystick value
	 *
	 * @return
	 */
	public double getClimberJoystickValue() {
		return climberInterpolator.interpolate(buttonBox2.getLeftStickRaw_Y());
	}

	public double getClimberTestJoystickValue() {
		return climberResetInterpolator.interpolate(testJoystick.getRightStickRaw_Y());
	}

	/**
	 * Shooter joystick value
	 * @return
	 */
	public double getShooterJoystick() {
		return 0.0; //testJoystick.getRightStickRaw_Y();
	}

	/**
		 * Balancer joystick value
		 *
		 * @return
		 */
		public double getBalancerJoystickValue()
		{
			return balancerInterpolator.interpolate(buttonBox2.getLeftStickRaw_X());
		}

	/**
	 * Hopper joystick value
	 *
	 * @return
	 */
	public double getHopperJoystickValue() {
		return  0.0; //testJoystick.getRightStickRaw_Y();
	}

}