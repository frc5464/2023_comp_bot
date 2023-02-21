package frc.robot;

import java.lang.annotation.Target;
import java.security.PublicKey;

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
    public boolean USBhasTargets;

    public double Picamerax;
    public double Picameray;
    public boolean PihasTargets;

    // ============================================== Private Variables
    // What the rest of the robot does not care about
    
    //Camera 
    private PhotonCamera piCamera = new PhotonCamera("apriltags");
    private PhotonCamera USBcamera = new PhotonCamera("reflective");
    
    // ============================================= Public Functions
    public void Init(){
        // put one-time setup steps here
        USBcamera.setPipelineIndex(0);

        piCamera.setPipelineIndex(1);
    }

    public void ReturnBestTargetXY(){
        var result = USBcamera.getLatestResult();
        USBhasTargets = result.hasTargets();
        
        if(USBhasTargets){
          PhotonTrackedTarget target = result.getBestTarget();
          USBcameray = target.getPitch();
          USBcamerax = target.getYaw();
        }

        var Piresult = piCamera.getLatestResult();
        PihasTargets = Piresult.hasTargets();

        if(PihasTargets){
            PhotonTrackedTarget Pitarget = Piresult.getBestTarget();
            Picameray = Pitarget.getPitch();
            Picamerax = Pitarget.getYaw();
        }

    }


    public void DisplayStats(){
        SmartDashboard.putBoolean("ReflectiveTape.Target?", USBhasTargets);
        SmartDashboard.putNumber("ReflectiveTape.targetx", USBcamerax);
        SmartDashboard.putNumber("ReflefctiveTape.targety", USBcameray);

        SmartDashboard.putBoolean("AprilTags.Target?", PihasTargets);
        SmartDashboard.putNumber("AprilTags.targetx", Picamerax);
        SmartDashboard.putNumber("AprilTags.targety", Picameray);
    }
    // ============================================= Private Functions

           
}
