/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.subsystems.Climber;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * An example command that uses an example subsystem.
 */
public class Climb extends CommandBase {
  private final Climber m_climber;
  private double encoderPosition = 0.0;
  private double encoderFinalPosition = 0.0;
  private boolean m_extend;
  private double m_multiplier;
  // private final ExampleSubsystem m_subsystem;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public Climb(final Climber climber, boolean extend) {
    m_climber = climber;
    m_extend = extend;
    if (extend)
      m_multiplier = 1.0;
    else
      m_multiplier = -1.0;
    // m_subsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    encoderPosition = m_climber.getEncoderPosition();
    SmartDashboard.putNumber("Encoder", encoderPosition);

    encoderFinalPosition = encoderPosition + 240 * m_multiplier;
    // make sure position is not below 0
    encoderFinalPosition = Math.max(encoderFinalPosition, 0);
    // make sure position is not above 240
    encoderFinalPosition = Math.min(encoderFinalPosition, 240);
  }

  @Log
  public double m_spin = 0.9;
  @Config
  public void setSpin(double value) {
    m_spin = value;
  }
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    encoderPosition = m_climber.getEncoderPosition();
    SmartDashboard.putNumber("Encoder", encoderPosition);
    // m_climber.spin(m_spin * m_multiplier);
    m_climber.spin(0.4 * m_multiplier);
//    m_climber.spin(m_robotContainer.getLeftY());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(final boolean interrupted) {
    encoderPosition = m_climber.getEncoderPosition();
    SmartDashboard.putNumber("Encoder", encoderPosition);
    m_climber.spin(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;

    // encoderPosition = m_climber.getEncoderPosition();
    // if (m_extend){

    // if (encoderPosition >= encoderFinalPosition)
    //   return true;
    // else
    //   return false;
    // } else {
    //   if (encoderPosition <= encoderFinalPosition)
    //     return true;
    //   else
    //     return false;
    // }
  }
}
