/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import io.github.oblarg.oblog.annotations.Config;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final int kSlotIdx = 0;
    public static final int kPIDLoopIdx = 0;
    public static final int kTimeoutMs = 30;

    /**
	 * How many sensor units per rotation.
	 * Using Talon FX Integrated Sensor.
	 * @link https://github.com/CrossTheRoadElec/Phoenix-Documentation#what-are-the-units-of-my-sensor
	 */
	public final static int kSensorUnitsPerRotation = 2048;
	
	// This is a property of the Pigeon IMU, and should not be changed.
    public final static int kPigeonUnitsPerRotation = 8192;
    public final static boolean kGyroReversed = false;
    
	// Motor neutral dead-band, set to the minimum 0.1%.
    public final static double kNeutralDeadband = 0.001;
    
    public final static double kLowGearMotorRotationsPerWheelRotation = 20.8333;
    public final static double kInchesPerFoot = 12.0;
    public final static double kRotationsPerInch = 1 / (2 * Math.PI * 3);
    public final static double kSensorUnitsPerFoot = Constants.kInchesPerFoot
        * Constants.kRotationsPerInch
        * Constants.kLowGearMotorRotationsPerWheelRotation
        * Constants.kSensorUnitsPerRotation;
	
	/**
	 * PID Gains may have to be adjusted based on the responsiveness of control loop.
     * kF: 1023 represents output value to Talon at 100%, 6800 represents Velocity units at 100% output
     * Not all set of Gains are used in this project and may be removed as desired.
     * 
	 * 	                                    			  kP   kI   kD   kF               Iz    PeakOut */
	public final static Gains kGains_Distance = new Gains( 0.1, 0.0,  0.0, 0.0,            100,  0.50 );
    public final static Gains kGains_Turning = new Gains( 2.0, 0.0,  4.0, 0.0,            200,  1.00 );
    private final static double kGains_Pigeon_kP = 0.025;
    public final static Gains kGains_Pigeon = new Gains( kGains_Pigeon_kP
        , 0.0
        , kGains_Pigeon_kP / 10.0
        , 0.0
        , 200
        , 1.00 );

    public final static double kTurnClamp = 1.0;
    public final static double kTurnMinimumSpeed = 0.1;//0.07
    public final static double kDriveMinimumSpeed = 0.15;
    public final static double kTurnAcceptableErrorDegrees = 0.5;
    public final static double kAimToTargetAcceptableErrorDegrees = 0.5;

    public final static Gains kGains_Limelight_Turn = new Gains(0.03, 0.0, 0.003, 0.0, 0, 0);
    public final static double kLimelightAcceptableErrorDegrees = 0.5;

    // Constants for accelerator PID
    public static final double acceleratorP = 0.0001;
    public static final double acceleratorI = 0;
    public static final double acceleratorD = 2;
    public static final double acceleratorF = 0.00017;
  
    // Constants for flywheel PID
    public static final double flywheelP = 0.38;
    public static final double flywheelI = 0;
    public static final double flywheelD = 14.5;
    public static final double flywheelF = 0.052;

    // Setpoint for the shooter
    public static final int acceleratorSetpoint = 4200;
    public static final int flywheelSetpoint = 17000;
    public static final int autoShotFlywheelSetpoint = 17000;
    public static final int batterShotFlywheelSetpoint = 8600;

    // Setpoint for auto shot distance
    public static final int autoShotVerticalAngle = 15;


    // Drivetrain w/ CTRE Falcon 500s/TalonFXs
    public static final int driveLeftLeaderFalconID = 4;
    public static final int driveLeftFollowerFalconID = 3;
    public static final int driveRightLeaderFalconID = 2;
    public static final int driveRightFollowerFalconID = 1;
    // Shifter
    public static final int shifterSolenoidForward = 4;
    public static final int shifterSolenoidReverse = 5;
    // Direction
    public static final double drivetrainForward = -1;
    public static final double drivetrainBackward = 1;

    // Pigeon
    public static final int pigeonCanID = 12;

    // Shooter w/ CTRE Falcon 500s/TalonFXs and Rev Robotics SparkMAX/NEO
    public static final int flywheelLeaderTalonID = 10;
    public static final int flywheelFollowerTalonID = 9;
    public static final int acceleratorSparkID = 8;
    // Shooter Hood
    public static final int shooterHoodSolenoidForward = 2;
    public static final int shooterHoodSolenoidReverse = 3;

    //climber
    public static final int climberSparkID = 11;

    // Intake
    public static final int intakeRollerTalonId = 7;

    // Intake Solenoids
    public static final int intakeSolenoidLeftForward = 0;
    public static final int intakeSolenoidLeftReverse = 1;
    @Config
    public static double intakeSpeed = 0.5;

    // Serializer
    public static final int serializerLeaderSparkMaxId = 6;
    public static final int serializerFollowerSparkMaxId = 5;
    @Config
    public static double frontSerializerSpeed = 0.4;
    public static double backSerializerSpeed = 0.2;

    public static final double batterShotSerializeFrontSpeed = 0.8;
    public static final double batterShotSerializeBackSpeed = 0.64;


    public static final double kJoystickTurnDeadzone = 0.15;

    /** ---- Flat constants, you should not need to change these ---- */
	/* We allow either a 0 or 1 when selecting an ordinal for remote devices [You can have up to 2 devices assigned remotely to a talon/victor] */
	public final static int REMOTE_0 = 0;
	public final static int REMOTE_1 = 1;
	/* We allow either a 0 or 1 when selecting a PID Index, where 0 is primary and 1 is auxiliary */
	public final static int PID_PRIMARY = 0;
	public final static int PID_TURN = 1;
	/* Firmware currently supports slots [0, 3] and can be used for either PID Set */
	public final static int SLOT_0 = 0;
	public final static int SLOT_1 = 1;
	public final static int SLOT_2 = 2;
	public final static int SLOT_3 = 3;
	/* ---- Named slots, used to clarify code ---- */
	public final static int kSlot_Distance = SLOT_0;
	public final static int kSlot_Turning = SLOT_1;
	public final static int kSlot_Velocity = SLOT_2;
	public final static int kSlot_MotionProfile = SLOT_3;
}