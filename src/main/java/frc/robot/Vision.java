package frc.robot;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// this bot's got EYES
// 
// TODO: low: If there is time, within Photonvision, make a new Apriltag pipeline.

public class Vision {
    // ============================================== Public Variables
    // What we want the rest of the robot to know

    public boolean pipeline = false;

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
    private PhotonCamera piCamera = new PhotonCamera("Pi");
    private PhotonCamera USBcamera = new PhotonCamera("Microsoft_LifeCam_HD-3000");
    
    // ============================================= Public Functions
    public void Init(){
        // put one-time setup steps here
        USBcamera.setPipelineIndex(0);

        piCamera.setPipelineIndex(1);
    }

    // This function updates XY values for both of our cameras.
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
            
            // Make a list of current targets!
            List<PhotonTrackedTarget> targets = result.getTargets();
            
            // Look at each tag within the list 
            System.out.print("Pi-cam Tags:");
            for ( int i = 0; i < targets.size(); i++){
                
                // fetch the id of the current tag
                int id = targets.get(i).getFiducialId();
                
                //print out the tag's id
                System.out.print(i);
                System.out.print(" ");

                // we do be lookin for the one with id = 7
                if(id == 7){                    
                    tag7x = targets.get(i).getYaw();
                    tag7y = targets.get(i).getPitch();
                }
            }
            // print a new line on the Rio log
            System.out.println("");

            // get the x,y of the best target that is detected.
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

    // TODO: low: If time, fill in method here that changes pipelines on USB camera, if we want apriltags on it
    public void PiplineSelect(int pipelineIndex){
        if(pipeline == false){
            pipeline = true;
            PiplineSelect(0);
            
        }
        else if(pipeline == true){
            pipeline = false;
            PiplineSelect(1);
        }
    }

    // ============================================= Private Functions

           
}
