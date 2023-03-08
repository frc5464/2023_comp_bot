package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
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

    // Angle-snap PID
    double kP = 0.01;
    double kI = 0;
    double kD = 0.001;
    PIDController angleSnapPidController = new PIDController(kP, kI, kD);

    RelativeEncoder frontleftEncoder;
    RelativeEncoder frontrightEncoder;
    RelativeEncoder backleftEncoder;
    RelativeEncoder backrightEncoder;

    public double frontleftrotations;

    // maximum drive speed (0 to 1.0)
    double maxspeed = 1;
    double rampRate = 0.4;

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
        SmartDashboard.putNumber("gyro P Gain", kP);
        SmartDashboard.putNumber("gyro I Gain", kI);
        SmartDashboard.putNumber("gyro D Gain", kD);
    }

    public void PIDgyro(){
        double p = SmartDashboard.getNumber("gyro P Gain", 0);
        double i = SmartDashboard.getNumber("gyro I Gain", 0);
        double d = SmartDashboard.getNumber("gyro D Gain", 0);
        if((p != kP)) { angleSnapPidController.setP(p); kP = p; }
        if((i != kI)) { angleSnapPidController.setI(i); kI = i; }
        if((d != kD)) { angleSnapPidController.setD(d); kD = d; } 
    }

    // ============================================= Public Functions
    public void Move(double x,double y,double rot){
        // This could be a function that we call when we want to move the robot.
        // We will "pass in" three values from our main Robot Class,
        // And this function will use those values
        // x = forward, y = strafe, rot = rotate the bot, gyroAngle = 
        
        drivetrain.driveCartesian(x*maxspeed, y*maxspeed, rot*maxspeed);

    }

    public void MoveFieldOriented(double x,double y,double rot, Rotation2d gyroAngle){

        drivetrain.driveCartesian(x*maxspeed, y*maxspeed, rot*maxspeed, gyroAngle);
        
    }

    public double SnapToAngle(double currentAngle, double targetAngle){
        // Calculates the output of the PID algorithm based on the sensor reading
        // and sends it to the drivetrain
        double turnVal = angleSnapPidController.calculate(currentAngle, targetAngle);
        
        return turnVal;
    }

    public void DisplayStats(){
        SmartDashboard.putNumber("front left rotations", frontleftEncoder.getPosition());
        frontleftrotations = frontleftEncoder.getPosition();
    }

    public void DriveEncodersZeroed(){
        frontleftEncoder.setPosition(0);
    }

    // ============================================= Private Functions
    
}
