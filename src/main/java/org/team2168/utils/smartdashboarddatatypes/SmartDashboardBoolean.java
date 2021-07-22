package org.team2168.utils.smartdashboarddatatypes;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardBoolean extends SmartDashboardObject<Boolean> {
    public SmartDashboardBoolean(String key, Boolean value) {
        super(key, value);
    }

    public SmartDashboardBoolean(String key) {
        this(key, false);
    }

    public Boolean get() {
        return SmartDashboard.getBoolean(key, false);
    }

    public void set(Boolean value) {
        SmartDashboard.putBoolean(key, value);
    }
    
}
