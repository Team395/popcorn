/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {
    // TalonSRX talonLeader = new TalonSRX(11);
    // TalonSRX talonFollower = new TalonSRX(12);

    // Joystick _joystick = new Joystick(3);
    CANSparkMax shooterMotor = new CANSparkMax(Constants.shooterMotorSparkID, MotorType.kBrushless);
    CANEncoder encoder;
    CANPIDController controller;
  

  public Shooter() {
    shooterMotor.setInverted(true);
    shooterMotor.setIdleMode(IdleMode.kCoast);
    encoder = shooterMotor.getEncoder();
    controller = shooterMotor.getPIDController();
    controller.setFeedbackDevice(encoder);
    stop();

    SmartDashboard.putNumber("desiredSetpoint", 3000);
    SmartDashboard.putNumber("setP", 0.0005);
    SmartDashboard.putNumber("setI", 0);
    SmartDashboard.putNumber("setD", 4);
    SmartDashboard.putNumber("setF", 0.00019);

    updateConstants();



    // talonLeader.setInverted(true);
    // talonFollower.setInverted(true);
    // talonFollower.follow(talonLeader);
  }

  public void update() {
    SmartDashboard.putNumber("velocity", encoder.getVelocity());
    updateConstants();
  }

  public void set(double setpoint) {
    setpoint = SmartDashboard.getNumber("desiredSetpoint", 1000);
    controller.setReference(setpoint, ControlType.kVelocity);
    SmartDashboard.putNumber("setpoint", setpoint);
  }

  public void stop() {
    controller.setReference(0, ControlType.kDutyCycle);
  }

  public double shooterP = 0.0001;
  public double shooterI = 0;
  public double shooterD = 2;
  public double shooterF = 0.00017;

  public void updateConstants() {
    shooterP = SmartDashboard.getNumber("setP", 0.0001);
    shooterI = SmartDashboard.getNumber("setI", 0);
    shooterD = SmartDashboard.getNumber("setD", 2);
    shooterF = SmartDashboard.getNumber("setF", 0.00017);

    controller.setOutputRange(0, 1);
    controller.setP(shooterP);
    controller.setI(shooterI);
    controller.setD(shooterD);
    controller.setFF(shooterF);

    SmartDashboard.putNumber("kP", shooterP);
    SmartDashboard.putNumber("kI", shooterI);
    SmartDashboard.putNumber("kD", shooterD);
    SmartDashboard.putNumber("kF", shooterF);
  }


  public void shoot(double speed) {
    shooterMotor.set(speed);
  }

  // @Override
  // public void periodic() {
  //   // This method will be called once per scheduler run
  //   double stick = _joystick.getRawAxis(1);
  //   shooterMotor.set(stick);
  //   // talonLeader.set(ControlMode.PercentOutput, stick);
  // }
}
