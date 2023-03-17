package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// The Gyro subsystem is NOT to be EATEN
// 
// 

public class Gyro {
    // ============================================== Public Variables
    // What we want the rest of the robot to know


    // ============================================== Private Variables
    // What the rest of the robot does not care about
    AHRS navx = new AHRS();
    
    public double Yaw;
    public double Pitch;
    public Rotation2d Angle;
    public double RawX;

    // ============================================= Public Functions
    public void Init(){
        // put one-time setup steps here

    }

    public void UpdateGyro(){
        Yaw = navx.getYaw();
        Pitch = navx.getPitch();
        Angle = navx.getRotation2d();
        RawX = navx.getRawGyroX();
    }

    // Corrects slow drift of the gyro. Shoudn't be necessary during matches
    public void ResetGyro(){
        navx.reset();
    }

    public void DisplayStats(){
        SmartDashboard.putNumber("Yaw", navx.getYaw());
        SmartDashboard.putNumber("Roll", navx.getRoll());
        SmartDashboard.putNumber("Pitch", navx.getPitch());
        SmartDashboard.putNumber("RawX", navx.getRawGyroX());
        SmartDashboard.putNumber("RawY", navx.getRawGyroY());
        SmartDashboard.putNumber("RawZ", navx.getRawGyroZ());
        SmartDashboard.putNumber("dispX", navx.getDisplacementX());
        SmartDashboard.putNumber("dispY", navx.getDisplacementY());
        SmartDashboard.putNumber("dispZ", navx.getDisplacementZ());    

    }
    // ============================================= Private Functions
         
}

    // DO NOT RUN THIS EVERY TIME! ONLY WHEN IT REEEEALLY NEEDS TO BE RUN!
    //navx.calibrate();
