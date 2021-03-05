package org.team2168.thirdcoast.motion;

import edu.wpi.first.wpilibj.Notifier;
import org.team2168.subsystem.Drivetrain;
import org.team2168.thirdcoast.util.Setpoint;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team2168.thirdcoast.swerve.SwerveDrive;
import org.team2168.thirdcoast.swerve.Wheel;

public class PathController implements Runnable {

  private static final int NUM_WHEELS = 4;
  private static final double TICKS_PER_FOOT = Wheel.TICKS_PER_FOOT_DW;
  private static final Drivetrain DRIVE = Drivetrain.getInstance();

  @SuppressWarnings("FieldCanBeLocal")
  private static final double yawKp = 0.01; // 0.03

  private static final double percentToDone = 0.50;
  private static final double DT = 0.04;

  private static final double MIN_VEL = 45.0;
  private static final double MIN_START = 40.0;
  //  private static final double RATE_CAP = 0.35;
  //  private static final RateLimit rateLimit = new RateLimit(0.015);
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final int PID = 0;
  private Trajectory trajectory;
  private Notifier notifier;
  private Wheel[] wheels;
  private States state;
  private double maxVelocityFtSec;
  private double yawDelta;
  private int iteration;
  private int[] start;
  private Setpoint setpoint;
  private double setpointPos;
  private double yawError;
  private boolean isDriftOut;

  public PathController(String pathName, double yawDelta, boolean isDriftOut) {
    this.yawDelta = yawDelta;
    this.isDriftOut = isDriftOut;
    wheels = DRIVE.getWheels();
    File csvFile = new File("home/lvuser/deploy/paths/" + pathName + ".pf1.csv");

    trajectory = new Trajectory(csvFile);
  }

  public void start() {
    start = new int[4];
    notifier = new Notifier(this);
    notifier.startPeriodic(DT);
    state = States.STARTING;
  }

  public boolean isFinished() {
    return state == States.STOPPED;
  }

  @Override
  public void run() {

    switch (state) {
      case STARTING:
        logState();
        double ticksPerSecMax = Wheel.getDriveSetpointMax() * 10.0;
        maxVelocityFtSec = ticksPerSecMax / (TICKS_PER_FOOT / 132);
        iteration = 0;
        DRIVE.setDriveMode(SwerveDrive.DriveMode.CLOSED_LOOP);

        for (int i = 0; i < NUM_WHEELS; i++) {
          start[i] = (int) wheels[i].getDriveTalon().getSelectedSensorPosition(PID);
        }

        double currentAngle = DRIVE.getGyro().getAngle();
        setpoint = new Setpoint(currentAngle, yawDelta, percentToDone);

        logInit();
        state = States.RUNNING;
        break;
      case RUNNING:
        if (iteration == trajectory.length() - 1) {
          state = States.STOPPING;
        }

        Trajectory.Segment segment = trajectory.getIteration(iteration);

        double currentProgress = iteration / (double) trajectory.length();

        double segmentVelocity = segment.velocity;
        if (isDriftOut && currentProgress < percentToDone && segment.velocity < MIN_START) {
          segmentVelocity = MIN_START;
        }

        double setpointVelocity = segmentVelocity / maxVelocityFtSec;

        double forward = Math.cos(segment.heading) * setpointVelocity;
        double strafe = Math.sin(segment.heading) * setpointVelocity;

        if (currentProgress > percentToDone && segment.velocity < MIN_VEL) {
          state = States.STOPPING;
        }

        setpointPos = setpoint.getSetpoint(currentProgress);

        yawError = setpointPos - DRIVE.getGyro().getAngle();
        double yaw;

        yaw = yawError * yawKp;

        if (forward > 1d || strafe > 1d) logger.warn("forward = {} strafe = {}", forward, strafe);

        DRIVE.drive(forward, strafe, yaw);
        iteration++;
        break;
      case STOPPING:
        DRIVE.setDriveMode(SwerveDrive.DriveMode.OPEN_LOOP);
        logState();
        state = States.STOPPED;
        break;
      case STOPPED:
        logState();
        notifier.close();
        break;
    }
  }

  private void logState() {
    logger.info("{}", state);
  }

  private void logInit() {
    logger.info(
        "Path start yawKp = {} yawDelta = {} maxVelocity in/s = {}",
        yawKp,
        yawDelta,
        maxVelocityFtSec);
  }

  public double getYawError() {
    return yawError;
  }

  public double getSetpointPos() {
    return setpointPos;
  }

  public void interrupt() {
    logger.info("interrupted");
    state = States.STOPPED;
  }
}