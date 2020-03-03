package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drivetrain extends SubsystemBase {

    // CANSparkMax leftLeader = new CANSparkMax(Constants.driveLeftLeaderSparkID, MotorType.kBrushless);
    // CANSparkMax leftFollower = new CANSparkMax(Constants.driveLeftFollowerSparkID, MotorType.kBrushless);
    // CANSparkMax rightLeader = new CANSparkMax(Constants.driveRightLeaderSparkID, MotorType.kBrushless);
    // CANSparkMax rightFollower = new CANSparkMax(Constants.driveRightFollowerSparkID, MotorType.kBrushless);
    WPI_TalonFX leftLeader = new WPI_TalonFX(Constants.driveLeftLeaderFalconID);
    WPI_TalonFX leftFollower = new WPI_TalonFX(Constants.driveLeftFollowerFalconID);
    WPI_TalonFX rightLeader = new WPI_TalonFX(Constants.driveRightLeaderFalconID);
    WPI_TalonFX rightFollower = new WPI_TalonFX(Constants.driveRightFollowerFalconID);
    DifferentialDrive differentialDrive = new DifferentialDrive(leftLeader, rightLeader);
    
    public Drivetrain() {
        leftLeader.setNeutralMode(NeutralMode.Brake);
        leftFollower.setNeutralMode(NeutralMode.Brake);
        rightLeader.setNeutralMode(NeutralMode.Brake);
        rightFollower.setNeutralMode(NeutralMode.Brake);

        leftLeader.setInverted(true);
        leftFollower.setInverted(InvertType.FollowMaster);
        rightLeader.setInverted(true);
        rightFollower.setInverted(InvertType.FollowMaster);

        leftFollower.follow(leftLeader);
        rightFollower.follow(rightLeader);
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        differentialDrive.tankDrive(leftSpeed, rightSpeed);
    }

}
