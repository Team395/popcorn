package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.enums.IntakePositions;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Serializer;

public class StowIntake extends CommandBase {
    private Intake intake;
    private Serializer serializer;

    public StowIntake(Intake intakeSystem, Serializer serializerSystem) {
        intake = intakeSystem;
        serializer = serializerSystem;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(intakeSystem, serializerSystem);
      }
    
      // Called when the command is initially scheduled.
      @Override
      public void initialize() {
          intake.moveIntake(IntakePositions.UP);
          intake.set(0);
          serializer.set(0);
      }
    
      // Called every time the scheduler runs while the command is scheduled.
      @Override
      public void execute() {
      }
    
      // Called once the command ends or is interrupted.
      @Override
      public void end(boolean interrupted) {
      }
    
      // Returns true when the command should end.
      @Override
      public boolean isFinished() {
        return true;
      }
}