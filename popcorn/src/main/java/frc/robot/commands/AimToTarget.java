package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;
import oi.limelightvision.limelight.frc.LimeLight;
import edu.wpi.first.wpilibj.controller.PIDController;

/**
 * An example command that uses an example subsystem.
 */
public class AimToTarget extends PIDCommand {
    private Drivetrain drivetrain;
    private LimeLight limelight;

  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  // private final ExampleSubsystem m_subsystem;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public AimToTarget(Drivetrain drivetrain, LimeLight limelight) {
        super(new PIDController(Constants.kGains_LimeLight.kP
                                , Constants.kGains_LimeLight.kI
                                , Constants.kGains_LimeLight.kD)
        , limelight::getdegRotationToTarget
        , limelight.getIsTargetFound() ? limelight.getdegRotationToTarget() : 0.0
        , output -> drivetrain.arcadeDrive(0, output)
        , drivetrain);

    this.drivetrain = drivetrain;
    this.limelight = limelight;

    getController()
      .setTolerance(Constants.kAimToTargetAcceptableErrorDegrees, 10);

    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    super.execute();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    drivetrain.tankDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}

