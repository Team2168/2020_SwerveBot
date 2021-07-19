package org.team2168.utils.smartdashboarddatatypes;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * SmartDashboardObject is an abstract class to make it easier to use types as smart dashboard variables
 */
abstract class SmartDashboardObject<T> {
    protected String key;

    public SmartDashboardObject(String key, T value) {
        this.key = key;

        if (!SmartDashboard.containsKey(key))
            set(value);
    }

    abstract T get();

    public String getKey() {
        return key;
    }

    abstract void set(T value);
}