package org.team2168.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.FilterConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team2168.RobotMap;
import org.team2168.commands.drivetrain.DriveWithJoystick;
import org.team2168.thirdcoast.swerve.*;

public class Drivetrain extends Subsystem {
    private Wheel[] _wheels = new Wheel[SwerveDrive.getWheelCount()];
    private final boolean[] DRIVE_INVERTED = {false , false, false, false};
    private final boolean[] ABSOLUTE_ENCODER_INVERTED = {true, true, true, true};
    private final double[] ABSOLUTE_ENCODER_OFFSET = {209.004, 194.678, 83.145, 9.141};
    private SwerveDrive _sd;
    private final boolean ENABLE_DRIVE_CURRENT_LIMIT = true;
    private final double CONTINUOUS_DRIVE_CURRENT_LIMIT = 40.0; // amps
    private final double TRIGGER_DRIVE_THRESHOLD_LIMIT = 60.0; // amps
    private final double TRIGGER_DRIVE_THRESHOLD_TIME = 0.2; // seconds

    private final boolean ENABLE_AZIMUTH_CURRENT_LIMIT = true;
    private final double CONTINUOUS_AZIMUTH_CURRENT_LIMIT = 10.0; // amps
    private final double TRIGGER_AZIMUTH_THRESHOLD_LIMIT = 10.0; // amps
    private final double TRIGGER_AZIMUTH_THRESHOLD_TIME = 0.1; // seconds

    private static Drivetrain instance = null;

    private Drivetrain() {

        // put the zeros for each module to the dashboard
        for (int i = 0; i < SwerveDrive.getWheelCount(); i++) {
            SmartDashboard.putNumber("Abs Zero Module " + i, Preferences.getInstance().getInt(SwerveDrive.getPreferenceKeyForWheel(i), SwerveDrive.DEFAULT_ABSOLUTE_AZIMUTH_OFFSET));
        }

        //_sd.zeroAzimuthEncoders();
        _sd = configSwerve();
    }

    /**
     * @return An instance of the DriveWheel subsystem
     */
    public static Drivetrain getInstance() {
        if (instance == null)
          instance = new Drivetrain();

        return instance;
    }

    /**
     * @return a configured SwerveDrive
     */
    private SwerveDrive configSwerve() {
        TalonFXConfiguration azimuthConfig = new TalonFXConfiguration();
        CANCoderConfiguration azimuthEncoderConfig = new CANCoderConfiguration();
        TalonFXConfiguration driveConfig = new TalonFXConfiguration();
        SupplyCurrentLimitConfiguration driveTalonCurrentLimit, azimuthTalonCurrentLimit;

        driveTalonCurrentLimit = new SupplyCurrentLimitConfiguration(ENABLE_DRIVE_CURRENT_LIMIT,
        CONTINUOUS_DRIVE_CURRENT_LIMIT, TRIGGER_DRIVE_THRESHOLD_LIMIT, TRIGGER_DRIVE_THRESHOLD_TIME);

        azimuthTalonCurrentLimit = new SupplyCurrentLimitConfiguration(ENABLE_AZIMUTH_CURRENT_LIMIT,
        CONTINUOUS_AZIMUTH_CURRENT_LIMIT, TRIGGER_AZIMUTH_THRESHOLD_LIMIT, TRIGGER_AZIMUTH_THRESHOLD_TIME);

        FilterConfiguration azimuthFilterConfig = new FilterConfiguration();
        azimuthFilterConfig.remoteSensorSource = RemoteSensorSource.CANCoder;

        azimuthConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.RemoteSensor0;
        azimuthConfig.remoteFilter0 = azimuthFilterConfig;
        azimuthConfig.slot0.kP = 0.5;
        azimuthConfig.slot0.kI = 0.0;
        azimuthConfig.slot0.kD = 0.0;
        azimuthConfig.slot0.kF = 0.0;
        azimuthConfig.slot0.integralZone = 0;
        azimuthConfig.slot0.allowableClosedloopError = 0; //Wheel.degreesToTicksAzimuth(0.1);
        azimuthConfig.motionAcceleration = Wheel.DPSToTicksPer100msAzimuth(7000); // 10_000;
        azimuthConfig.motionCruiseVelocity = Wheel.DPSToTicksPer100msAzimuth(700); // 800;
        driveConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
        driveConfig.slot0.kP = 0.35; //0.05
        driveConfig.slot0.kI = 0.0005;
        driveConfig.slot0.kD = 0.0;
        driveConfig.slot0.kF = 0.032;  // TODO: tune these
        driveConfig.slot0.integralZone = 1000;
        driveConfig.slot0.maxIntegralAccumulator = 150_000;
        driveConfig.slot0.allowableClosedloopError = 0;
        driveConfig.motionAcceleration = Wheel.DPSToTicksPer100msDW(180); // 500;
        driveConfig.motionCruiseVelocity = Wheel.DPSToTicksPer100msDW(30); // 100;

        azimuthEncoderConfig.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;

        for (int i = 0; i < SwerveDrive.getWheelCount(); i++) {
            azimuthEncoderConfig.magnetOffsetDegrees = ABSOLUTE_ENCODER_OFFSET[i];
            azimuthEncoderConfig.sensorDirection = ABSOLUTE_ENCODER_INVERTED[i];

            CANCoder azimuthEncoder = new CANCoder(RobotMap.CANCODER_ID[i]);
            azimuthEncoder.configAllSettings(azimuthEncoderConfig);

            azimuthFilterConfig.remoteSensorDeviceID = RobotMap.CANCODER_ID[i];

            WPI_TalonFX azimuthTalon = new WPI_TalonFX(RobotMap.AZIMUTH_TALON_ID[i]);
            azimuthTalon.configFactoryDefault();
            azimuthTalon.setInverted(false);
            azimuthTalon.setSensorPhase(false);
            azimuthTalon.configAllSettings(azimuthConfig);
            azimuthTalon.configSupplyCurrentLimit(azimuthTalonCurrentLimit);
            azimuthTalon.setNeutralMode(NeutralMode.Coast);

            WPI_TalonFX driveTalon = new WPI_TalonFX(RobotMap.DRIVE_TALON_ID[i]);
            driveTalon.configFactoryDefault();
            driveTalon.setInverted(DRIVE_INVERTED[i]);
            driveTalon.configAllSettings(driveConfig);
            driveTalon.configSupplyCurrentLimit(driveTalonCurrentLimit);
            driveTalon.setNeutralMode(NeutralMode.Coast);

            Wheel wheel = new Wheel(azimuthTalon, driveTalon);
            _wheels[i] = wheel;

            // set the value of the internal encoder's current position to that of the external encoder,
            // taking into account the gear ratio & difference in resolution, as well as the saved zero
            Preferences prefs = Preferences.getInstance();
            _wheels[i].setAzimuthZero(prefs.getInt(SwerveDrive.getPreferenceKeyForWheel(i), SwerveDrive.DEFAULT_ABSOLUTE_AZIMUTH_OFFSET));

            SmartDashboard.putNumber("Abs position on init, module " + i, wheel.getAzimuthPosition());
        }

        SwerveDriveConfig config = new SwerveDriveConfig();
        config.wheels = _wheels;
        config.gyro = new PigeonIMU(RobotMap.PIGEON_IMU_CAN_ID);
        config.gyro.setYaw(0.0);
        return new SwerveDrive(config);
    }

    /**
     * Drive the robot in given field-relative direction and with given rotation.
     *
     * @param forward Y-axis movement, from -1.0 (reverse) to 1.0 (forward)
     * @param strafe X-axis movement, from -1.0 (left) to 1.0 (right)
     * @param azimuth robot rotation, from -1.0 (CCW) to 1.0 (CW)
     */
    public void drive(double forward, double strafe, double azimuth) {
        _sd.drive(forward, strafe, azimuth);
    }

    public Wheel[] getWheels() {
        return _wheels;
    }

    /**
     * sets the gyros heading (yaw) to 0 degrees.
     */
    public void zeroGyro() {
      _sd.getGyro().setYaw(0.0);
    }

    /**
     *
     * @return the robot's heading (yaw) in degrees. Yaw positively increases in the CW direction TODO: verify this is accurate
     */
    public double getHeading() {
      double ypr_deg[] = new double[3];
      _sd.getGyro().getYawPitchRoll(ypr_deg);
      return -ypr_deg[0];
    }

    /**
     * Set the absolute module heading in terms of the module
     *
     * @param position position in motor ticks
     */
    public void setAzimuth(Wheel wheel, int position) {
        wheel.setAzimuthPosition(position);
    }

    /**
     * Puts azimuth positions in degrees to the SmartDashboard
     */
    public void putAzimuthPositions() {
        Wheel[] wheels = _sd.getWheels();
        for(int i = 0; i < SwerveDrive.getWheelCount(); i++) {
            SmartDashboard.putNumber("Azimuth angle " + i, Wheel.ticksToDegreesAzimuth(wheels[i].getAzimuthPosition()));
        }
    }

    /**
     * Puts external and calculated internal encoder positions to the SmartDashboard
     */
    public void putEncoderPositions() {
        Wheel[] wheels = _sd.getWheels();
        for(int i = 0; i < SwerveDrive.getWheelCount(); i++) {
            SmartDashboard.putNumber("External encoder pos " + i, wheels[i].getAzimuthPosition());
        }
    }

    /**
     * Save the wheels' azimuth current position as read by absolute encoder. These values are saved
     * persistently on the roboRIO and are normally used to calculate the relative encoder offset
     * during wheel initialization.
     *
     * <p>The wheel alignment data is saved in the WPI preferences data store and may be viewed using
     * a network tables viewer.
     *
     * @see #zeroAzimuthEncoders()
     */
    public void saveAzimuthPositions() {
        _sd.saveAzimuthPositions();
    }

    /**
     * Stops all wheels' azimuth and drive movement. Calling this in the robots {@code teleopInit} and
     * {@code autonomousInit} will reset wheel azimuth relative encoders to the current position and
     * thereby prevent wheel rotation if the wheels were moved manually while the robot was disabled.
     */
    public void stop() {
        _sd.stop();
    }


    public void setDriveMode(SwerveDrive.DriveMode mode) {
        _sd.setDriveMode(mode);
      }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveWithJoystick());
    }

}