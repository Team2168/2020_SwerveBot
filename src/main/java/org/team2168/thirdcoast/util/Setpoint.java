package org.team2168.thirdcoast.util;

public class Setpoint {

  private final double INITIAL;
  private final double TARGET;
  private final double DELTA;
  private double targetProgress;

  public Setpoint(double initial, double target) {
    this(initial, target, 1.0);
  }

  public Setpoint(double initial, double target, double targetProgress) {
    this.INITIAL = initial;
    this.TARGET = target;
    this.targetProgress = targetProgress;
    DELTA = TARGET - INITIAL;
  }

  public double getSetpoint(double progress) {
    if (progress > 1.0 || progress < 0.0) System.out.println("invalid control loop progress");

    if (progress > targetProgress) {
      return INITIAL + DELTA;
    }

    return INITIAL + DELTA * (progress / targetProgress);
  }
}