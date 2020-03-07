/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Set;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

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
import frc.robot.commands.autonomous.DriveFeet;
import frc.robot.commands.intake.IntakePowerCells;
import frc.robot.commands.intake.StowIntake;
import frc.robot.enums.DrivetrainShifterGears;
import frc.robot.enums.IntakePositions;
import frc.robot.enums.ShooterHoodPositions;
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

  // Joystick leftJoystick = new Joystick(3);
  // Joystick rightJoystick = new Joystick(4);


  // JoystickButton leftJoystickTrigger = new JoystickButton(leftJoystick, 1);
  // JoystickButton leftJoystickThumbButton = new JoystickButton(leftJoystick, 2);
  // JoystickButton leftJoystickButtonFour = new JoystickButton(leftJoystick, 4);

  // JoystickButton rightJoystickTrigger = new JoystickButton(rightJoystick, 1);
  // JoystickButton rightJoystickThumbButton = new JoystickButton(rightJoystick, 2);
  // JoystickButton rightJoystickButtonFour = new JoystickButton(rightJoystick, 4);
  
  XboxController xboxController = new XboxController(0);
  JoystickButton xboxAButton = new JoystickButton(xboxController, 1);
  JoystickButton xboxBButton = new JoystickButton(xboxController, 2);
  JoystickButton xboxYButton = new JoystickButton(xboxController, 4);
  JoystickButton xboxXButton = new JoystickButton(xboxController, 3);


  static final double joystickDeadzone = 0.15;
  // static final double xboxDeadzone = 0.25;

  Boolean driveDistance = false;
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
    if(m_intake.currentPosition == IntakePositions.DOWN) {
      SmartDashboard.putString("IntakePosition", "DOWN");
    } else {
      SmartDashboard.putString("IntakePosition", "UP");
    }

    if(m_shooter.currentPosition == ShooterHoodPositions.DOWN) {
      SmartDashboard.putString("ShooterHoodPosition", "DOWN");
    } else {
      SmartDashboard.putString("ShooterHoodPosition", "UP");
    }

    if(m_drivetrain.currentPosition == DrivetrainShifterGears.LOW) {
      SmartDashboard.putString("DrivetrainShifterPosition", "LOW");
    } else {
      SmartDashboard.putString("DrivetrainShifterPosition", "HIGH");
    }

    m_drivetrain.updateSmartDashboard();
  }

  public void teleopInit() {
    m_intake.moveIntake(IntakePositions.UP);
    m_shooter.moveHood(ShooterHoodPositions.UP);
    m_drivetrain.shiftGear(DrivetrainShifterGears.LOW);
  }

  public double getControllerLeftTrigger() {
    return -1*xboxController.getTriggerAxis(Hand.kLeft);
    // return xboxController.getRawAxis(XboxController.Axis.kLeftTrigger.value);
  }

  public double getControllerRightTrigger() {
    return -1*xboxController.getTriggerAxis(Hand.kRight);
    // return xboxController.getRawAxis(XboxController.Axis.kRightTrigger.value);
  }

  public double getControllerTurn() {
    return -1*xboxController.getX(Hand.kLeft);
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
  public int flywheelErrorThreshold = 100;
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

    // leftJoystickButtonFour.whenHeld(new RunCommand(() -> {
    //     // For testing purposes only:
    //     m_serializer.setSerializer(
    //       direction * -1 * testSerializerSpeed);

    //     // m_shooter.setAccelerator(testAcceleratorSpeed);
    //   }))
    //   .whenReleased(new InstantCommand(() -> {
    //     m_serializer.set(0);
    //     // m_shooter.stopAccelerator();
    //   }));

    // leftJoystickThumbButton
    //   .whenHeld(new RunCommand(() -> {
    //     m_intake.set(direction * testIntakeSpeed);
    //     m_serializer.setSerializer(
    //       direction * -1 * testSerializerSpeed);
    //   }))
    //   .whenReleased(new InstantCommand(() -> {
    //     m_intake.set(0);
    //     m_serializer.set(0);
    //   }));

    //   leftJoystickTrigger
    //   .whenHeld(
    //     new InstantCommand(() -> m_shooter.setFlywheel(testFlywheelSpeed), m_shooter)
    //     .andThen( 
    //     new WaitForFlywheelToReachSetpoint(m_shooter, this))
    //     .andThen(
    //     new WaitCommand(1))
    //     .andThen(
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
    //   );

      // xboxAButton.whenPressed(
      //   new InstantCommand(() -> m_intake.toggleIntakePosition(), m_intake));
      // xboxBButton.whenPressed(
      //   new InstantCommand(()-> m_shooter.toggleHoodPosition(), m_shooter));
      // xboxYButton.whenPressed(
      //   new InstantCommand(() -> m_drivetrain.toggleGearPosition(), m_drivetrain));

    // leftJoystickTrigger
    xboxBButton
      .whenHeld(new SequentialCommandGroup(
        new InstantCommand(() -> m_shooter.setFlywheel(Constants.flywheelSetpoint), m_shooter),
        new WaitForFlywheelToReachSetpoint(m_shooter, this),
        new InstantCommand(() -> {
            m_serializer.set(
              -1, -1);

            m_shooter.setAccelerator(4200);
          }
        )))
      .whenReleased(
        new InstantCommand(() -> {
            m_serializer.set(0, 0);
            m_shooter.stopFlywheel();
            m_shooter.stopAccelerator();
          }, m_shooter, m_serializer)
        );

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
    
    xboxAButton
      .whenHeld(new IntakePowerCells(m_intake
        , m_serializer
        , Constants.intakeSpeed
        , -Constants.frontSerializerSpeed
        , -Constants.backSerializerSpeed))
      .whenReleased(new StowIntake(m_intake, m_serializer));

    // xboxYButton.whenHeld(new InstantCommand(() -> m_shooter.set(1 * Constants.acceleratorSetpoint, 0)))
    // .whenReleased(new InstantCommand(() -> m_shooter.stopAccelerator()));
    // xboxXButton.whenHeld(new InstantCommand(() -> m_shooter.set(-1 * Constants.acceleratorSetpoint, 0)))
    // .whenReleased(new InstantCommand(() -> m_shooter.stopAccelerator()));
    
    // xboxYButton.whenHeld(new InstantCommand(() -> m_shooter.acceleratorSparkMax.set(1)))
    // .whenReleased(new InstantCommand(() -> m_shooter.stopAccelerator()));
    // xboxXButton.whenHeld(new InstantCommand(() -> m_shooter.acceleratorSparkMax.set(-1)))
    // .whenReleased(new InstantCommand(() -> m_shooter.stopAccelerator()));

    xboxXButton
          .whenPressed(new DriveFeet(m_drivetrain, 5));
    xboxYButton
          .whenPressed(new DriveFeet(m_drivetrain, -5));
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
