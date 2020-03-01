package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;

public class TankDrive extends CommandBase {
    private final Drivetrain m_drivetrain;
    private final RobotContainer m_robotContainer;

    public TankDrive(Drivetrain drivetrain, RobotContainer robotContainer) {
        m_drivetrain = drivetrain;
        m_robotContainer = robotContainer;
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
       SmartDashboard.putNumber("leftY", m_robotContainer.getLeftY());
       SmartDashboard.putNumber("rightY", m_robotContainer.getRightY());
        m_drivetrain.tankDrive(m_robotContainer.getLeftY()
          , m_robotContainer.getRightY());
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