/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {
    // TalonSRX talonLeader = new TalonSRX(11);
    // TalonSRX talonFollower = new TalonSRX(12);

    CANSparkMax shooterLeaderSpark = new CANSparkMax(Constants.shooterLeaderSparkID, MotorType.kBrushless);
    CANSparkMax shooterFollowerSpark = new CANSparkMax(Constants.shooterFollowerSparkID, MotorType.kBrushless);
    CANEncoder leaderEncoder;
    CANEncoder followerEncoder;
    CANPIDController leaderController;
    CANPIDController followerController;
  

  public Shooter() {
    shooterLeaderSpark.setInverted(true);
    shooterFollowerSpark.setInverted(true);
    shooterLeaderSpark.setIdleMode(IdleMode.kCoast);
    shooterFollowerSpark.setIdleMode(IdleMode.kCoast);

    shooterFollowerSpark.follow(shooterLeaderSpark);

    leaderEncoder = shooterLeaderSpark.getEncoder();
    followerEncoder = shooterFollowerSpark.getEncoder();
    leaderController = shooterLeaderSpark.getPIDController();
    followerController = shooterFollowerSpark.getPIDController();
    leaderController.setFeedbackDevice(leaderEncoder);
    followerController.setFeedbackDevice(followerEncoder);

    stop();

    SmartDashboard.putNumber("desiredSetpoint", 3000);
    SmartDashboard.putNumber("setP", 0.0005);
    SmartDashboard.putNumber("setI", 0);
    SmartDashboard.putNumber("setD", 4);
    SmartDashboard.putNumber("setF", 0.00019);

    updateConstants();
  }

  public void update() {
    SmartDashboard.putNumber("velocity", leaderEncoder.getVelocity());
    updateConstants();
  }

  public void set(double setpoint) {
    setpoint = SmartDashboard.getNumber("desiredSetpoint", 1000);
    leaderController.setReference(setpoint, ControlType.kVelocity);
    SmartDashboard.putNumber("setpoint", setpoint);
  }

  public void stop() {
    leaderController.setReference(0, ControlType.kDutyCycle);
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

    leaderController.setOutputRange(0, 1);
    leaderController.setP(shooterP);
    leaderController.setI(shooterI);
    leaderController.setD(shooterD);
    leaderController.setFF(shooterF);

    SmartDashboard.putNumber("kP", shooterP);
    SmartDashboard.putNumber("kI", shooterI);
    SmartDashboard.putNumber("kD", shooterD);
    SmartDashboard.putNumber("kF", shooterF);
  }


  public void shoot(double speed) {
    shooterLeaderSpark.set(speed);
  }

}
