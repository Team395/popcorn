/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {

    CANSparkMax climber = new CANSparkMax(Constants.climberSparkID, null);
  /**
   * Creates a new ExampleSubsystem.
   */
  public Climber() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }


public void climberDrive(double speed) {
    climber.set(speed);
}

}