package org.team2168.utils.smartdashboarddatatypes;

import java.text.MessageFormat;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**SmartDashboardDouble is a helper class for getting values from Smart Dashboard.
 * 
 * This is useful in situations like tuning PID, where a SmartDashboardDouble would be used instead of
 * a normal double to avoid having to recompile to change a variable.
 */
public class SmartDashboardDouble {
    private String key;
    private double value;
    private boolean writable; // This might be useful so values don't get overwritten

    public SmartDashboardDouble(String key, double value, boolean writable) {
        this.key = key;
        this.value = value;
        this.writable = writable;

        if (!SmartDashboard.containsKey(this.key))
            SmartDashboard.putNumber(this.key, this.value);

        if (this.writable)
            System.out.println(MessageFormat.format("WARNING: Variable {0} is writable.", this.key));
    }

    public SmartDashboardDouble(String key) {
        this(key, 0.0, true);
    }

    public double get() {
        if (writable)
            value = (double) SmartDashboard.getNumber(key, 0.0);
        return value;
    }

    public String getKey() {
        return key;
    }

    public boolean isWritable() {
        return writable;
    }

    public void set(double value) {
        if (writable)
            this.value = value;
        else
            DriverStation.reportError("Cannot write to read only value!", true);
        SmartDashboard.putNumber(key, value);
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }
}