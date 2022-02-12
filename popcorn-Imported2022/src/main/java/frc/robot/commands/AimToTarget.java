package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;
import oi.limelightvision.limelight.frc.Limelight;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AimToTarget extends PIDCommand {
    private Drivetrain drivetrain;
    private Limelight limelight;

  public AimToTarget(Drivetrain drivetrain, Limelight limelight) {
    super(new PIDController(Constants.kGains_Limelight_Turn.kP
            , Constants.kGains_Limelight_Turn.kI
            , Constants.kGains_Limelight_Turn.kD)
          , limelight::getdegRotationToTarget
          , 0
          , output -> drivetrain.arcadeDrive(0, output)
          , drivetrain);
    this.drivetrain = drivetrain;
    this.limelight = limelight;

    getController()
            .setTolerance(Constants.kTurnAcceptableErrorDegrees);

    addRequirements(drivetrain);
  }

  @Override
  public void execute() {
      super.execute();

      SmartDashboard.putNumber("limelightError", getController().getPositionError());
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
    return !limelight.getIsTargetFound() || getController().atSetpoint();
  }
}

