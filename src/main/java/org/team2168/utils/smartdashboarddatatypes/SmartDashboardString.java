package org.team2168.utils.smartdashboarddatatypes;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * SmartDashboardString is a helper class for getting values from Smart Dashboard
 */
public class SmartDashboardString {
    private String key;


    public SmartDashboardString(String key, String value) {
        this.key = key;

        if (!SmartDashboard.containsKey(key))
            SmartDashboard.putString(key, value);
    }

    public SmartDashboardString(String key) {
        this(key, "");
    }

    public String get() {
        return SmartDashboard.getString(key, "");
    }

    public String getKey() {
        return key;
    }

    public void set(String value) {
        SmartDashboard.putString(key, value);
    }
}