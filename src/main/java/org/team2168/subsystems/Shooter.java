package org.team2168.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import org.team2168.Gains;
import org.team2168.Robot;
import org.team2168.RobotMap;
import org.team2168.commands.shooter.DriveShooterWithJoystick;
import org.team2168.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem {

    private TalonFX _motorOne;
    private TalonFX _motorTwo;

    private FiringLocation _firingLocation;

    // private final boolean _motorOneReversed = false;
    // private final boolean _motorTwoReversed = false;

    private StatorCurrentLimitConfiguration talonCurrentLimitStator;
    private final boolean ENABLE_CURRENT_LIMIT_STATOR = true;
    private final double CONTINUOUS_CURRENT_LIMIT_STATOR = 60; //amps
    private final double TRIGGER_THRESHOLD_LIMIT_STATOR = 70; //amp
    private final double TRIGGER_THRESHOLD_TIME_STATOR = 100; //ms

    private SupplyCurrentLimitConfiguration talonCurrentLimitSupply;
    private final boolean ENABLE_CURRENT_LIMIT_SUPPLY = true;
    private final double CONTINUOUS_CURRENT_LIMIT_SUPPLY = 45; //amps
    private final double TRIGGER_THRESHOLD_LIMIT_SUPPLY = 50; //amp
    private final double TRIGGER_THRESHOLD_TIME_SUPPLY = 0.2; //s

    private static Shooter _instance;

    /**
     * Which PID slot to pull gains from. Starting 2018, you can choose from
     * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
     * configuration.
     */
    public static final int kSlotIdx = 0;

    /**
     * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
     * now we just want the primary one.
     */
    public static final int kPIDLoopIdx = 0;

    /**
     * Set to zero to skip waiting for confirmation, set to nonzero to wait and
     * report to DS if action fails.
     */
    public static final int kTimeoutMs = 30;

    public static final double max_velocity = 8000.0; //TODO set (measured ~18,000 units/1000ms at full stick)

    /** Invert Directions for Left and Right */
    TalonFXInvertType _motorOneInvert = TalonFXInvertType.CounterClockwise;
    TalonFXInvertType _motorTwoInvert = TalonFXInvertType.Clockwise;

    /** Config Objects for motor controllers */
    TalonFXConfiguration _leftConfig = new TalonFXConfiguration();
    TalonFXConfiguration _rightConfig = new TalonFXConfiguration();

    private static final double TICKS_PER_REV = 2048.0; //one event per edge on each quadrature channel
    private static final double TICKS_PER_100MS = TICKS_PER_REV / 10.0;
    private static final double GEAR_RATIO = 18.0/24.0;  // motor pulley/shooter wheel pulley
    private static final double SECS_PER_MIN = 60.0;

    /**
     * PID Gains may have to be adjusted based on the responsiveness of control loop.
     * kF: 1023 represents output value to Talon at 100%, 7200 represents Velocity units at 100% output
     *
     * 	                                      kP    kI    kD          kF               Iz   PeakOut
     */
    final Gains kGains_Velocity = new Gains( 1.0, 0.0005, 0.0, 0.52*1023.0/11205.0,  300,  1.00); // kF = 75% * 1023.0 / max_vel in sensor ticks, kF = 1.00*1023.0/19225.0--better, kP = 0.5
    //in process--kP = 0.8, kF = 0.52*1023.0/10894.0
    private double setPointVelocity_sensorUnits;

    private final double WALL_VEL = 1490.0;
    private final double WHITE_LINE_VEL = 2140.0;
    private final double FRONT_TRENCH_VEL = 3250.0;
    private final double BACK_TRENCH_VEL = 3340.0;

    private final double WALL_VEL_PBOT = 2540.0; //new red balls
    private final double WHITE_LINE_VEL_PBOT = 3300.0; //
    private final double FRONT_TRENCH_VEL_PBOT = 3900.0; //
    private final double BACK_TRENCH_VEL_PBOT = 4540.0; //

    private static double _wallVel;
    private static double _whiteLineVel;
    private static double _frontTrenchVel;
    private static double _backTrenchVel;
    private static double velocityAdjustment = -100.0;

    private Shooter() {
        _motorOne = new TalonFX(RobotMap.SHOOTER_1_CAN_ID);
        _motorTwo = new TalonFX(RobotMap.SHOOTER_2_CAN_ID);

        /* Factory Default all hardware to prevent unexpected behaviour */
        _motorOne.configFactoryDefault();
        _motorTwo.configFactoryDefault();

        talonCurrentLimitStator = new StatorCurrentLimitConfiguration(ENABLE_CURRENT_LIMIT_STATOR,
        CONTINUOUS_CURRENT_LIMIT_STATOR, TRIGGER_THRESHOLD_LIMIT_STATOR, TRIGGER_THRESHOLD_TIME_STATOR);

        _motorOne.configStatorCurrentLimit(talonCurrentLimitStator);
        _motorTwo.configStatorCurrentLimit(talonCurrentLimitStator);

        talonCurrentLimitSupply = new SupplyCurrentLimitConfiguration(ENABLE_CURRENT_LIMIT_SUPPLY,
        CONTINUOUS_CURRENT_LIMIT_SUPPLY, TRIGGER_THRESHOLD_LIMIT_SUPPLY, TRIGGER_THRESHOLD_TIME_SUPPLY);
        _motorOne.configSupplyCurrentLimit(talonCurrentLimitSupply);
        _motorTwo.configSupplyCurrentLimit(talonCurrentLimitSupply);

        _motorOne.setNeutralMode(NeutralMode.Coast);
        _motorTwo.setNeutralMode(NeutralMode.Coast);

        /* Configure output and sensor direction */
        _motorOne.setInverted(_motorOneInvert);
        _motorTwo.setInverted(_motorTwoInvert);

        //set second motor as a follower
        _motorTwo.follow(_motorOne, FollowerType.PercentOutput);

        /* Config neutral deadband to be the smallest possible */
        _motorOne.configNeutralDeadband(0.01);

        /* Config sensor used for Primary PID [Velocity] */
        _motorOne.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor,
                                            kPIDLoopIdx,
                                            kTimeoutMs);


        /* Config the peak and nominal outputs */
        _motorOne.configNominalOutputForward(0.0, kTimeoutMs);
        _motorOne.configNominalOutputReverse(0.0, kTimeoutMs);
        _motorOne.configPeakOutputForward(1.0, kTimeoutMs);
        _motorOne.configPeakOutputReverse(0.0, kTimeoutMs); //set so that the shooter CANNOT run backwards

        /* Config the Velocity closed loop gains in slot0 */
        _motorOne.config_kF(kPIDLoopIdx, kGains_Velocity.kF, kTimeoutMs);
        _motorOne.config_kP(kPIDLoopIdx, kGains_Velocity.kP, kTimeoutMs);
        _motorOne.config_kI(kPIDLoopIdx, kGains_Velocity.kI, kTimeoutMs);
        _motorOne.config_kD(kPIDLoopIdx, kGains_Velocity.kD, kTimeoutMs);
        _motorOne.config_IntegralZone(kPIDLoopIdx, kGains_Velocity.kIzone, kTimeoutMs);
        /*
         * Talon FX does not need sensor phase set for its integrated sensor
         * This is because it will always be correct if the selected feedback device is integrated sensor (default value)
         * and the user calls getSelectedSensor* to get the sensor's position/velocity.
         *
         * https://phoenix-documentation.readthedocs.io/en/latest/ch14_MCSensor.html#sensor-phase
         */
        // _motorOne.setSensorPhase(true);

        if(Robot.isPracticeBot()) {
            _wallVel = WALL_VEL_PBOT;
            _whiteLineVel = WHITE_LINE_VEL_PBOT;
            _frontTrenchVel = FRONT_TRENCH_VEL_PBOT;
            _backTrenchVel = BACK_TRENCH_VEL_PBOT;
        } else {
            _wallVel = WALL_VEL;
            _whiteLineVel = WHITE_LINE_VEL;
            _frontTrenchVel = FRONT_TRENCH_VEL;
            _backTrenchVel = BACK_TRENCH_VEL;
        }

        _firingLocation = FiringLocation.WALL;


        ConsolePrinter.putNumber("Shooter Velocity", () -> {return getVelocity();}, true, false);
        ConsolePrinter.putNumber("Shooter Error", () -> {return getError();}, true, false);
        ConsolePrinter.putNumber("Shooter Motor Output Percent", () -> {return _motorOne.getMotorOutputPercent();}, true, false);
        ConsolePrinter.putNumber("Velocity Adjust", () -> {return velocityAdjustment;}, true, false);
        //ConsolePrinter.putNumber("Shooter Setpoint", () -> {return ticks_per_100ms_to_revs_per_minute( _motorOne.getClosedLoopTarget());}, true, false);
    }

    /**
     * Creates a new Instance of the shooter
     * @return - New shooter instance will be utilized by the commands
     */
    public static Shooter getInstance() {
        if (_instance == null) {
            _instance = new Shooter();
        }
        return _instance;
    }

    /**
     * Sets the speed (%Vbus) of the shooter motors
     *
     * @param speed 1.0 to -1.0, positive is out of the robot
     */
    public void driveShooterMotors(double speed)
    {
        _motorOne.set(ControlMode.PercentOutput, speed);
        //driveShooterMotorTwo(speed); //Not needed this is configured as a follower
    }

    public double getVelocity()
    {
        return ticks_per_100ms_to_revs_per_minute(_motorOne.getSelectedSensorVelocity(kPIDLoopIdx));
    }

    /**
     * Sets the closed loop shooter speed.
     *
     * @param setPoint speed in RPM
     */
    public void setSpeed(double setPoint)
    {
        if(Robot.isAutoMode()) {
            setPointVelocity_sensorUnits = revs_per_minute_to_ticks_per_100ms(setPoint) ;
        } else {
            setPointVelocity_sensorUnits = revs_per_minute_to_ticks_per_100ms(setPoint + velocityAdjustment);
        }
        _motorOne.set(ControlMode.Velocity, setPointVelocity_sensorUnits);
    }

    public void incrementSpeed() {
        velocityAdjustment += 50.0;
    }

    public void decrementSpeed() {
        velocityAdjustment -= 50.0;
    }

    public void zeroSpeed() {
        velocityAdjustment = 0.0;
    }

    public double getError()
    {
        return ticks_per_100ms_to_revs_per_minute(_motorOne.getClosedLoopError(kPIDLoopIdx)) ;
    }

    /**
     * Converts RPM to sensor ticks per 100ms
     *
     * @param revs speed (RPM) to convert to ticks/100ms
     */
    private double revs_per_minute_to_ticks_per_100ms(double revs) {
        return (revs / SECS_PER_MIN) * GEAR_RATIO * TICKS_PER_100MS;
    }

    /**
     * Convert speed in motor units per 100ms to RPM
     *
     * @param ticks speed (ticks/100ms) to convert to RPM
     */
    private double ticks_per_100ms_to_revs_per_minute(double ticks) {
        //TODO: Verify conversion is correct
        return ticks * SECS_PER_MIN / (GEAR_RATIO * TICKS_PER_100MS);
    }

    public enum FiringLocation {
        WALL(_wallVel),
        WHITE_LINE(_whiteLineVel),
        FRONT_TRENCH(_frontTrenchVel),
        BACK_TRENCH(_backTrenchVel);

        public double getSpeed(){
            return _speed;
        }

        private final double _speed;

        private FiringLocation(double speed) {
            this._speed = speed;
        }
    }

    public FiringLocation getFiringLocation() {
        return _firingLocation;
    }

    public void setFiringLocation(FiringLocation fl) {
        _firingLocation = fl;
    }

    public void initDefaultCommand() {
        setDefaultCommand(new DriveShooterWithJoystick());
    }
}