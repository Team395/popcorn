package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drivetrain extends SubsystemBase {

    CANSparkMax leftLeader = new CANSparkMax(Constants.driveLeftLeaderSparkID, MotorType.kBrushless);
    CANSparkMax leftFollower = new CANSparkMax(Constants.driveLeftFollowerSparkID, MotorType.kBrushless);

    public Drivetrain() {
        leftLeader.setInverted(true);
        leftFollower.setInverted(true);

        leftFollower.follow(leftLeader);
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        leftLeader.set(leftSpeed);
        // rightLeader.set(rightSpeed);
    }

}
