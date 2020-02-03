package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class FalconShooter extends SubsystemBase{
    TalonFX shooterLeaderTalon = new TalonFX(Constants.shooterLeaderTalonID);
    TalonFX shooterFollowerTalon = new TalonFX(Constants.shooterFollowerTalonID);
    
    
}