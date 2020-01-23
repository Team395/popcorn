/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    TalonSRX talonLeader = new TalonSRX(11);
    TalonSRX talonFollower = new TalonSRX(12);

    Joystick _joystick = new Joystick(3);
  /**
   * Creates a new ExampleSubsystem.
   */
  public Shooter() {
    talonLeader.setInverted(true);
    talonFollower.setInverted(true);

    talonFollower.follow(talonLeader);
    

  }

  public void Shot(double speed) {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    double stick = _joystick.getRawAxis(1);
    talonLeader.set(ControlMode.PercentOutput, stick);
  }
}
