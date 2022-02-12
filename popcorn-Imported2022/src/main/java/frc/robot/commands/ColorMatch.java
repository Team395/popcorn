package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ColorSensor;


public class ColorMatch extends CommandBase{
    private final ColorSensor m_colorSensor;

    public ColorMatch(ColorSensor colorSensor) {
        m_colorSensor = colorSensor;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(colorSensor);
    }
     // Called when the command is initially scheduled. 
     // list of things to do
    @Override
    public void initialize() {
    }
  
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        String colorString = m_colorSensor.getColor();
        SmartDashboard.putString("Detected Color", colorString);
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