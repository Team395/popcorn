package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
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

     public void GTADrive(double leftTrigger, double rightTrigger, double turn) {
        if (-Constants.kJoystickTurnDeadzone <= turn
            && turn <= Constants.kJoystickTurnDeadzone) {
            turn = 0.0;
        }

        turn = Math.abs(turn) < Constants.kJoystickTurnDeadzone
          ? 0.0
          : (turn - Math.signum(turn) * Constants.kJoystickTurnDeadzone) / (1.0 - Constants.kJoystickTurnDeadzone);
    
        
        double left = rightTrigger - leftTrigger;
        double right = rightTrigger - leftTrigger;

        // left = left * left * Math.signum(left);
        // right = right * right * Math.signum(right);

        // turn smoothing
        turn = turn * turn * Math.signum(turn);
    
        // if(left > 0) { turn *= -1.0; }

        left += turn;
        right -= turn;

        left = Math.min(1.0, Math.max(-1.0, left));
        right = Math.max(-1.0, Math.min(1.0, right));

        left = left * 2/3;
        right = right * 2/3;
    
        m_drivetrain.tankDrive(left, right);
        SmartDashboard.putNumber("left", left);
        SmartDashboard.putNumber("right", right);
    }

    public void traditionalTankDrive() {
      // SmartDashboard.putNumber("leftY", m_robotContainer.getLeftY());
      // SmartDashboard.putNumber("rightY", m_robotContainer.getRightY());
      //  m_drivetrain.tankDrive(m_robotContainer.getLeftY()
      //    , m_robotContainer.getRightY());   
    }
  
   
     // Called every time the scheduler runs while the command is scheduled.
     @Override
     public void execute() {
      //  traditionalTankDrive();
      GTADrive(m_robotContainer.getControllerLeftTrigger(),
               m_robotContainer.getControllerRightTrigger(),
               m_robotContainer.getControllerTurn());
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