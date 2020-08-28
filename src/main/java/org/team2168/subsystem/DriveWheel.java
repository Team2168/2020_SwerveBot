package org.team2168.subsystem;

import org.team2168.thirdcoast.swerve.Wheel;

import edu.wpi.first.wpilibj.command.Subsystem;

 public class DriveWheel extends Subsystem{
    private TalonFX azimuthTalon;
    private TalonFX driveTalon;
    Wheel wheel = new Wheel(azimuthTalon, driveTalon, 1.0);
    private DriveWheel {

    }
  
      }

public void set(double azimuth, double drive) {
    if (drive == 0){ 
        driver.accept(0d);
        return;
        }
    }
//Allows for the Azimuth and Speed to be changed
private WheelDrive getWheels() { 
TalonFXConfigutation azimuthConfig = new TalonFXConfigutation();
TalonFXConfigutation.driveConfig = new TalonFXConfigutation();
azimuthConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.CTRE_MagEncoder_Relative;
azimuthConfig.continuousCurrentLimit = 10;
azimuthConfig.peakCurrentDuration = 0;
azimuthConfig.peakCurrentLimit = 0;
azimuthConfig.slot0.kp = 10.0;
azimuthConfig.slot0.kI = 0.0;
azimuthConfig.slot0.kD = 100.0;
azimuthConfig.slot0.kF = 0.0;
azimuthConfig.slot0.integralZone = 0;
azimuthConfig.slot0.allowableClosedloopError = 0;
azimuthConfig.motionAcceleration = 10_000;
azimuthConfig.motionCruiseVelocity = 800;
driveConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.CTRE_MagEncoder_Relative;
driveConfig.continuousCurrentLimit = 40;
driveConfig.peakCurrentDuration = 0;
driveConfig.peakCurrentLimit = 0;
    for (int i = 0; i < 4; i++) {
         TalonFX azimuthTalon = new TalonFX(i);
         azimuthTalon.configAllSettings(azimuthConfig); 
         TalonFX drivttalon = new TalonFX(i + 10);
         driveTalon.configAllSettings(driveConfig);


    return;         
     
@Override
protected void initDefaultCommand() {
	// TODO Auto-generated method stub
	
}
}
    }

}