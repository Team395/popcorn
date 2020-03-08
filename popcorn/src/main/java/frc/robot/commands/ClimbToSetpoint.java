package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class ClimbToSetpoint extends CommandBase{
    private final Climber m_climber;
    private double encoderPosition = 0.0;
    private double encoderFinalPosition = 199;
    
    public ClimbToSetpoint(final Climber climber){
        m_climber = climber;
        
        addRequirements(m_climber);
    }

    @Override
    public void initialize() {
        encoderPosition = m_climber.getEncoderPosition();
        SmartDashboard.putNumber("Encoder", encoderPosition);

    }

    @Override
    public void execute() {
        encoderPosition = m_climber.getEncoderPosition();
        SmartDashboard.putNumber("Encoder", encoderPosition);
        m_climber.spin(1.0);
    }

    @Override
    public void end(final boolean interrupted) {
        encoderPosition = m_climber.getEncoderPosition();
        SmartDashboard.putNumber("Encoder", encoderPosition);
        m_climber.spin(0.0);
    }

    @Override
    public boolean isFinished() {
        encoderPosition = m_climber.getEncoderPosition();
        if (encoderPosition >= encoderFinalPosition){
            return true;
        }
        else{
            return false;
        }
    }
}

