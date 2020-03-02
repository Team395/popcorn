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
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.Climb;
import frc.robot.commands.ClimbToSetpoint;
// import edu.wpi.first.wpilibj.XboxController;
// import frc.robot.commands.ColorMatch;
import frc.robot.commands.TankDrive;
import frc.robot.commands.WaitForFlywheelToReachSetpoint;
import frc.robot.commands.intake.IntakePowerCells;
import frc.robot.commands.intake.StowIntake;
import frc.robot.subsystems.Climber;
// import frc.robot.subsystems.ColorSensor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Serializer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.WaitCommand;
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

  private final Drivetrain m_drivetrain = new Drivetrain();
  private final TankDrive m_tankDrive = new TankDrive(m_drivetrain, this);

  public final Climber m_climber = new Climber();
  // private final Hanger m_hanger = new Hanger(m_climber, extend);

  private final Shooter m_shooter = new Shooter();

  private final Intake m_intake = new Intake();
  private final Serializer m_serializer = new Serializer();

  Joystick leftJoystick = new Joystick(3);
  // Joystick rightJoystick = new Joystick(4);
  
  JoystickButton leftJoystickTrigger = new JoystickButton(leftJoystick, 1);
  JoystickButton leftJoystickThumbButton = new JoystickButton(leftJoystick, 2);
  JoystickButton leftJoystickButtonFour = new JoystickButton(leftJoystick, 4);

  // JoystickButton rightJoystickTrigger = new JoystickButton(rightJoystick, 1);
  // JoystickButton rightJoystickThumbButton = new JoystickButton(rightJoystick, 2);
  // JoystickButton rightJoystickButtonFour = new JoystickButton(rightJoystick, 4);
  
  XboxController xboxController = new XboxController(0);
  // JoystickButton xboxAButton = new JoystickButton(xboxController, 2);

  static final double joystickDeadzone = 0.15;
  // static final double xboxDeadzone = 0.25;

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    // m_colorSensor.setDefaultCommand(m_colorMatch);
    m_drivetrain.setDefaultCommand(m_tankDrive);
    // m_climber.setDefaultCommand(m_hanger);
  }

  public void periodic() {
  }

  public void teleopPeriodic() {
  }

  public void teleopInit() {
    // m_drivetrain.tankDrive(0.0, 0.0);
    // SmartDashboard
    //   .putNumber("EncoderStart"
    //     , m_climber.getEncoderPosition());
  }

  public double getControllerLeftTrigger() {
    return xboxController.getTriggerAxis(Hand.kLeft);
    // return xboxController.getRawAxis(XboxController.Axis.kLeftTrigger.value);
  }

  public double getControllerRightTrigger() {
    return xboxController.getTriggerAxis(Hand.kRight);
    // return xboxController.getRawAxis(XboxController.Axis.kRightTrigger.value);
  }

  public double getControllerTurn() {
    return xboxController.getX(Hand.kLeft);
    // return xboxController.getRawAxis(XboxController.Axis.kLeftX.value);
  }

  // private double getJoyY(Joystick stick) {
  //   if(Math.abs(stick.getY()) < joystickDeadzone) {
  //       return 0;
  //   }

  //   return -stick.getY();     
  // }

  // public double getLeftY() {
  //   return getJoyY(leftJoystick);
  // }

  // public double getRightY() {
  //   return getJoyY(rightJoystick);
  // }
  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  @Log
  public int direction = 1;
  @Config
  public void setDirection(int value) {
    direction = value;
  }
  @Log
  public double testSerializerSpeed = 0.1;

  @Config
  public void setTestSerializerSpeed(double value) {
    testSerializerSpeed = value;
  }

  @Log
  public double testIntakeSpeed = 0.1;

  @Config
  public void setTestIntakeSpeed(double value) {
    testIntakeSpeed = value;
  }

  @Log
  public double testAcceleratorSpeed = 0;

  @Config
  public void setTestAcceleratorSpeed(double value) {
    testAcceleratorSpeed = value;
  }

  @Log
  public double testFlywheelSpeed = 0;

  @Config
  public void setTestFlywheelSpeed(double value) {
    testFlywheelSpeed = value;
  }

  @Log
  public int flywheelErrorThreshold = 10;
  @Config
  public void setFlywheelErrorThreshold(int value) {
    flywheelErrorThreshold = value;
  }
  @Log
  public int flywheelLoopsToSettle = 10;
  @Config
  public void setFlywheelLoopsToSettle(int value) {
    flywheelLoopsToSettle = value;
  }

  private void configureButtonBindings() {
    // m_shooter.setDefaultCommand(
    //   new RunCommand(() -> m_shooter.stop(), m_shooter));

    leftJoystickButtonFour.whenHeld(new RunCommand(() -> {
        // For testing purposes only:
        m_serializer.setSerializer(
          direction * -1 * testSerializerSpeed);

        // m_shooter.setAccelerator(testAcceleratorSpeed);
      }))
      .whenReleased(new InstantCommand(() -> {
        m_serializer.set(0);
        // m_shooter.stopAccelerator();
      }));

    leftJoystickThumbButton
      .whenHeld(new RunCommand(() -> {
        m_intake.set(direction * testIntakeSpeed);
        m_serializer.setSerializer(
          direction * -1 * testSerializerSpeed);
      }))
      .whenReleased(new InstantCommand(() -> {
        m_intake.set(0);
        m_serializer.set(0);
      }));

      leftJoystickTrigger
      .whenHeld(
        new InstantCommand(() -> m_shooter.setFlywheel(testFlywheelSpeed), m_shooter)
        .andThen(
        new WaitCommand(1)
        )
        .andThen(  
        new WaitForFlywheelToReachSetpoint(m_shooter, this))
        .andThen(
        new InstantCommand(() -> {
            m_serializer.setSerializer(
              direction * -1 * testSerializerSpeed);

            m_shooter.setAccelerator(testAcceleratorSpeed);
          }
        )))
      .whenReleased(
        new RunCommand(() -> {
            m_serializer.set(0);
            m_shooter.stopFlywheel();
            m_shooter.stopAccelerator();
          }, m_shooter, m_serializer)
      );

    // leftJoystickTrigger
    //   .whenHeld(new SequentialCommandGroup(
    //     new InstantCommand(() -> m_shooter.setFlywheel(testFlywheelSpeed), m_shooter),
    //     new WaitForFlywheelToReachSetpoint(m_shooter, flywheelErrorThreshold, flywheelLoopsToSettle),
    //     new InstantCommand(() -> {
    //         m_serializer.setSerializer(
    //           direction * -1 * testSerializerSpeed);

    //         m_shooter.setAccelerator(testAcceleratorSpeed);
    //       }
    //     )))
    //   .whenReleased(
    //     new RunCommand(() -> {
    //         m_serializer.set(0);
    //         m_shooter.stopFlywheel();
    //         m_shooter.stopAccelerator();
    //       }, m_shooter, m_serializer)
    //     );

    // leftJoystickTrigger
    //     .whenHeld(new RunCommand(() ->
    //       m_shooter.setFlywheel(testFlywheelSpeed)))
    //     .whenReleased(new RunCommand(() -> m_shooter.stopFlywheel()));

    // leftJoystickTrigger
    //     .whenHeld(new RunCommand(() ->
    //       m_shooter.set(0, testFlywheelSpeed), m_shooter))
    //     .whenReleased(new RunCommand(() -> m_shooter.stop(), m_shooter));

    // leftJoystickTrigger
    //     .whenHeld(new RunCommand(() -> m_shooter.set(Constants.acceleratorSetpoint, Constants.flywheelSetpoint), m_shooter))
    //     .whenReleased(new RunCommand(() -> m_shooter.stop(), m_shooter));
    
    // rightJoystickTrigger
    //     .whenHeld(new Climb(m_climber, true));
  
    // rightJoystickThumbButton
    //   .whenHeld(new Climb(m_climber, false));

    // rightJoystickButtonFour
    //   .whenPressed(new ClimbToSetpoint(m_climber), false);
    
    // xboxAButton
    //   .whenHeld(new IntakePowerCells(m_intake, m_serializer, Constants.intakeSpeed, Constants.serializerSpeed))
    //   .whenReleased(new StowIntake(m_intake, m_serializer));
  } 


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
