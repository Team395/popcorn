// package frc.robot.subsystems;

// import com.ctre.phoenix.motorcontrol.NeutralMode;
// import com.ctre.phoenix.motorcontrol.can.TalonFX;
// import com.revrobotics.CANEncoder;
// import com.revrobotics.CANSparkMax;
// import com.revrobotics.ControlType;
// import com.revrobotics.CANSparkMax.IdleMode;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import frc.robot.Constants;

// public class FalconShooter extends SubsystemBase{
//    TalonFX flywheelLeaderTalon = new TalonFX(Constants.shooterLeaderTalonID);
//    TalonFX flywheelFollowerTalon = new TalonFX(Constants.shooterLeaderTalonID);
//    CANSparkMax accelerator = new CANSparkMax(Constants.shooterLeaderSparkID, MotorType.kBrushless);
//    CANEncoder acceleratorEncoder;
// //    CANIDController acceleratorController;

//     public FalconShooter() {
//         accelerator.setIdleMode(IdleMode.kCoast);
        
        
//         flywheelLeaderTalon.setNeutralMode(NeutralMode.Brake);
//         flywheelFollowerTalon.setNeutralMode(NeutralMode.Brake);
        
//         flywheelLeaderTalon.setInverted(false);
//         flywheelFollowerTalon.setInverted(true);

//         flywheelFollowerTalon.follow(flywheelLeaderTalon);

//         // stop();

//         SmartDashboard.putNumber("flywheelDesiredSetpoint", 3000);
//         SmartDashboard.putNumber("flywheelSetP", 0.0005);
//         SmartDashboard.putNumber("flywheelSetI", 0);
//         SmartDashboard.putNumber("flywheelSetD", 4);
//         SmartDashboard.putNumber("flywheelSetF", 0.00019);



//     }

//     public void set(double setpoint) {
//         setpoint = SmartDashboard.getNumber("flywheelDesiredSetpoint", 1000);
//         // leaderController.setReference(setpoint, ControlType.kVelocity);
//         SmartDashboard.putNumber("flywheelSetpoint", setpoint);
    

//     }

//     public double shooterP = 0.0001;
//     public double shooterI = 0;
//     public double shooterD = 2;
//     public double shooterF = 0.00017;

//     public void updteConstants() {
//         shooterP = SmartDashboard.getNumber("setP", 0.0001);
//         shooterI = SmartDashboard.getNumber("setI", 0);
//         shooterD = SmartDashboard.getNumber("setD", 2);
//         // shooterF = SmartDashboard.getNumber("setF", 0.00017);

//     //     leaderController.setOutputRange(0, 1);
//     //     leaderController.setP(shooterP);
//     //     leaderController.setI(shooterI);
//     //     leaderController.setD(shooterD);
//     //     leaderController.setF(shooterF);
//     // }
//     // public void shoot(double speed) {
//     //     flywheelLeaderTalon.set(speed);
//     // }

