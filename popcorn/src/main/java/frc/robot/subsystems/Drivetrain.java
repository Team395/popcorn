package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class Drivetrain extends SubsystemBase {
    
    CANSparkMax leftLeader = new CANSparkMax(RobotMap.driveLeftLeaderMapID, MotorType.kBrushless);
}
