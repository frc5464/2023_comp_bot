package frc.robot;

import java.lang.annotation.Target;
import java.security.PublicKey;
import java.util.List;

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

    // for our best target
    public double Picamerax;
    public double Picameray;
    public boolean PihasTargets;

    // this is for tag 7 (or the opposite one)
    public double tag7x;
    public double tag7y;

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
            List<PhotonTrackedTarget> targets = result.getTargets();
            // loop through things in this list
            for ( int i = 0; i < targets.size(); i++){
                
                // we do be lookin for the one with id = 7
                if(targets.get(i).getFiducialId() == 7){
                    
                    tag7x = targets.get(i).getYaw();
                    tag7y = targets.get(i).getPitch();
                }


            }

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

        SmartDashboard.putNumber("AprilTags.tag7x", tag7x);
        SmartDashboard.putNumber("AprilTags.tag7y", tag7y);

    }
    // ============================================= Private Functions

           
}
