package org.team2168.subsystem;

 import edu.wpi.first.wpilibj.command.Subsystem;

 public class DriveWheel extends Subsystem{
    private TalonFX azimuthTalon;
    private BaseTalon driveTalon;
   
    private DriveWheel {
//Creates new instance of wheel
 Wheel wheel = new Wheel(azimuthTalon, driveTalon, 1.0);
    
    }
//Allows for the Azimuth and Speed to be changed
public void set(double azimuth, double drive) {
    if (drive == 0){ 
        driver.accept(0d);
        return;
        }
    }
}