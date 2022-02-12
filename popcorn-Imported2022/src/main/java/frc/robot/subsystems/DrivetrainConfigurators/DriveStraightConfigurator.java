package frc.robot.subsystems.DrivetrainConfigurators;

import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;

public class DriveStraightConfigurator {
    private Drivetrain drivetrain;

    public DriveStraightConfigurator(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    public void configDrivetrainDriveStraight() {
        var rightConfig = new TalonFXConfiguration();
        var leftConfig = new TalonFXConfiguration();

        // Configure the left Talon's selected sensor as integrated sensor
        leftConfig.primaryPID.selectedFeedbackSensor
            = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice();

        // Configure the Remote (Left) Talon's selected sensor as a remote sensor for the right Talon
        rightConfig.remoteFilter0.remoteSensorDeviceID = drivetrain.leftLeader.getDeviceID();
        rightConfig.remoteFilter0.remoteSensorSource = RemoteSensorSource.TalonFX_SelectedSensor;

        setRobotDistanceConfigs(drivetrain._rightInvert, rightConfig);

        // PIDF for distance
        rightConfig.slot0.kP = Constants.kGains_Distance.kP;
        rightConfig.slot0.kI = Constants.kGains_Distance.kI;
        rightConfig.slot0.kD = Constants.kGains_Distance.kD;
        rightConfig.slot0.kF = Constants.kGains_Distance.kF;

        rightConfig.slot0.integralZone = Constants.kGains_Distance.kIzone;
        rightConfig.slot0.closedLoopPeakOutput = Constants.kGains_Distance.kPeakOutput;

        // Heading configs
        rightConfig.remoteFilter1.remoteSensorDeviceID = drivetrain.pidgey.getDeviceID();
        rightConfig.remoteFilter1.remoteSensorSource = RemoteSensorSource.Pigeon_Yaw;
        rightConfig.auxiliaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.RemoteSensor1.toFeedbackDevice();
        rightConfig.auxiliaryPID.selectedFeedbackCoefficient =
            3600.0 / Constants.kPigeonUnitsPerRotation; // convert yaw to tenths of a degree
        
        // PIDF for heading
        rightConfig.slot1.kP = Constants.kGains_Turning.kP;
        rightConfig.slot1.kI = Constants.kGains_Turning.kI;
        rightConfig.slot1.kD = Constants.kGains_Turning.kD;
        rightConfig.slot1.kF = Constants.kGains_Turning.kF;
        rightConfig.slot1.integralZone = Constants.kGains_Turning.kIzone;
        rightConfig.slot1.closedLoopPeakOutput = Constants.kGains_Turning.kPeakOutput;

        leftConfig.neutralDeadband = Constants.kNeutralDeadband;
        rightConfig.neutralDeadband = Constants.kNeutralDeadband;

        /**
         * 1ms per loop.  PID loop can be slowed down if need be.
         * For example,
         * - if sensor updates are too slow
         * - sensor deltas are very small per update, so derivative error never gets large enough to be useful.
         * - sensor movement is very slow causing the derivative error to be near zero.
         */
        int closedLoopTimeMs = 1;
        rightConfig.slot0.closedLoopPeriod = closedLoopTimeMs;
        rightConfig.slot1.closedLoopPeriod = closedLoopTimeMs;
        rightConfig.slot2.closedLoopPeriod = closedLoopTimeMs;
        rightConfig.slot3.closedLoopPeriod = closedLoopTimeMs;

        drivetrain.configureDrivetrain(leftConfig, rightConfig);
    }

    /** 
	 * Determines if SensorSum or SensorDiff should be used 
	 * for combining left/right sensors into Robot Distance.  
	 * 
	 * Assumes Aux Position is set as Remote Sensor 0.  
	 * 
	 * configAllSettings must still be called on the master config
	 * after this function modifies the config values. 
	 * 
	 * @param leaderInvertType Invert of the Master Talon
	 * @param leaderConfig Configuration object to fill
	 */
	 public void setRobotDistanceConfigs(TalonFXInvertType leaderInvertType, TalonFXConfiguration leaderConfig){
		/**
		 * Determine if we need a Sum or Difference.
		 * 
		 * The auxiliary Talon FX will always be positive
		 * in the forward direction because it's a selected sensor
		 * over the CAN bus.
		 * 
		 * The master's native integrated sensor may not always be positive when forward because
		 * sensor phase is only applied to *Selected Sensors*, not native
		 * sensor sources.  And we need the native to be combined with the 
		 * aux (other side's) distance into a single robot distance.
		 */

		/* THIS FUNCTION should not need to be modified. 
		   This setup will work regardless of whether the master
		   is on the Right or Left side since it only deals with
		   distance magnitude.  */

		/* Check if we're inverted */
		if (leaderInvertType == TalonFXInvertType.Clockwise){
			/* 
				If master is inverted, that means the integrated sensor
				will be negative in the forward direction.

				If master is inverted, the final sum/diff result will also be inverted.
				This is how Talon FX corrects the sensor phase when inverting 
				the motor direction.  This inversion applies to the *Selected Sensor*,
				not the native value.

				Will a sensor sum or difference give us a positive total magnitude?

				Remember the Master is one side of your drivetrain distance and 
				Auxiliary is the other side's distance.

					Phase | Term 0   |   Term 1  | Result
				Sum:  -1 *((-)Master + (+)Aux   )| NOT OK, will cancel each other out
				Diff: -1 *((-)Master - (+)Aux   )| OK - This is what we want, magnitude will be correct and positive.
				Diff: -1 *((+)Aux    - (-)Master)| NOT OK, magnitude will be correct but negative
			*/

			leaderConfig.diff0Term = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice(); //Local Integrated Sensor
			leaderConfig.diff1Term = TalonFXFeedbackDevice.RemoteSensor0.toFeedbackDevice();   //Aux Selected Sensor
			leaderConfig.primaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.SensorDifference.toFeedbackDevice(); //Diff0 - Diff1
		} else {
			/* Master is not inverted, both sides are positive so we can sum them. */
			leaderConfig.sum0Term = TalonFXFeedbackDevice.RemoteSensor0.toFeedbackDevice();    //Aux Selected Sensor
			leaderConfig.sum1Term = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice(); //Local IntegratedSensor
			leaderConfig.primaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.SensorSum.toFeedbackDevice(); //Sum0 + Sum1
		}

		/* Since the Distance is the sum of the two sides, divide by 2 so the total isn't double
		   the real-world value */
		leaderConfig.primaryPID.selectedFeedbackCoefficient = 0.5;
	 }
}