package org.team2168.utils.smartdashboarddatatypes;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**SmartDashboardDouble is a helper class for getting values from Smart Dashboard.
 * 
 * This is useful in situations like tuning PID, where a SmartDashboardDouble would be used instead of
 * a normal double to avoid having to recompile to change a variable.
 */
public class SmartDashboardDouble {
    private String key;

    public SmartDashboardDouble(String key, double value) {
        this.key = key;

        if (!SmartDashboard.containsKey(key))
            SmartDashboard.putNumber(key, value);
    }

    public SmartDashboardDouble(String key) {
        this(key, 0.0);
    }

    public double get() {
        return (double) SmartDashboard.getNumber(key, 0.0);
    }

    public String getKey() {
        return key;
    }

    public void set(double value) {
        SmartDashboard.putNumber(key, value);
    }
}