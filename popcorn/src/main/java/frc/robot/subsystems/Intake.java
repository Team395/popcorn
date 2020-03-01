/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.enums.IntakePositions;

public class Intake extends SubsystemBase {
    private final TalonSRX intakeRoller = new TalonSRX(Constants.intakeRollerTalonId);
    
    // private final DoubleSolenoid intakeSolenoid =
    //     new DoubleSolenoid(Constants.intakeSolenoidLeftForward
    //         , Constants.intakeSolenoidLeftReverse);

    public Intake() {
        intakeRoller.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    public void periodic() {
    // This method will be called once per scheduler run
    }

    public void set(final double speed){
        intakeRoller.set(ControlMode.PercentOutput, speed);
    }

    public void moveIntake(final IntakePositions position) {
        // switch(position) {
        //     case UP:
        //         intakeSolenoid.set(Value.kReverse);
        //         break;
        //     case DOWN:
        //         intakeSolenoid.set(Value.kForward);
        //         break;
        // }
    }
}
