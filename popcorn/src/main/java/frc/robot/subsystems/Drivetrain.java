package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drivetrain extends SubsystemBase {

    // CANSparkMax leftLeader = new CANSparkMax(Constants.driveLeftLeaderSparkID, MotorType.kBrushless);
    // CANSparkMax leftFollower = new CANSparkMax(Constants.driveLeftFollowerSparkID, MotorType.kBrushless);
    // CANSparkMax rightLeader = new CANSparkMax(Constants.driveRightLeaderSparkID, MotorType.kBrushless);
    // CANSparkMax rightFollower = new CANSparkMax(Constants.driveRightFollowerSparkID, MotorType.kBrushless);

    TalonFX leftLeader = new TalonFX(Constants.driveLeftLeaderFalconID);
    TalonFX leftFollower = new TalonFX(Constants.driveLeftFollowerFalconID);
    TalonFX rightLeader = new TalonFX(Constants.driveRightLeaderFalconID);
    TalonFX rightFollower = new TalonFX(Constants.driveRightFollowerFalconID);
    
    public Drivetrain() {
        leftLeader.setNeutralMode(NeutralMode.Brake);
        leftFollower.setNeutralMode(NeutralMode.Brake);
        rightLeader.setNeutralMode(NeutralMode.Brake);
        rightFollower.setNeutralMode(NeutralMode.Brake);

        leftLeader.setInverted(true);
        leftFollower.setInverted(true);
        rightLeader.setInverted(false);
        rightFollower.setInverted(false);

        leftFollower.follow(leftLeader);
        rightFollower.follow(rightLeader);
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        leftLeader.set(ControlMode.PercentOutput, leftSpeed);
        rightLeader.set(ControlMode.PercentOutput, rightSpeed);
    }

}
