package frc.robot;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// this bot's got EYES
// 
// 

public class Vision {
    // ============================================== Public Variables
    // What we want the rest of the robot to know

    public double USBcamerax;
    public double USBcameray;
    public boolean hasTargets;
    // ============================================== Private Variables
    // What the rest of the robot does not care about
    
    //Camera 
    private PhotonCamera piCamera = new PhotonCamera("apriltags");
    private PhotonCamera USBcamera = new PhotonCamera("reflective");
    
    // ============================================= Public Functions
    public void Init(){
        // put one-time setup steps here
        USBcamera.setPipelineIndex(0);
    }

    public void ReturnBestTargetXY(){
        var result = USBcamera.getLatestResult();
        hasTargets = result.hasTargets();
        
        if(hasTargets){
          PhotonTrackedTarget target = result.getBestTarget();
          USBcameray = target.getPitch();
          USBcamerax = target.getYaw();
        }
    }


    public void DisplayStats(){
        SmartDashboard.putBoolean("Target?", hasTargets);
        SmartDashboard.putNumber("targetx", USBcamerax);
        SmartDashboard.putNumber("targety", USBcameray);
    }
    // ============================================= Private Functions

           
}
