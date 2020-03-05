package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.enums.DrivetrainShifterGears;
import io.github.oblarg.oblog.Loggable;

public class Drivetrain extends SubsystemBase implements Loggable {
    public TalonFX leftLeader = new TalonFX(Constants.driveLeftLeaderFalconID);
    public TalonFX leftFollower = new TalonFX(Constants.driveLeftFollowerFalconID);
    public TalonFX rightLeader = new TalonFX(Constants.driveRightLeaderFalconID);
    public TalonFX rightFollower = new TalonFX(Constants.driveRightFollowerFalconID);
    // DifferentialDrive differentialDrive = new DifferentialDrive(leftLeader, rightLeader);
    DoubleSolenoid shifter = new DoubleSolenoid(Constants.shifterSolenoidForward,Constants.shifterSolenoidReverse);

    public DrivetrainShifterGears currentPosition = DrivetrainShifterGears.LOW;

    public PigeonIMU pidgey = new PigeonIMU(Constants.pigeonCanID);
    public TalonFXConfiguration leftConfig = new TalonFXConfiguration();
    public TalonFXConfiguration rightConfig = new TalonFXConfiguration();

    public TalonFXInvertType _leftInvert = TalonFXInvertType.CounterClockwise; //Same as invert = "false"
	public TalonFXInvertType _rightInvert = TalonFXInvertType.Clockwise; //Same as invert = "true"

    public Drivetrain() {
        pidgey.configFactoryDefault();
        leftLeader.configFactoryDefault();
        leftFollower.configFactoryDefault();
        rightLeader.configFactoryDefault();
        rightFollower.configFactoryDefault();

        leftLeader.setNeutralMode(NeutralMode.Brake);
        leftFollower.setNeutralMode(NeutralMode.Brake);
        rightLeader.setNeutralMode(NeutralMode.Brake);
        rightFollower.setNeutralMode(NeutralMode.Brake);

        leftLeader.setInverted(false);
        leftFollower.setInverted(InvertType.FollowMaster);
        rightLeader.setInverted(true);
        rightFollower.setInverted(InvertType.FollowMaster);

        leftFollower.follow(leftLeader);
        rightFollower.follow(rightLeader);
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        // differentialDrive.tankDrive(leftSpeed, rightSpeed);
        leftLeader.set(TalonFXControlMode.PercentOutput, leftSpeed);
        rightLeader.set(TalonFXControlMode.PercentOutput, rightSpeed);
    }

    public void shiftGear(final DrivetrainShifterGears gear) {
        switch(gear) {
            case HIGH:
                shifter.set(Value.kReverse);
                currentPosition = DrivetrainShifterGears.HIGH;
                break;
            case LOW:
                shifter.set(Value.kForward);
                currentPosition = DrivetrainShifterGears.LOW;
                break;
        }
    }

    public void toggleGearPosition() {
        switch(currentPosition) {
            case HIGH:
                shiftGear(DrivetrainShifterGears.LOW);
                break;
            case LOW:
                shiftGear(DrivetrainShifterGears.HIGH);
                break;
        }
    }

    public void configDrivetrainDriveStraight() {
        // Configure the left Talon's selected sensor as integrated sensor
        leftConfig.primaryPID.selectedFeedbackSensor
            = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice();

        // Configure the Remote (Left) Talon's selected sensor as a remote sensor for the right Talon
        rightConfig.remoteFilter0.remoteSensorDeviceID = leftLeader.getDeviceID();
        rightConfig.remoteFilter0.remoteSensorSource = RemoteSensorSource.TalonFX_SelectedSensor;

        setRobotDistanceConfigs(_rightInvert, rightConfig);

        // PIDF for distance
        rightConfig.slot0.kP = Constants.kGains_Distance.kP;
        rightConfig.slot0.kI = Constants.kGains_Distance.kI;
        rightConfig.slot0.kD = Constants.kGains_Distance.kD;
        rightConfig.slot0.kF = Constants.kGains_Distance.kF;

        rightConfig.slot0.integralZone = Constants.kGains_Distance.kIzone;
        rightConfig.slot0.closedLoopPeakOutput = Constants.kGains_Distance.kPeakOutput;

        // Heading configs
        rightConfig.remoteFilter1.remoteSensorDeviceID = pidgey.getDeviceID();
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

        leftLeader.configAllSettings(leftConfig);
        rightLeader.configAllSettings(rightConfig);

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
        
        leftLeader.setInverted(_leftInvert);
		rightLeader.setInverted(_rightInvert);
		leftFollower.setInverted(TalonFXInvertType.FollowMaster);
        rightFollower.setInverted(TalonFXInvertType.FollowMaster);
        
        leftFollower.follow(leftLeader);
        rightFollower.follow(rightLeader);
        
        /* Set status frame periods to ensure we don't have stale data */
		rightLeader.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, Constants.kTimeoutMs);
		rightLeader.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, Constants.kTimeoutMs);
		rightLeader.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20, Constants.kTimeoutMs);
		leftLeader.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, Constants.kTimeoutMs);

        rightLeader.selectProfileSlot(Constants.kSlot_Distance, Constants.PID_PRIMARY);
        rightLeader.selectProfileSlot(Constants.kSlot_Turning, Constants.PID_TURN);
    }

    public double getClosedLoopError() {
        return rightLeader.getClosedLoopError();
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
	 * @param masterInvertType Invert of the Master Talon
	 * @param masterConfig Configuration object to fill
	 */
	 public void setRobotDistanceConfigs(TalonFXInvertType masterInvertType, TalonFXConfiguration masterConfig){
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
		if (masterInvertType == TalonFXInvertType.Clockwise){
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

			masterConfig.diff0Term = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice(); //Local Integrated Sensor
			masterConfig.diff1Term = TalonFXFeedbackDevice.RemoteSensor0.toFeedbackDevice();   //Aux Selected Sensor
			masterConfig.primaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.SensorDifference.toFeedbackDevice(); //Diff0 - Diff1
		} else {
			/* Master is not inverted, both sides are positive so we can sum them. */
			masterConfig.sum0Term = TalonFXFeedbackDevice.RemoteSensor0.toFeedbackDevice();    //Aux Selected Sensor
			masterConfig.sum1Term = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice(); //Local IntegratedSensor
			masterConfig.primaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.SensorSum.toFeedbackDevice(); //Sum0 + Sum1
		}

		/* Since the Distance is the sum of the two sides, divide by 2 so the total isn't double
		   the real-world value */
		masterConfig.primaryPID.selectedFeedbackCoefficient = 0.5;
	 }

    // Zero all sensors, both Pigeon and Talons
	public void zeroSensors() {
        zeroDistance();
        
		pidgey.setYaw(0, Constants.kTimeoutMs);
		pidgey.setAccumZAngle(0, Constants.kTimeoutMs);
		System.out.println("[Integrated Sensors + Position] All sensors are zeroed.\n");
	}

    // Zero integrated sensors on Talons
    public void zeroDistance() {
        leftLeader.getSensorCollection().setIntegratedSensorPosition(0, Constants.kTimeoutMs);
        rightLeader.getSensorCollection().setIntegratedSensorPosition(0, Constants.kTimeoutMs);

        leftLeader.setSelectedSensorPosition(0);
        rightLeader.setSelectedSensorPosition(0);

        System.out.println("[Integrated Sensors] All encoders are zeroed.\n");
    }

    public void driveStraight(double targetStraightUnits, double targetTurn) {
        targetTurn = rightLeader.getSelectedSensorPosition(1);
        rightLeader.set(TalonFXControlMode.Position, targetStraightUnits, DemandType.AuxPID, targetTurn);
        // rightLeader.set(ControlMode.Position, targetStraightUnits);
        leftLeader.follow(rightLeader, FollowerType.AuxOutput1);
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("leftSensor", leftLeader.getSelectedSensorPosition());
        SmartDashboard.putNumber("rightSensor", rightLeader.getSelectedSensorPosition());
        SmartDashboard.putNumber("rightPrimary", rightLeader.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("rightAux", rightLeader.getSelectedSensorPosition(1));
        SmartDashboard.putNumber("leftPrimary", leftLeader.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("leftAux", leftFollower.getSelectedSensorPosition(1));
        SmartDashboard.putNumber("closedLoopTarget", rightLeader.getClosedLoopTarget());
        SmartDashboard.putNumber("closedLoopError", rightLeader.getClosedLoopError());
    }
}
