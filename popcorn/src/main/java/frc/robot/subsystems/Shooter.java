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
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.enums.ShooterHoodPositions;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

public class Shooter extends SubsystemBase implements Loggable {
    CANSparkMax acceleratorSparkMax = new CANSparkMax(Constants.acceleratorSparkID, MotorType.kBrushless);
    CANEncoder acceleratorEncoder;
    CANPIDController acceleratorController;
    
    TalonFX flywheelLeaderTalonFX = new TalonFX(Constants.flywheelLeaderTalonID);
    TalonFX flywheelFollowerTalonFX = new TalonFX(Constants.flywheelFollowerTalonID);
    TalonFXConfiguration configs = new TalonFXConfiguration();

    // DoubleSolenoid shooterHoodSolenoid = new DoubleSolenoid(Constants.shooterHoodSolenoidForward
    //   , Constants.shooterHoodSolenoidReverse);

  public Shooter() {
    configureAccelerator();

    configureFlywheel();
  }

  @Log public double acceleratorControllerKp = Constants.acceleratorP;
  @Log public double acceleratorControllerKi = Constants.acceleratorI;
  @Log public double acceleratorControllerKd = Constants.acceleratorD;
  @Log public double acceleratorControllerKf = Constants.acceleratorF;

  @Log public double acceleratorVelocity() {
    return acceleratorEncoder.getVelocity();
  };

  @Config
  public void setAcceleratorPid(double kP, double kI, double kD, double kF) {
    if(kP == 0) kP = Constants.acceleratorP;
    if(kI == 0) kI = Constants.acceleratorI;
    if(kD == 0) kD = Constants.acceleratorD;
    if(kF == 0) kF = Constants.acceleratorF;

    acceleratorController.setP(kP);
    acceleratorController.setI(kI);
    acceleratorController.setD(kD);
    acceleratorController.setFF(kF);

    acceleratorControllerKp = acceleratorController.getP();
    acceleratorControllerKi = acceleratorController.getI();
    acceleratorControllerKd = acceleratorController.getD();
    acceleratorControllerKf = acceleratorController.getFF();
  }

  private void configureAccelerator(){
    acceleratorSparkMax.setIdleMode(IdleMode.kBrake);
    acceleratorEncoder = acceleratorSparkMax.getEncoder();
    acceleratorController = acceleratorSparkMax.getPIDController();
    acceleratorController.setFeedbackDevice(acceleratorEncoder);
    acceleratorController.setOutputRange(0, 1);
    setAcceleratorPid(Constants.acceleratorP
      , Constants.acceleratorI
      , Constants.acceleratorD
      , Constants.acceleratorF);
  }

  private void configureFlywheel(){
    configs.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    configs.slot0.kP = Constants.flywheelP;
    configs.slot0.kI = Constants.flywheelI;
    configs.slot0.kD = Constants.flywheelD;
    configs.slot0.kF = Constants.flywheelF;
    flywheelFollowerTalonFX.configAllSettings(configs);
    flywheelLeaderTalonFX.configAllSettings(configs);

    flywheelFollowerTalonFX.setInverted(TalonFXInvertType.OpposeMaster);
    flywheelLeaderTalonFX.setInverted(TalonFXInvertType.CounterClockwise);

    flywheelFollowerTalonFX.setNeutralMode(NeutralMode.Coast);
    flywheelLeaderTalonFX.setNeutralMode(NeutralMode.Coast);
    
    flywheelFollowerTalonFX.follow(flywheelLeaderTalonFX);
  }

  public void set(double acceleratorSetpoint, double flywheelSetpoint) {
    acceleratorController.setReference(acceleratorSetpoint, ControlType.kVelocity);
    flywheelLeaderTalonFX.set(ControlMode.Velocity, flywheelSetpoint);
  }

  public void setFlywheel(double flywheelSetpoint) {
    flywheelLeaderTalonFX.set(ControlMode.Velocity, flywheelSetpoint);
  }

  public void setAccelerator(double acceleratorSetpoint) {
    acceleratorController.setReference(acceleratorSetpoint, ControlType.kVelocity);
  }

  public void moveHood(final ShooterHoodPositions position) {
    // switch(position) {
    //     case UP:
    //         shooterHoodSolenoid.set(Value.kReverse);
    //         break;
    //     case DOWN:
    //         shooterHoodSolenoid.set(Value.kForward);
    //         break;
    // }
}

  public void stop() {
    acceleratorController.setReference(0, ControlType.kDutyCycle);
    flywheelLeaderTalonFX.set(ControlMode.PercentOutput, 0);
  }

  public void stopAccelerator() {
    acceleratorController.setReference(0, ControlType.kDutyCycle);
  }

  public void stopFlywheel() {
    flywheelLeaderTalonFX.set(ControlMode.PercentOutput, 0);
  }
}
