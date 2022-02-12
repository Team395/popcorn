package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.enums.IntakePositions;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Serializer;

public class IntakePowerCells extends CommandBase {
    private Intake intake;
    private Serializer serializer;
    private double intakeSpeedToSet;
    private double frontSerializerSpeedToSet;
    private double backSerializerSpeedToSet;
	  private RobotContainer robotContainer;

    public IntakePowerCells(Intake intakeSystem
            , Serializer serializerSystem
            , RobotContainer robotContainer
            , double intakeSpeedToSet
            , double frontSerializerSpeedToSet
            , double backSerializerSpeedToSet) {
        intake = intakeSystem;
        serializer = serializerSystem;
		    this.robotContainer = robotContainer;
        this.intakeSpeedToSet = intakeSpeedToSet;
        this.frontSerializerSpeedToSet = frontSerializerSpeedToSet;
        this.backSerializerSpeedToSet = backSerializerSpeedToSet;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(intakeSystem, serializerSystem);
      }
    
      // Called when the command is initially scheduled.
      @Override
      public void initialize() {
          // intake.moveIntake(IntakePositions.DOWN);
      }
    
      // Called every time the scheduler runs while the command is scheduled.
      @Override
      public void execute() {
        var intakeSpeed = Math.abs(robotContainer
            .operatorController.getRightY()) > Constants.kJoystickTurnDeadzone
          ? robotContainer.operatorController.getRightY()
          : intakeSpeedToSet;

        var frontSerializerSpeed = Math.abs(robotContainer.operatorController.getLeftY()) > Constants.kJoystickTurnDeadzone
          ? robotContainer.operatorController.getLeftY()
          : frontSerializerSpeedToSet;

        var backSerializerSpeed = Math.abs(robotContainer.operatorController.getLeftY()) > Constants.kJoystickTurnDeadzone
          ? robotContainer.operatorController.getLeftY()
          : backSerializerSpeedToSet;


        intake.set(intakeSpeed);
        serializer.set(frontSerializerSpeed, backSerializerSpeed);
      }
    
      // Called once the command ends or is interrupted.
      @Override
      public void end(boolean interrupted) {
      }
    
      // Returns true when the command should end.
      @Override
      public boolean isFinished() {
        return false;
      }
}