package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// This class encompasses the Drivetrain system
// Motor configuration and drive methods go here
// 

public class Drivetrain {
    // ============================================== Public Variables
    // What we want the rest of the robot to know

    // ============================================== Private Variables
    // What the rest of the robot does not care about

    // Motor Controllers for the Drive Train
    CANSparkMax frontleft = new CANSparkMax(2, MotorType.kBrushless);
    CANSparkMax frontright = new CANSparkMax(6, MotorType.kBrushless);
    CANSparkMax backleft = new CANSparkMax(7, MotorType.kBrushless);
    CANSparkMax backright = new CANSparkMax(5, MotorType.kBrushless);

    // Drive Train
    MecanumDrive drivetrain = new MecanumDrive(frontleft, backright, frontright, backleft);

    RelativeEncoder frontleftEncoder;
    RelativeEncoder frontrightEncoder;
    RelativeEncoder backleftEncoder;
    RelativeEncoder backrightEncoder;

    // maximum drive speed (0 to 1.0)
    double maxspeed = 1;
    double rampRate = 0.25;


    

    public void Init(){
        // put one-time setup steps here
        frontleft.setOpenLoopRampRate(rampRate);
        frontright.setOpenLoopRampRate(rampRate);
        backleft.setOpenLoopRampRate(rampRate);
        backright.setOpenLoopRampRate(rampRate);      
        
        frontleftEncoder = frontleft.getEncoder();
        frontrightEncoder = frontright.getEncoder();
        backleftEncoder = backleft.getEncoder();
        backrightEncoder = backright.getEncoder();
    }

    // ============================================= Public Functions
    public void Move(double x,double y,double rot){
        // This could be a function that we call when we want to move the robot.
        // We will "pass in" three values from our main Robot Class,
        // And this function will use those values
        // x = forward, y = strafe, rot = rotate the bot
        drivetrain.driveCartesian(x*maxspeed, y*maxspeed, rot*maxspeed);
    }

    public void DisplayStats(){
        SmartDashboard.putNumber("front left rotations", frontleftEncoder.getPosition());
        //TODO: print out other drive motor rotations!
    }

    // ============================================= Private Functions
    
}
