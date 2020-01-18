package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class Drivetrain extends SubsystemBase {
    
    //Creating a new SPARK MAX Controller
    CANSparkMax leftLeader = new CANSparkMax(RobotContainer.driveLeftLeaderMapID, MotorType.kBrushless);
    CANSparkMax leftFollower = new CANSparkMax(RobotContainer.driveLeftFollowerMapID, MotorType.kBrushless);
    CANSparkMax rightLeader = new CANSparkMax(RobotContainer.driveRightLeaderMapID, MotorType.kBrushless);
    CANSparkMax rightFollower = new CANSparkMax(RobotContainer.driveRightFollowerMapID, MotorType.kBrushless);
   
    //Creating a new Joystick and XboxController
    Joystick lefJoystick = new Joystick(0);
    Joystick righJoystick = new Joystick(1);
    XboxController xboxController = new XboxController(2);
    

    public Drivetrain(){
        leftLeader.setInverted(true);
        leftFollower.setInverted(true);
        rightLeader.setInverted(false);
        rightFollower.setInverted(false);

        leftFollower.follow(leftLeader);
        rightFollower.follow(rightLeader);
    }

    public void tankDrive(double leftSpeed, double rightSpeed){
        leftLeader.set(leftSpeed);
        rightLeader.set(rightSpeed);
    }


    @Override
    public void periodic() {
      // This method will be called once per scheduler run
    }
}
