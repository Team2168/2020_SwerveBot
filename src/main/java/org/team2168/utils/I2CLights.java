package org.team2168.utils;

import edu.wpi.first.wpilibj.I2C;

/**
 * The subsystem for controlling lights.
 * 
 * @Author Elijah and Bennett
 */
public class I2CLights {
	private I2C i2c;
	private static I2CLights instance = null;

	private static final I2C.Port I2C_PORT = I2C.Port.kOnboard;
	private static final int I2C_ADDRESS = 8;

	private static byte[] data = new byte[8];


	public enum Pattern {
		Off(0), Solid(1), FastBlink(2), SlowBlink(3), Fade(4), Chase(5), Rainbow(6), Cylon(7);

		private final int val;

		Pattern(int val) {
			this.val = val;
		}

		public int getVal() {
			return val;
		}
	}

	public enum Range {
		Hopper, Shooter;
	}

	private I2CLights() {
		i2c = new I2C(I2C_PORT, I2C_ADDRESS);
	}

	/**
	 * Singleton constructor for Lights subsystem
	 * 
	 * @return singleton instance of Lights subsystem
	 */
	public static I2CLights getInstance() {
		if (instance == null)
			instance = new I2CLights();
		return instance;
	}

	/**
	 * Takes in value to directly right to LED's over I2C.
	 * 
	 * @param r
	 *            Red value between 0 and 255.
	 * @param g
	 *            Green value between 0 and 255.
	 * @param b
	 *            Blue value between 0 and 255.
	 * @param pat
	 *            Pattern value between 0 and 6.
	 * @param rang
	 *            Range value between either 1 or 2.
	 * @author Elijah
	 */
	public void writeLED(int r, int g, int b, Pattern pat, Range range) {
		
		if (range == Range.Hopper) {
			data[0] = (byte) r;
			data[1] = (byte) g;
			data[2] = (byte) b;
			data[3] = (byte) pat.getVal();
		} else {
			data[4] = (byte) r;
			data[5] = (byte) g;
			data[6] = (byte) b;
			data[7] = (byte) pat.getVal();
		}
		i2c.writeBulk(data);
	}

	/**
	 * Takes in a range and turns it's LED's off.
	 * 
	 * @param rang
	 *            Range of the LED's to turn off.
	 * @author Elijah
	 */
	public void off(Range range) {
		writeLED(0, 0, 0, Pattern.Off, range);
	}

	/**
	 * Sets the LED's to a solid color.
	 * 
	 * @param r
	 *            Red value from 0 - 255.
	 * @param g
	 *            Green value from 0 - 255.
	 * @param b
	 *            Blue value form 0 -255.
	 * @param range
	 *            LED range.
	 * @author Elijah
	 */
	public void solid(int r, int g, int b, Range range) {
		writeLED(r, g, b, Pattern.Solid, range);
	}

	/**
	 * When your lights gotta go fast!
	 * 
	 * @param r
	 *            Red value from 0 - 255.
	 * @param g
	 *            Green value from 0 - 255.
	 * @param b
	 *            Blue value form 0 -255.
	 * @param range
	 *            LED range.
	 * @author Elijah
	 */
	public void fastBlink(int r, int g, int b, Range range) {
		writeLED(r, g, b, Pattern.FastBlink, range);
	}

	/**
	 * A slower blink than Fast Blink.
	 * 
	 * @param r
	 *            Red value from 0 - 255.
	 * @param g
	 *            Green value from 0 - 255.
	 * @param b
	 *            Blue value form 0 -255.
	 * @param range
	 *            LED range.
	 * @author Elijah
	 */
	public void slowBlink(int r, int g, int b, Range range) {
		writeLED(r, g, b, Pattern.SlowBlink, range);
	}

	/**
	 * Fades the lights in and out.
	 * 
	 * @param r
	 *            Red value from 0 - 255.
	 * @param g
	 *            Green value from 0 - 255.
	 * @param b
	 *            Blue value form 0 -255.
	 * @param range
	 *            LED range.
	 * @author Elijah
	 */
	public void fade(int r, int g, int b, Range range) {
		writeLED(r, g, b, Pattern.Fade, range);
	}

	/**
	 * Makes the lights chase in towards the middle of the range in the color
	 * chosen.
	 * 
	 * @param r
	 *            Red value from 0 - 255.
	 * @param g
	 *            Green value from 0 - 255.
	 * @param b
	 *            Blue value form 0 -255.
	 * @param range
	 *            LED range.
	 * @author Elijah
	 */
	public void chaseIn(int r, int g, int b, Range range) {
		writeLED(r, g, b, Pattern.Chase, range);
	}

	/**
	 * Makes a pretty rainbow out of all of the LED's on the robot.
	 * 
	 * @author Elijah
	 */
	public void rainbow() {
		writeLED(0, 0, 0, Pattern.Rainbow, Range.Hopper);
		writeLED(0, 0, 0, Pattern.Rainbow, Range.Shooter);
	}

	public void cylon(int r, int g, int b, Range range) {
		writeLED(r, g, b, Pattern.Cylon, range);
	
	}

	
}
