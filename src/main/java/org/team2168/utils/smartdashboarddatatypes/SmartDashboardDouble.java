package org.team2168.utils.smartdashboarddatatypes;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**SmartDashboardDouble is a helper class for getting values from Smart Dashboard.
 * 
 * This is useful in situations like tuning PID, where a SmartDashboardDouble would be used instead of
 * a normal double to avoid having to recompile to change a variable.
 */
public class SmartDashboardDouble extends SmartDashboardObject<Double> {
    private String key;

    public SmartDashboardDouble(String key, Double value) {
        super(key, value);
    }

    public SmartDashboardDouble(String key) {
        this(key, 0.0);
    }

    public Double get() {
        return (Double) SmartDashboard.getNumber(key, 0.0);
    }

    public void set(Double value) {
        SmartDashboard.putNumber(key, value);
    }

}