/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {

    CANSparkMax climberMotor = new CANSparkMax(Constants.climberSparkID, MotorType.kBrushless);
    RelativeEncoder climberEncoder;
  /**
   * Creates a new ExampleSubsystem.
   */
  public Climber() {
    climberMotor.setIdleMode(IdleMode.kBrake);
    climberEncoder = climberMotor.getEncoder();
    climberMotor.setInverted(false);
    climberEncoder.setPosition(0);
    double encoderPosition = climberEncoder.getPosition();
    SmartDashboard.putNumber("Encoder", encoderPosition);
  }

  public double getEncoderPosition() {
    return climberEncoder.getPosition();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Log
  public double m_spin = 0.9;
  @Config
  public void setSpin(double value) {
    m_spin = value;
  }

  public void spin(double speed) {
    climberMotor.set(speed);
    // climberMotor.set(m_spin * speed);
  }

  public void stop() {
    climberMotor.set(0);
  }
}