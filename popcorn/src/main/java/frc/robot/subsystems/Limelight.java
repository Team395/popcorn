package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {

    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tv = table.getEntry("tv");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ts = table.getEntry("ts");
    NetworkTableEntry ta = table.getEntry("ta");
    int counter = 0;

    public Limelight() {
    }

    public void update() {
        // read values periodically
        double v = tv.getDouble(0.0);
        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0.0);
        double skew = ts.getDouble(0.0);
        double area = ta.getDouble(0.0);

        // post to smart dashboard periodically
        SmartDashboard.putNumber("LimelightV", v);
        SmartDashboard.putNumber("LimelightX", x);
        SmartDashboard.putNumber("LimelightY", y);
        SmartDashboard.putNumber("LimelightSkew", skew);
        SmartDashboard.putNumber("LimelightArea", area);
        SmartDashboard.putNumber("Counter", counter);
        counter++;
    }
}
