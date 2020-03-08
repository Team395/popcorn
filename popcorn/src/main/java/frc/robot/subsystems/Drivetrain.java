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
import edu.wpi.first.wpiutil.math.MathUtil;
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
        leftLeader.set(TalonFXControlMode.PercentOutput, leftSpeed);
        rightLeader.set(TalonFXControlMode.PercentOutput, rightSpeed);
    }

    public void arcadeDrive(double speed, double turn) {
        SmartDashboard.putNumber("turn", turn);
        turn = MathUtil.clamp(turn, -1 * Constants.kTurnClamp, Constants.kTurnClamp);
        double sign = Math.signum(turn);
        if(sign > 0) { turn = Math.max(Constants.kTurnMinimumSpeed, turn); }
        else if(sign < 0) { turn = Math.min(-1 * Constants.kTurnMinimumSpeed, turn); }
        SmartDashboard.putNumber("massagedTurn", turn);

        leftLeader.set(ControlMode.PercentOutput, speed + turn);
        rightLeader.set(ControlMode.PercentOutput, speed - turn);
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

    public void configureDrivetrain(TalonFXConfiguration leftConfig, TalonFXConfiguration rightConfig) {
        leftLeader.configAllSettings(leftConfig);
        rightLeader.configAllSettings(rightConfig);

        leftLeader.setInverted(_leftInvert);
        rightLeader.setInverted(_rightInvert);
        leftFollower.setInverted(TalonFXInvertType.FollowMaster);
        rightFollower.setInverted(TalonFXInvertType.FollowMaster);
        
        leftFollower.follow(leftLeader);
        rightFollower.follow(rightLeader);

        rightLeader.configAuxPIDPolarity(true);
        
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

    public double getHeading() {
        var pidgeyArray = new double[3];
        pidgey.getYawPitchRoll(pidgeyArray);
        SmartDashboard.putNumber("pidgey yaw", pidgeyArray[0]);
        return pidgeyArray[0] * (Constants.kGyroReversed ? -1 : 1);
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
        SmartDashboard.putNumber("pidgey", rightLeader.getSelectedSensorPosition(1));
        var pidgeyArray = new double[3];
        pidgey.getAccumGyro(pidgeyArray);
        SmartDashboard.putNumber("pidgeyDirect", pidgeyArray[0]);
    }
}
