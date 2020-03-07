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
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PIDCommand;

/**
 * An example command that uses an example subsystem.
 */
public class TurnDegrees extends PIDCommand {
    private Drivetrain drivetrain;
    private double degreesToTurn;
    private int _withinThresholdLoops;
    private double allowableClosedLoopErrorDegrees = 10
        / 360
        * Constants.kPigeonUnitsPerRotation;

    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    // private final ExampleSubsystem m_subsystem;

    /**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public TurnDegrees(Drivetrain drivetrain, double degreesToTurn){
        super(new PIDController(Constants.kGains_Pigeon.kP
                    , Constants.kGains_Pigeon.kI
                    , Constants.kGains_Pigeon.kD)
            , drivetrain::getHeading
            , degreesToTurn
            , output -> drivetrain.arcadeDrive(0, output)
            , drivetrain);

        // getController().enableContinuousInput(-180, 180);

        getController()
            .setTolerance(Constants.kTurnAcceptableErrorDegrees, 10);

        this.drivetrain = drivetrain;
        // this.degreesToTurn = degreesToTurn;

    // Use addRequirements() here to declare subsystem dependencies.
    // addRequirements(drivetrain);
  }

  @Override
  public void initialize() {
      // TODO Auto-generated method stub
      super.initialize();

      this.drivetrain.zeroSensors();
  }

  @Override
  public void execute() {
      // TODO Auto-generated method stub
      super.execute();

      SmartDashboard.putNumber("closedLoopError", getController().getPositionError());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
      drivetrain.tankDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
