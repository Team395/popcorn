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

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Serializer extends SubsystemBase {
    private final TalonSRX serializerLeader = new TalonSRX(Constants.serializerLeaderSparkMaxId);
    private final TalonSRX serializerFollower = new TalonSRX(Constants.serializerFollowerSparkMaxId);


    public Serializer() {
        serializerLeader.setNeutralMode(NeutralMode.Brake);
        serializerFollower.setNeutralMode(NeutralMode.Brake);

        serializerFollower.setInverted(InvertType.OpposeMaster);
        serializerFollower.follow(serializerLeader);
    }

    @Override
    public void periodic() {
    // This method will be called once per scheduler run
    }

    public void set(final double speed){
        serializerLeader.set(ControlMode.PercentOutput, speed);
    }
}
