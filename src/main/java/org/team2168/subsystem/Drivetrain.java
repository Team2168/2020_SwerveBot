package org.team2168.subsystem;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifierStatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;

import org.team2168.commands.drivetrain.DriveWithJoystick;
import org.team2168.thirdcoast.swerve.*;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Drivetrain extends Subsystem {
    private CANifier _canifier = new CANifier(00);
    private Wheel[] wheels = new Wheel[4];
    private SwerveDrive sd;
    private final boolean ENABLE_CURRENT_LIMIT = true;
    private final double CONTINUOUS_CURRENT_LIMIT = 40; //amps
    private final double TRIGGER_THRESHOLD_LIMIT = 60; //amp
    private final double TRIGGER_THRESHOLD_TIME = 0.2; //s

    private static Drivetrain instance = null;

    private Drivetrain() {
        _canifier.setStatusFramePeriod(CANifierStatusFrame.Status_3_PwmInputs0, 10);
        _canifier.setStatusFramePeriod(CANifierStatusFrame.Status_4_PwmInputs1, 10);
        _canifier.setStatusFramePeriod(CANifierStatusFrame.Status_5_PwmInputs2, 10);
        _canifier.setStatusFramePeriod(CANifierStatusFrame.Status_6_PwmInputs3, 10);
        
        TalonFXConfiguration azimuthConfig = new TalonFXConfiguration();
        TalonFXConfiguration driveConfig = new TalonFXConfiguration();
        SupplyCurrentLimitConfiguration talonCurrentLimit;

        talonCurrentLimit = new SupplyCurrentLimitConfiguration(ENABLE_CURRENT_LIMIT,
        CONTINUOUS_CURRENT_LIMIT, TRIGGER_THRESHOLD_LIMIT, TRIGGER_THRESHOLD_TIME);
        
        // TODO: Set up gear ratios, at least for the driveTalon
        // TODO: Check if we need to set/configure any canifier settings


        azimuthConfig.remoteFilter0.remoteSensorDeviceID = _canifier.getDeviceID();
        azimuthConfig.remoteFilter0.remoteSensorSource = RemoteSensorSource.CANifier_PWMInput1;
        // azimuthConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.RemoteSensor0;
        azimuthConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
        // a possible workaround to get the remote sensor value?
        azimuthConfig.auxiliaryPID.selectedFeedbackSensor = FeedbackDevice.RemoteSensor0;
        azimuthConfig.slot0.kP = 0.075;
        azimuthConfig.slot0.kI = 0.0;
        azimuthConfig.slot0.kD = 0.0;
        azimuthConfig.slot0.kF = 0.0;
        azimuthConfig.slot0.integralZone = 0;
        azimuthConfig.slot0.allowableClosedloopError = Wheel.degreesToTicksAzimuth(0.1);
        azimuthConfig.motionAcceleration = Wheel.DPSToTicksPer100msAzimuth(7000); //10_000;
        azimuthConfig.motionCruiseVelocity = Wheel.DPSToTicksPer100msAzimuth(700); //800;
        driveConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
        driveConfig.motionAcceleration = Wheel.DPSToTicksPer100msDW(180); //500;
        driveConfig.motionCruiseVelocity = Wheel.DPSToTicksPer100msDW(30); //100;

        // TODO: Add closed loop control parameters / configuration for the drive motor. Probably need it for auto modes at some point.

        for (int i = 0; i < 4; i++) {
            TalonFX azimuthTalon = new TalonFX(11 + i);
            azimuthTalon.configFactoryDefault();
            azimuthTalon.setInverted(true);
            azimuthTalon.setSensorPhase(true);
            azimuthTalon.configAllSettings(azimuthConfig);
            azimuthTalon.configSupplyCurrentLimit(talonCurrentLimit);
 
            TalonFX driveTalon = new TalonFX(1 + i);
            driveTalon.configFactoryDefault();
            driveTalon.configAllSettings(driveConfig);
            driveTalon.configSupplyCurrentLimit(talonCurrentLimit);

            Wheel wheel = new Wheel(azimuthTalon, driveTalon, 1.0);
            wheels[i] = wheel;
            initializeAzimuthPosition(wheel);
        }

        SwerveDriveConfig config = new SwerveDriveConfig();
        config.wheels = wheels;
        sd = new SwerveDrive(config);
    }

    
  /**
   * @returns An instance of the DriveWheel subsystem
   */
    public static Drivetrain getInstance() {
        if (instance == null)
          instance = new Drivetrain();
    
        return instance;
      }

    /**
     * Drive the robot in given field-relative direction and with given rotation.
     *
     * @param forward Y-axis movement, from -1.0 (reverse) to 1.0 (forward)
     * @param strafe X-axis movement, from -1.0 (left) to 1.0 (right)
     * @param azimuth robot rotation, from -1.0 (CCW) to 1.0 (CW)
     */
    public void drive(double forward, double strafe, double azimuth) {
        sd.drive(forward, strafe, azimuth);
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
     * Stop azimuth and drive movement
     */
    public void stop() {
        for(int i = 0; i < 4; i++)
            wheels[i].stop();
    }

    /**
     * Sets the given azimuth internal encoder's current position to that of the corresponding external encoder,
     * taking difference in resolution and gear ratio into account
     * 
     * @param wheel wheel to initialize
     */
    public void initializeAzimuthPosition(Wheel wheel) {
        int position = wheel.getExternalEncoderPos();
        wheel.setAzimuthInternalEncoderPosition(position);
    }

    /**
     * Sets all azimuth internal encoders' current positions to those of the corresponding external encoders,
     * taking difference in resolution and gear ratio into account
     */
    public void initializeAzimuthPosition() {
        int position;
        for(int i = 0; i < 4; i++) {
            position = wheels[i].getExternalEncoderPos();
            wheels[i].setAzimuthInternalEncoderPosition(position);
        }
    }

    //Allows for the Azimuth and Speed to be changed
    // private Wheel[] getWheels() {
        // you'll make us proud some day
    // }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveWithJoystick());
    }

}