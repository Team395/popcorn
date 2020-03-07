/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonomous;

import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.DrivetrainConfigurators.DriveStraightConfigurator;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * An example command that uses an example subsystem.
 */
public class DriveFeet extends CommandBase {
    private Drivetrain drivetrain;
    private double distanceInFeet;
    private int _withinThresholdLoops;
    private double allowableClosedLoopErrorInches = 3
        * Constants.kRotationsPerInch
        * Constants.kLowGearMotorRotationsPerWheelRotation
        * Constants.kSensorUnitsPerRotation;

    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    // private final ExampleSubsystem m_subsystem;

    /**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public DriveFeet(Drivetrain drivetrain, double distanceInFeet) {
        this.drivetrain = drivetrain;
        this.distanceInFeet = distanceInFeet;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    _withinThresholdLoops = 0;

    var configurator = new DriveStraightConfigurator(drivetrain);
    configurator.configDrivetrainDriveStraight();

    drivetrain.zeroDistance();

    double target_sensorUnits = Constants.drivetrainForward
        * distanceInFeet
        * Constants.kSensorUnitsPerFoot;

    double target_turn = drivetrain.rightLeader.getSelectedSensorPosition(1);
    
    /* Configured for Position Closed loop on Integrated Sensors' Sum and Auxiliary PID on Pigeon's Yaw */
    drivetrain.driveStraight(target_sensorUnits, target_turn);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      /* Check if closed loop error is within the threshld */
      double drivetrainClosedLoopError = drivetrain.getClosedLoopError();
      if (drivetrainClosedLoopError < +allowableClosedLoopErrorInches &&
        drivetrainClosedLoopError > -allowableClosedLoopErrorInches) {

          ++_withinThresholdLoops;
      } else {
          _withinThresholdLoops = 0;
      }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
      drivetrain.tankDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (_withinThresholdLoops > 10);
  }
}
