/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {
    CANSparkMax acceleratorSparkMax = new CANSparkMax(Constants.acceleratorSparkID, MotorType.kBrushless);
    CANEncoder acceleratorEncoder;
    CANPIDController acceleratorController;
    
    TalonFX flywheelLeaderTalonFX = new TalonFX(Constants.flywheelLeaderTalonID);
    TalonFX flywheelFollowerTalonFX = new TalonFX(Constants.flywheelFollowerTalonID);
    TalonFXConfiguration configs = new TalonFXConfiguration();

  public Shooter() {
    acceleratorSparkMax.setIdleMode(IdleMode.kCoast);
    acceleratorEncoder = acceleratorSparkMax.getEncoder();
    acceleratorController = acceleratorSparkMax.getPIDController();
    acceleratorController.setFeedbackDevice(acceleratorEncoder);

    configs.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    flywheelFollowerTalonFX.configAllSettings(configs);
    flywheelLeaderTalonFX.configAllSettings(configs);

    flywheelFollowerTalonFX.setInverted(TalonFXInvertType.Clockwise);
    flywheelLeaderTalonFX.setInverted(TalonFXInvertType.Clockwise);

    flywheelFollowerTalonFX.setNeutralMode(NeutralMode.Coast);
    flywheelLeaderTalonFX.setNeutralMode(NeutralMode.Coast);
    
    flywheelFollowerTalonFX.follow(flywheelLeaderTalonFX);

    flywheelFollowerTalonFX.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);
    flywheelLeaderTalonFX.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);

    stop();

    SmartDashboard.putNumber("acceleratorDesiredSetpoint", 3000);
    SmartDashboard.putNumber("acceleratorSetP", 0.0005);
    SmartDashboard.putNumber("acceleratorSetI", 0);
    SmartDashboard.putNumber("acceleratorSetD", 4);
    SmartDashboard.putNumber("acceleratorSetF", 0.00019);

    SmartDashboard.putNumber("flywheelDesiredSetpoint", 4000);
    SmartDashboard.putNumber("flywheelSetP", 0.0005);
    SmartDashboard.putNumber("flywheelSetI", 0);
    SmartDashboard.putNumber("flywheelSetD", 4);
    SmartDashboard.putNumber("flywheelSetF", 0.00019);

    updateConstants();
  }

  public void update() {
    SmartDashboard.putNumber("acceleratorVelocity", acceleratorEncoder.getVelocity());
    SmartDashboard.putNumber("flywheelLeaderVelocity", flywheelLeaderTalonFX.getSelectedSensorVelocity()); 

    updateConstants();
  }

  public void set(double acceleratorSetpoint, double flywheelSetpoint) {
    acceleratorSetpoint = SmartDashboard.getNumber("acceleratorDesiredSetpoint", 1000);
    acceleratorController.setReference(acceleratorSetpoint, ControlType.kVelocity);
    SmartDashboard.putNumber("acceleratorSetpoint", acceleratorSetpoint);

    flywheelSetpoint = SmartDashboard.getNumber("flywheelDesiredSetpoint", 1000);
    flywheelLeaderTalonFX.set(ControlMode.Velocity, flywheelSetpoint);
    SmartDashboard.putNumber("flywheelSetpoint", flywheelSetpoint);
  }

  public void stop() {
    acceleratorController.setReference(0, ControlType.kDutyCycle);
    flywheelLeaderTalonFX.set(ControlMode.PercentOutput, 0);
    flywheelFollowerTalonFX.set(ControlMode.PercentOutput, 0);
  }

  public double acceleratorP = 0.0001;
  public double acceleratorI = 0;
  public double acceleratorD = 2;
  public double acceleratorF = 0.00017;

  public double flywheelP = 0.0001;
  public double flywheelI = 0;
  public double flywheelD = 2;
  public double flywheelF = 0.00017;

  public void updateConstants() {
    acceleratorP = SmartDashboard.getNumber("acceleratorSetP", 0.0001);
    acceleratorI = SmartDashboard.getNumber("acceleratorSetI", 0);
    acceleratorD = SmartDashboard.getNumber("acceleratorSetD", 2);
    acceleratorF = SmartDashboard.getNumber("acceleratorSetF", 0.00017);

    acceleratorController.setOutputRange(0, 1);
    acceleratorController.setP(acceleratorP);
    acceleratorController.setI(acceleratorI);
    acceleratorController.setD(acceleratorD);
    acceleratorController.setFF(acceleratorF);

    SmartDashboard.putNumber("acceleratorKP", acceleratorP);
    SmartDashboard.putNumber("acceleratorKI", acceleratorI);
    SmartDashboard.putNumber("acceleratorKD", acceleratorD);
    SmartDashboard.putNumber("acceleratorKF", acceleratorF);

    flywheelP = SmartDashboard.getNumber("flywheelSetP", 0.0001);
    flywheelI = SmartDashboard.getNumber("flywheelSetI", 0);
    flywheelD = SmartDashboard.getNumber("flywheelSetD", 2);
    flywheelF = SmartDashboard.getNumber("flywheelSetF", 0.00017);
    
    configs.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    configs.slot0.kP = flywheelP;
    configs.slot0.kI = flywheelI;
    configs.slot0.kD = flywheelD;
    configs.slot0.kF = flywheelF;
    flywheelFollowerTalonFX.configAllSettings(configs);
    flywheelLeaderTalonFX.configAllSettings(configs);

    SmartDashboard.putNumber("flywheelKP", flywheelP);
    SmartDashboard.putNumber("flywheelKI", flywheelI);
    SmartDashboard.putNumber("flywheelKD", flywheelD);
    SmartDashboard.putNumber("flywheelKF", flywheelF);
  }
}
