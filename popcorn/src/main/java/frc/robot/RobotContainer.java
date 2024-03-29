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

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.AimToTarget;
import frc.robot.commands.Climb;
import frc.robot.commands.ClimbToSetpoint;
import frc.robot.commands.DriveToTarget;
// import edu.wpi.first.wpilibj.XboxController;
// import frc.robot.commands.ColorMatch;
import frc.robot.commands.TankDrive;
import frc.robot.commands.WaitForFlywheelToReachSetpoint;
import frc.robot.commands.autonomous.DriveFeet;
import frc.robot.commands.autonomous.TurnDegrees;
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
import oi.limelightvision.limelight.frc.Limelight;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Serializer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.Servo;

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

  private final Shooter m_shooter = new Shooter(this);

  private final Intake m_intake = new Intake();
  private final Serializer m_serializer = new Serializer();

  private final Limelight m_limelight = new Limelight();

  private final Command m_autonomousCommand = getAutonomousCommand();
  
  XboxController driverController = new XboxController(0);
  JoystickButton driverXboxAButton = new JoystickButton(driverController, 1);
  JoystickButton driverXboxBButton = new JoystickButton(driverController, 2);
  JoystickButton driverXboxYButton = new JoystickButton(driverController, 4);
  JoystickButton driverXboxXButton = new JoystickButton(driverController, 3);
  
  public XboxController operatorController = new XboxController(1); 
  JoystickButton operatorXboxLButton = new JoystickButton(operatorController, 5);
  JoystickButton operatorXboxRButton = new JoystickButton(operatorController, 6);
  JoystickButton operatorXboxYButton = new JoystickButton(operatorController, 4);


  Servo climberPaul = new Servo(0);


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

    m_intake.setDefaultCommand(new RunCommand(() -> {
      var speed = Math.abs(operatorController.getY(Hand.kRight)) > Constants.kJoystickTurnDeadzone
        ? operatorController.getY(Hand.kRight)
        : 0; 
      m_intake.set(speed);
    }, m_intake));
    m_serializer.setDefaultCommand(new RunCommand(() -> {
      var speed = Math.abs(operatorController.getY(Hand.kLeft)) > Constants.kJoystickTurnDeadzone
      ? operatorController.getY(Hand.kLeft)
      : 0; 
      m_serializer.set(speed, speed);
    }, m_serializer));
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

    // m_drivetrain.updateSmartDashboard();
    m_shooter.updateSmartDashboard();
  }

  public void teleopInit() {
    m_intake.moveIntake(IntakePositions.UP);
    m_shooter.moveHood(ShooterHoodPositions.UP);
    m_drivetrain.shiftGear(DrivetrainShifterGears.LOW);

    CameraServer.getInstance().startAutomaticCapture();
  }

  public void autonomousInit() {
    m_intake.moveIntake(IntakePositions.UP);
    m_shooter.moveHood(ShooterHoodPositions.UP);
    m_drivetrain.shiftGear(DrivetrainShifterGears.LOW);

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }

    // new InstantCommand(() -> new DriveFeet(m_drivetrain, 5), m_drivetrain);
    // new DriveFeet(m_drivetrain, 5); 
  }

  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();
  }

  public double getControllerLeftTrigger() {
    return -1*driverController.getTriggerAxis(Hand.kLeft);
    // return xboxController.getRawAxis(XboxController.Axis.kLeftTrigger.value);
  }

  public double getControllerRightTrigger() {
    return -1*driverController.getTriggerAxis(Hand.kRight);
    // return xboxController.getRawAxis(XboxController.Axis.kRightTrigger.value);
  }

  public double getControllerTurn() {
    return -1*driverController.getX(Hand.kLeft);
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

  private void configureButtonBindings() {
      // driverXboxBButton.whenPressed(
      //   new InstantCommand(()-> m_shooter.toggleHoodPosition(), m_shooter));
      // driverXboxYButton.whenPressed(
      //   new InstantCommand(() -> m_drivetrain.toggleGearPosition(), m_drivetrain));

    // BATTER SHOT (B BUTTON)
    driverXboxBButton
      .whenHeld(new SequentialCommandGroup(
        new InstantCommand(() -> m_shooter.moveHood(ShooterHoodPositions.DOWN), m_shooter),
        new InstantCommand(() -> m_shooter.setFlywheel(Constants.batterShotFlywheelSetpoint), m_shooter),
        // new InstantCommand(() -> m_shooter.setFlywheelRobotContainer(), m_shooter),
        new WaitForFlywheelToReachSetpoint(m_shooter, this),
        new RunCommand(() -> {
            m_serializer.set(
              -Constants.batterShotSerializeFrontSpeed, 
              -Constants.batterShotSerializeBackSpeed
            );

            m_shooter.setAccelerator(Constants.acceleratorSetpoint);
            // m_shooter.setAcceleratorRobotContainer();
          }
        , m_shooter, m_serializer)))
      .whenReleased(
        new InstantCommand(() -> {
            m_serializer.set(0, 0);
            m_shooter.stopFlywheel();
            m_shooter.stopAccelerator();
          }, m_shooter, m_serializer)
        );

    // leftJoystickTrigger
    //     .whenHeld(new RunCommand(() -> m_shooter.set(Constants.acceleratorSetpoint, Constants.flywheelSetpoint), m_shooter))
    //     .whenReleased(new RunCommand(() -> m_shooter.stop(), m_shooter));

    
    operatorXboxRButton
        .whenHeld(new Climb(m_climber, true));
  
    operatorXboxLButton
      .whenHeld(new Climb(m_climber, false));

    operatorXboxYButton
      .whenPressed(new InstantCommand(() -> climberPaul.setAngle(90), m_climber));

    // rightJoystickButtonFour
    //   .whenPressed(new ClimbToSetpoint(m_climber), false);
    
    driverXboxAButton.whenPressed(
        new InstantCommand(() -> m_intake.toggleIntakePosition(), m_intake));
    driverXboxAButton
      .whileHeld(new IntakePowerCells(m_intake
        , m_serializer
        , this
        , Constants.intakeSpeed
        , -Constants.frontSerializerSpeed
        , -Constants.backSerializerSpeed));
    driverXboxAButton.whenReleased(new StowIntake(m_intake, m_serializer));

    // driverXboxXButton
    //       .whenPressed(new DriveFeet(m_drivetrain, 5));
    // driverXboxYButtonDriveFee
    //       .whenPressed(new DriveFeet(m_drivetrain, -5));

    // driverXboxXButton
    //       .whenPressed(new TurnDegrees(m_drivetrain, 180));
    // driverXboxYButton
    //       .whenPressed(new TurnDegrees(m_drivetrain, -180));

    // TEST DRIVE PATTERN (we think)
    // driverXboxYButton
    //       .whenPressed(new DriveFeet(m_drivetrain, 5)
    //       .andThen(new WaitCommand(0.1))
    //       .andThen(new TurnDegrees(m_drivetrain, 180))
    //       .andThen(new WaitCommand(0.1))
    //       .andThen(new DriveFeet(m_drivetrain, 5))
    //       .andThen(new WaitCommand(0.1))
    //       .andThen(new TurnDegrees(m_drivetrain, 180)));

    // AUTO SHOT
    driverXboxXButton
          .whenHeld(new AimToTarget(m_drivetrain, m_limelight)
            .andThen(new DriveToTarget(m_drivetrain, m_limelight)
            .andThen(new AimToTarget(m_drivetrain, m_limelight)
            .andThen(new SequentialCommandGroup(
              new InstantCommand(() -> m_shooter.moveHood(ShooterHoodPositions.UP), m_shooter),
              new InstantCommand(() -> m_shooter.setFlywheel(Constants.flywheelSetpoint), m_shooter),
              new WaitForFlywheelToReachSetpoint(m_shooter, this),
              new RunCommand(() -> {
                  m_serializer.set(
                    -1.0, -0.8);
                  m_shooter.setAccelerator(Constants.acceleratorSetpoint);
                }
              , m_shooter, m_serializer))))))
          .whenReleased(
            new InstantCommand(() -> {
                m_serializer.set(0, 0);
                m_shooter.stopFlywheel();
                m_shooter.stopAccelerator();
              }, m_shooter, m_serializer)
            );
  } 


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new DriveFeet(m_drivetrain, 5);
  }
}
  