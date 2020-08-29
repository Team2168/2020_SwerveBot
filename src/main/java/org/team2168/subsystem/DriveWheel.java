package org.team2168.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;

import org.team2168.thirdcoast.swerve.Wheel;

import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveWheel extends Subsystem {
    private TalonFX azimuthTalon;
    private TalonFX driveTalon;
    Wheel wheel;
    private final boolean ENABLE_CURRENT_LIMIT = true;
    private final double CONTINUOUS_CURRENT_LIMIT = 40; //amps
    private final double TRIGGER_THRESHOLD_LIMIT = 60; //amp
    private final double TRIGGER_THRESHOLD_TIME = 0.2; //s

    private DriveWheel() {
        TalonFXConfiguration azimuthConfig = new TalonFXConfiguration();
        TalonFXConfiguration driveConfig = new TalonFXConfiguration();
        SupplyCurrentLimitConfiguration talonCurrentLimit;

        talonCurrentLimit = new SupplyCurrentLimitConfiguration(ENABLE_CURRENT_LIMIT,
        CONTINUOUS_CURRENT_LIMIT, TRIGGER_THRESHOLD_LIMIT, TRIGGER_THRESHOLD_TIME);

        azimuthConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.CTRE_MagEncoder_Relative;
        azimuthConfig.slot0.kP = 10.0;
        azimuthConfig.slot0.kI = 0.0;
        azimuthConfig.slot0.kD = 100.0;
        azimuthConfig.slot0.kF = 0.0;
        azimuthConfig.slot0.integralZone = 0;
        azimuthConfig.slot0.allowableClosedloopError = 0;
        azimuthConfig.motionAcceleration = 10_000;
        azimuthConfig.motionCruiseVelocity = 800;
        driveConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.CTRE_MagEncoder_Relative;
        // for (int i = 0; i < 4; i++) {
            TalonFX azimuthTalon = new TalonFX(i);
            azimuthTalon.configAllSettings(azimuthConfig);
            azimuthTalon.configSupplyCurrentLimit(talonCurrentLimit);
 
            TalonFX drivttalon = new TalonFX(i + 10);
            driveTalon.configAllSettings(driveConfig);
            driveTalon.configSupplyCurrentLimit(talonCurrentLimit);
        // return;         
        // }

        wheel = new Wheel(azimuthTalon, driveTalon, 1.0);
    }

    public void set(double azimuth, double drive) {
        if (drive == 0){ 
            driver.accept(0d);
            return;
        }
    }

    //Allows for the Azimuth and Speed to be changed
    // private Wheel[] getWheels() {
        // you'll make us proud some day
    // }

    @Override
    protected void initDefaultCommand() {
	// TODO Auto-generated method stub
	
    }

}