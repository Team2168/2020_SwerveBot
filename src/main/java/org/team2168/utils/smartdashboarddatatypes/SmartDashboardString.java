package org.team2168.utils.smartdashboarddatatypes;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * SmartDashboardString is a helper class for getting values from Smart Dashboard
 */
public class SmartDashboardString extends SmartDashboardObject<String> {
    public SmartDashboardString(String key, String value) {
        super(key, value);
    }

    public SmartDashboardString(String key) {
        this(key, "");
    }

    public String get() {
        return SmartDashboard.getString(key, "");
    }

    public void set(String value) {
        SmartDashboard.putString(key, value);
    }
}