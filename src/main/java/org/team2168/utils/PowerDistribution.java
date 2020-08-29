package org.team2168.utils;

import java.util.TimerTask;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class PowerDistribution {
	private java.util.Timer executor;
	private long period;

	private PowerDistributionPanel pdp;

	private volatile NPointAverager[] channelCurrent;
	private volatile double[] channelPower;
	private volatile int[] channelError;

	private volatile double batteryVoltage;
	private volatile double mainBreakerTemp;

	private volatile double totalCurrent;
	private volatile double totalEnergy;
	private volatile double totalPower;
	private volatile double temperature;

	public static final int NUM_OF_PDP_CHANNELS = 16;
	private static final int PDP_CAN_ID = 0;
	private static final long PDPThreadPeriod = 100;
	private static final double WARNING_CURRENT_LIMIT = 20;
	private static final double STALL_CURRENT_LIMIT = 350;
	private static final double CURRENT_LIMIT_TIME_THRESHOLD_SECONDS = 1;


	int currentTimeThreshold;

	public PowerDistribution(long period) {
		this.period = period;
		pdp = new PowerDistributionPanel(PDP_CAN_ID);

		// Calculate number of loops needed to meet time iteration
		currentTimeThreshold = (int) (CURRENT_LIMIT_TIME_THRESHOLD_SECONDS / (this.period * 1000));

		channelCurrent = new NPointAverager[NUM_OF_PDP_CHANNELS];

		// initialize NPoint Averager for each channel
		for (int i = 0; i < NUM_OF_PDP_CHANNELS; i++)
			channelCurrent[i] = new NPointAverager(currentTimeThreshold);

		channelPower = new double[NUM_OF_PDP_CHANNELS];
		channelError = new int[NUM_OF_PDP_CHANNELS];

		//WHAT WHY??? static call to console printer to pass in a reference to a public static variable held inside the robot class, which is an instance of this classa!@?#!@#
		// TODO: Clean up, utils shouldn't have references into robot code
		// ConsolePrinter.putNumber("Battery Voltage", () -> {return Robot.pdp.getBatteryVoltage();}, true, false);
		// ConsolePrinter.putNumber("totalCurrent", () -> {return Robot.pdp.getTotalCurrent();}, true, false);
		// ConsolePrinter.putNumber("pcmCompressorCurrent", () -> {return Robot.pdp.getChannelCurrent(RobotMap.COMPRESSOR_PDP);
		// 	}, true, false);

	}

	public void startThread() {
		this.executor = new java.util.Timer();
		this.executor.schedule(new PowerDistributionTask(this), 0L, period);
	}

	public int getChannelError(int channel) {
		if ((channel >= channelPower.length) || (channel < 0))
			return 0;

		return channelError[channel];
	}

	public double getChannelCurrent(int channel) {
		if ((channel >= channelPower.length) || (channel < 0))
			return 0;

		return channelCurrent[channel].getLatestValue();
	}

	public double getBatteryVoltage() {
		return batteryVoltage;
	}

	public double getChannelPower(int channel) {
		if ((channel >= channelPower.length) || (channel < 0))
			return 0;

		return channelPower[channel];
	}

	private void run() {
		batteryVoltage = pdp.getVoltage();

		for (int i = 0; i < NUM_OF_PDP_CHANNELS; i++) {

			channelCurrent[i].putData(pdp.getCurrent(i));
			channelPower[i] = channelCurrent[i].getLatestValue() * batteryVoltage;

			// calculate current average over last period of time and report error
			if (channelCurrent[i].getAverage() > STALL_CURRENT_LIMIT)
				channelError[i] = 2; // danger
			else if (channelCurrent[i].getAverage() > WARNING_CURRENT_LIMIT)
				channelError[i] = 1; // warning
			else
				channelError[i] = 0; // assume no error

		}

		totalCurrent = pdp.getTotalCurrent();
		totalEnergy = pdp.getTotalEnergy();
		temperature = pdp.getTemperature();
		totalPower = pdp.getTotalPower();

	}

	private class PowerDistributionTask extends TimerTask {
		private PowerDistribution console;

		private PowerDistributionTask(PowerDistribution printer) {
			if (printer == null) {
				throw new NullPointerException("PDP was null");
			}
			this.console = printer;
		}

		/**
		 * Called periodically in its own thread
		 */
		public void run() {
			console.run();
		}
	}

	/**
	 * Gets total Current
	 * 
	 * @return Total Current
	 */
	public double getTotalCurrent() {
		return totalCurrent;
	}

	/**
	 * Gets total Energy
	 * 
	 * @return Total Energy
	 */
	public double totalEnergy() {
		return totalEnergy;
	}

	/**
	 * Gets total Power
	 * 
	 * @return Total Power
	 */
	public double totalPower() {
		return totalPower;
	}

	//TODO: Fix. Utils shouldn't have static references to robot code.
	// public boolean isRightMotorThreeTrip() {
	// 	if (channelError[RobotMap.DRIVETRAIN_RIGHT_MOTOR_3_PDP] == 2)
	// 		return true;
	// 	else 
	// 		return false;
	// }

	// public boolean isRightMotorTwoTrip() {
	// 	if (channelError[RobotMap.DRIVETRAIN_RIGHT_MOTOR_2_PDP] == 2)
	// 		return true;
	// 	else
	// 		return false;
	// }

	// public boolean isRightMotorOneTrip() {
	// 	if (channelError[RobotMap.DRIVETRAIN_RIGHT_MOTOR_1_PDP] == 2)
	// 		return true;
	// 	else
	// 		return false;
	// }

	
	// public boolean isLeftMotorOneTrip() {
	// 	if (channelError[RobotMap.DRIVETRAIN_LEFT_MOTOR_1_PDP] == 2)
	// 		return true;
	// 	else
	// 		return false;
	// }

	// public boolean isLeftMotorTwoTrip() {
	// 	if (channelError[RobotMap.DRIVETRAIN_LEFT_MOTOR_2_PDP] == 2)
	// 		return true;
	// 	else
	// 		return false;
	// }

	// public boolean isLeftMotorThreeTrip() {
	// 	if (channelError[RobotMap.DRIVETRAIN_LEFT_MOTOR_3_PDP] == 2)
	// 		return true;
	// 	else
	// 		return false;
	// }

	
	// public boolean isLiftMotorOneTrip() {
	// 	if (channelError[RobotMap.LIFT_MOTOR_1_PDP] == 2)
	// 		return true;
	// 	else
	// 		return false;
	// }
	
	// public boolean isLiftMotorTwoTrip() {
	// 	if (channelError[RobotMap.LIFT_MOTOR_2_PDP] == 2)
	// 		return true;
	// 	else
	// 		return false;
	// }
	

	// public boolean isIntakeMotorTrip() {
	// 	if (channelError[RobotMap.CARGO_INTAKE_MOTOR_PDP] == 2)
	// 		return true;
	// 	else
	// 		return false;
	// }


	// public boolean isPlungerArmPivotMotorTrip(){
	// 	if (channelError[RobotMap.PLUNGER_PIVOT_MOTOR_PDP] ==2)
	// 		return true;
	// 	else 
	// 		return false;
	// }
		
	
}
