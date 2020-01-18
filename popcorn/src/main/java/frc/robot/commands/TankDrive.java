package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class TankDrive extends CommandBase {
    private final Drivetrain m_drivetrain;

    public TankDrive(Drivetrain drivetrain) {
        m_drivetrain = drivetrain;
        addRequirements(m_drivetrain);
    }

     // Called when the command is initially scheduled. 
     // list of things to do
     @Override
     public void initialize() {
     }
   
     // Called every time the scheduler runs while the command is scheduled.
     @Override
     public void execute() {
         m_drivetrain.tankDrive(0.1, 0.1);
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