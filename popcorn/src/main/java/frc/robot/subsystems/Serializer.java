/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Serializer extends SubsystemBase {
    private final CANSparkMax serializerLeader = new CANSparkMax(Constants.serializerLeaderSparkMaxId, MotorType.kBrushless);
    private final CANSparkMax serializerFollower = new CANSparkMax(Constants.serializerFollowerSparkMaxId, MotorType.kBrushless);


    public Serializer() {
        serializerLeader.setIdleMode(IdleMode.kBrake);
        serializerFollower.setIdleMode(IdleMode.kBrake);

        serializerFollower.setInverted(true);
        //serializerFollower.follow(serializerLeader);
    }

    @Override
    public void periodic() {
    // This method will be called once per scheduler run
    }

    public void set(final double speed){
        serializerLeader.set(speed);
        serializerFollower.set(speed);
    }
}
