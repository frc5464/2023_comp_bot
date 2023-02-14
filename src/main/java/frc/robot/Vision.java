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

    public double camerax;
    public double cameray;
    public boolean hasTargets;
    // ============================================== Private Variables
    // What the rest of the robot does not care about
    
    //Camera 
    private PhotonCamera camera = new PhotonCamera("apriltags");
    
    // ============================================= Public Functions
    public void Init(){
        // put one-time setup steps here

    }

    public void ReturnBestTargetXY(){
        var result = camera.getLatestResult();
        hasTargets = result.hasTargets();
        
        if(hasTargets){
          PhotonTrackedTarget target = result.getBestTarget();
          cameray = target.getPitch();
          camerax = target.getYaw();
        }
    }

    public void changeVisionType(String selection){
        if(selection == "reflective"){
            camera.setPipelineIndex(1);
        }
        else if(selection == "apriltags"){
            camera.setPipelineIndex(0);
        }
        else{
            System.out.println("wrong vision type selected!");
        }       
    }

    public void DisplayStats(){
        SmartDashboard.putBoolean("Target?", hasTargets);
        SmartDashboard.putNumber("targetx", camerax);
        SmartDashboard.putNumber("targety", cameray);
    }
    // ============================================= Private Functions
           
}
