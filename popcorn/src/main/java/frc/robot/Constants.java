/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import io.github.oblarg.oblog.annotations.Config;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final int kSlotIdx = 0;
    public static final int kPIDLoopIdx = 0;
    public static final int kTimeoutMs = 30;

    // Constants for accelerator PID
    public static final double acceleratorP = 0.0001;
    public static final double acceleratorI = 0;
    public static final double acceleratorD = 2;
    public static final double acceleratorF = 0.00017;
  
    // Constants for flywheel PID
    public static final double flywheelP = 0.35;
    public static final double flywheelI = 0;
    public static final double flywheelD = 14.5;
    public static final double flywheelF = 0.046;

    // Setpoint for the shooter
    public static final int acceleratorSetpoint = 4200;
    // 8600 for initiation line
    public static final int flywheelSetpoint = 13000;

    // Drivetrain w/ CTRE Falcon 500s/TalonFXs
    public static final int driveLeftLeaderFalconID = 4;
    public static final int driveLeftFollowerFalconID = 3;
    public static final int driveRightLeaderFalconID = 2;
    public static final int driveRightFollowerFalconID = 1;

    // Shooter w/ CTRE Falcon 500s/TalonFXs and Rev Robotics SparkMAX/NEO
    public static final int flywheelLeaderTalonID = 10;
    public static final int flywheelFollowerTalonID = 9;
    public static final int acceleratorSparkID = 8;
    // Shooter Hood
    public static final int shooterHoodSolenoidForward = 2;
    public static final int shooterHoodSolenoidReverse = 3;

    //climber
    public static final int climberSparkID = 11;

    // Intake
    public static final int intakeRollerTalonId = 7;

    // Intake Solenoids
    public static final int intakeSolenoidLeftForward = 0;
    public static final int intakeSolenoidLeftReverse = 1;
    @Config
    public static double intakeSpeed = 0.7;

    // Serializer
    public static final int serializerLeaderSparkMaxId = 6;
    public static final int serializerFollowerSparkMaxId = 5;
    @Config
    public static double serializerSpeed = 0.5;
}