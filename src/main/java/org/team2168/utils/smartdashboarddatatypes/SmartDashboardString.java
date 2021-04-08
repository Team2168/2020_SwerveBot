package org.team2168.utils.smartdashboarddatatypes;

import java.text.MessageFormat;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * SmartDashboardString is a helper class for getting values from Smart Dashboard
 */
public class SmartDashboardString {
    private String key;
    private String value;
    private boolean writable; // This might be useful so values don't get overwritten


    public SmartDashboardString(String key, String value, boolean writable) {
        this.key = key;
        this.value = value;
        this.writable = writable;

        if (!SmartDashboard.containsKey(this.key))
            SmartDashboard.putString(this.key, this.value);
        
        if (this.writable)
        System.out.println(MessageFormat.format("WARNING: Variable {0} is writable.", this.key));
    }

    public SmartDashboardString(String key) {
        this(key, "", true);
    }

    public String get() {
        if (writable)
            value = SmartDashboard.getString(key, "");
        return value;
    }

    public String getKey() {
        return key;
    }

    public boolean isWritable() {
        return writable;
    }

    public void set(String value) {
        if (writable)
            this.value = value;
        else
            DriverStation.reportError("Cannot write to read only value!", true);
        SmartDashboard.putString(key, value);
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }
}