/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Set;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.ColorMatch;
import frc.robot.commands.TankDrive;
import frc.robot.subsystems.ColorSensor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  // private final ColorSensor m_colorSensor = new ColorSensor();

  // private final ColorMatch m_colorMatch = new ColorMatch(m_colorSensor);

  // private final Drivetrain m_drivetrain = new Drivetrain();
  // private final TankDrive m_tankDrive = new TankDrive(m_drivetrain, this);
  private final Limelight m_limelight = new Limelight();


  //private final Shooter m_shooter = new Shooter();

  Joystick leftJoystick = new Joystick(3);
  Joystick rightJoystick = new Joystick(4);
  JoystickButton leftJoystickTrigger = new JoystickButton(leftJoystick, 1);
  // XboxController xboxController = new XboxController(2);

  static final double joystickDeadzone = 0.1;
  // static final double xboxDeadzone = 0.25;

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    //configureButtonBindings();
    // m_colorSensor.setDefaultCommand(m_colorMatch);
    // m_drivetrain.setDefaultCommand(m_tankDrive);
  }

  public void periodic() {
    //m_shooter.update();
    m_limelight.update();

  }

  private double getJoyY(Joystick stick) {
    if(Math.abs(stick.getY()) < joystickDeadzone) {
        return 0;
    }

    return -stick.getY();     
}

  public double getLeftY() {
    return getJoyY(leftJoystick);
}

  public double getRightY() {
    return getJoyY(rightJoystick);
}
  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  // private void configureButtonBindings() {
  //   m_shooter.setDefaultCommand(
  //     new RunCommand(() -> m_shooter.stop(), m_shooter));

  //     leftJoystickTrigger
  //       .whenHeld(new RunCommand(() -> m_shooter.set(4000), m_shooter))
  //       .whenReleased(new RunCommand(() -> m_shooter.stop(), m_shooter));
  // }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    // return m_colorMatch;
    return new Command(){
    
      @Override
      public Set<Subsystem> getRequirements() {
        // TODO Auto-generated method stub
        return null;
      }
    };
  }
}
