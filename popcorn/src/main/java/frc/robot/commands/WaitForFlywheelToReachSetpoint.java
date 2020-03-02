package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shooter;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

public class WaitForFlywheelToReachSetpoint extends CommandBase implements Loggable {
    private Shooter m_shooter;
    private int kErrThreshold = 10; // how many sensor units until its close-enough
    private int kLoopsToSettle = 10; // how many loops sensor must be close-enough
    @Log
    private int _withinThresholdLoops = 0;
    private RobotContainer m_robotContainer;



    public WaitForFlywheelToReachSetpoint(
        final Shooter shooter,
        RobotContainer robotContainer) {
        m_shooter = shooter;
        m_robotContainer = robotContainer;
        addRequirements(m_shooter);
    }

    @Override
    public void initialize() {
        _withinThresholdLoops = 0;
    }

    @Override
    public void execute() {
        /* Check if closed loop error is within the threshld */
        int flywheelClosedLoopError = m_shooter.getFlywheelClosedLoopError();
        if (flywheelClosedLoopError < +m_robotContainer.flywheelErrorThreshold &&
            flywheelClosedLoopError > -m_robotContainer.flywheelErrorThreshold) {

            ++_withinThresholdLoops;
        } else {
            _withinThresholdLoops = 0;
        }

        SmartDashboard.putNumber("clError", flywheelClosedLoopError);
        SmartDashboard.putNumber("wtLoops", _withinThresholdLoops);
        SmartDashboard.putNumber("thresh", kErrThreshold);
        SmartDashboard.putNumber("loops", kLoopsToSettle);
    }

    @Override
    public void end(final boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return (_withinThresholdLoops > m_robotContainer.flywheelLoopsToSettle);
    }
    
}