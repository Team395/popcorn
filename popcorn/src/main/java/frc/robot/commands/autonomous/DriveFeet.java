// /*----------------------------------------------------------------------------*/
// /* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
// /* Open Source Software - may be modified and shared by FRC teams. The code   */
// /* must be accompanied by the FIRST BSD license file in the root directory of */
// /* the project.                                                               */
// /*----------------------------------------------------------------------------*/

// package frc.robot.commands.autonomous;

// import frc.robot.subsystems.Drivetrain;
// import frc.robot.subsystems.ExampleSubsystem;
// import edu.wpi.first.wpilibj2.command.CommandBase;

// /**
//  * An example command that uses an example subsystem.
//  */
// public class DriveFeet extends CommandBase {    
//     private final double distance;
//     private final Drivetrain m_drivetrain;

//     // private final ExampleSubsystem m_subsystem;

//     /**
//      * Creates a new ExampleCommand.
//      *
//      * @param subsystem The subsystem used by this command.
//      */


//     public DriveFeet(final Drivetrain drivetrain) {

//         m_drivetain = drivetrain;
//         // m_subsystem = subsystem;
//         // Use addRequirements() here to declare subsystem dependencies.
//         addRequirements(drivetrain);
//     }

//     public DriveFeet(final double distance2, final boolean b) {
//         this(distance, true);
//     }

//     // Called when the command is initially scheduled.
//   @Override
//   public void initialize() {
//   }

//   // Called every time the scheduler runs while the command is scheduled.
//   @Override
//   public void execute() {
//   }

//   // Called once the command ends or is interrupted.
//   @Override
//   public void end(final boolean interrupted) {
//   }

//   // Returns true when the command should end.
//   @Override
//   public boolean isFinished() {
//     return false;
//   }
// }
