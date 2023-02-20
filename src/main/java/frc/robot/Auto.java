package frc.robot;

public class Auto {
    //5464-created classes!    
    Drivetrain drivetrain = new Drivetrain();
    Elevator elevator = new Elevator();
    Gyro gyro = new Gyro();
    Leds Leds = new Leds();
    Vacuum intake = new Vacuum();
    Vision vision = new Vision();  
    Pneumatics pneumatics = new Pneumatics();

    private int autoStep = 0;
    String score_preset_selected = "";
   //Presets for cone scoring
   private static final String kHigh = "High";
   private static final String kMid = "Mid";
   private static final String kLow = "Low";

    public boolean scorePrep(){
        // flag indicating we are lined up
        boolean ready = false;
    
        

        double xcord = vision.USBcamerax;
        double ycord = vision.USBcameray;
        //vision.changeVisionType("reflective"); 
    
        if ((xcord < 2) && (xcord > -2) && (ycord < 2) && (ycord > -2)){
        ready = true;
        }
    
        // tell the parent routine if we are ready to move on
        return ready;
      }
     
      public boolean sConeEl(){
        // flag indicating elevator height and extension are lined up
        boolean ready = false;
    
        // checking elevator encoder
        // checking extension encoder
    
        switch (score_preset_selected) {
          
          case kHigh:
            elevator.setElevatorPosition("ScoreHighCone");
              ready = true;
            break;
    
          case kMid:
            elevator.setElevatorPosition("ScoreHScoreMidCone");
              ready = true;
            break;
    
          case kLow:
            elevator.setElevatorPosition("ScoreLowCone");
              ready = true;
            break;
        }    
        return ready;
      }
    
      public boolean Score(){
        // flag indicating cone has been dropped
        boolean ready = false;
        // release cone motors until encoders read a certain value = cone is dropped
        // set ready to true once conditions are met
        return ready;
      }
      
      public boolean TokyoEscape(){
        // flag indicates if we have moved out of the community
        boolean ready = false;
        return ready;
      }
    
      public boolean HitchEscape(){
        // flag indicating 
        boolean ready = false;
        return ready;
     }
    
      public boolean FadeEscape(){
        // flag indicating 
        boolean ready = false;
        return ready;
      }
    
      public boolean FirstEscape(){
        // flag indicating 
        boolean ready = false;
        return ready;
      }
    
      public boolean SecondEscape(){
        // flag indicating 
        boolean ready = false;
        return ready;
      }
    
      public boolean ThirdEscape(){
        // flag indicating 
        boolean ready = false;
        return ready;
      }
    
      public boolean Spin180Gyro(){
        //Gyro will preform a 180
        boolean ready = false;
        return ready;
      }
    
      public boolean ConeDetect(){
        boolean ready = false;
        return ready;
      }
    
      public boolean IntakeRun(){
        boolean ready = false;
        return ready;
      }
    
      public boolean IntakeDead(){
        boolean ready = false;
        return ready;
      }
    
      public boolean FirstFormation(){
        boolean ready = false;
        return ready;
      }
    
      public boolean SecondFormation(){
        boolean ready = false;
        return ready;
      }
    
      public boolean ThirdFormation(){
        boolean ready = false;
        return ready;
      }
    
      public boolean TokyoDrift(){
        boolean ready = false;
        return ready;
      }
    
      public boolean HitchDrift(){
        boolean ready = false;
        return ready;
      }
      
      public boolean FadeDrift(){
        boolean ready = false;
        return ready;
      }
    
      public boolean Arrival(){
        boolean ready = false;
        return ready;
      }
    
      public boolean Gunit(){
        boolean ready = false;
        return ready;
      }
    
      public boolean Balance(){
        boolean ready = false;
    
     //IMPORTANT: WANTED TO ADD ALL CANSPARKS TOGETHER IN ONE VARIBLE BUT IT DID NOT WORK 
        //What this mean yo. Just drive with the drivetrain instance *cries*
    
        //When pitch ~ 0 then stop
      // if(navx.getPitch() == 0){
      //   frontleft.set(0);
      //   backleft.set(0); 
      //   frontright.set(0);
      //   backright.set(0);
      // }
       
      // //When pitch > 0 then move forward
      // if(navx.getPitch() > 0){
      //   frontleft.set(1);
      //   backleft.set(1);
      //   frontright.set(1);
      //   backright.set(1);
      // }
        
      // //When pitch < 0 then move backward
      // if(navx.getPitch() < 0){
      //   frontleft.set(-1);
      //   backleft.set(-1);
      //   frontright.set(-1);
      //   backright.set(-1);
      // }
        return ready;
        //Focus on pitch when level value reads around 0
      }
    
      public boolean Generic_Backup(){
        boolean ready = false;
        return ready;
      }

    // This class may end up containing our autonomous code. For now, non-implemented things go here.
    // This should help clean up the code until the team makes one good routine.
    public void AutoFirstScore(){
        boolean ready = false;
        switch(autoStep){
          case 0:
            ready = scorePrep();
    
          case 1:
            ready = sConeEl();
    
          case 2:
            ready = Score();
    
          case 3:
            ready = FirstEscape();
    
          case 4: 
            ready = Spin180Gyro();
    
          case 5: 
            ready = ConeDetect();
    
          case 6:
            ready = IntakeRun();
    
          case 7: 
            ready = IntakeDead();
    
          case 8:
            ready = Spin180Gyro();
    
          case 9: 
            ready = FirstFormation();
    
          case 10:
            ready = scorePrep();
    
          case 11:
            ready = sConeEl();
    
          case 12:
            ready = Score();
    
          case 13:
            break;
        }
        if(ready){
          ready = false;
          autoStep++;
        }
      }
    
      public void AutoSecondScore(){
        boolean ready = false;
        switch(autoStep){
          case 0:
            ready = scorePrep();
    
          case 1:
            ready = sConeEl();
    
          case 2:
            ready = Score();
    
          case 3:
            ready = SecondEscape();
    
          case 4: 
            ready = Spin180Gyro();
    
          case 5: 
            ready = ConeDetect();
    
          case 6:
            ready = IntakeRun();
    
          case 7: 
            ready = IntakeDead();
    
          case 8:
            ready = Spin180Gyro();
    
          case 9: 
            ready = SecondFormation();
    
          case 10:
            ready = scorePrep();
    
          case 11:
            ready = sConeEl();
    
          case 12:
            ready = Score();  
    
          case 13:
            break;
        }
        // if an autonomous step is complete, move on to the next one!
        if(ready){
          ready = false;
          autoStep++;
        }
      }
    
      public void AutoThirdScore(){
        boolean ready = false;
        switch(autoStep){
          case 0:
            ready = scorePrep();
    
          case 1:
            ready = sConeEl();
    
          case 2:
            ready = Score();
    
          case 3:
            ready = ThirdEscape();
    
          case 4: 
            ready = Spin180Gyro();
    
          case 5: 
            ready = ConeDetect();
    
          case 6:
            ready = IntakeRun();
    
          case 7: 
            ready = IntakeDead();
    
          case 8:
            ready = Spin180Gyro();
    
          case 9: 
            ready = ThirdEscape();
    
          case 10:
            ready = scorePrep();
    
          case 11:
            ready = sConeEl();
    
          case 12:
            ready = Score();  
    
          case 13:
            break;
        }
        if(ready){
          ready = false;
          autoStep++;
        }
      }

        // This autonomous routine starts in the middle position, scores a cone,
  // backs up past the charge station, and drives back on it
  public void AutoHitchRoute(){
    boolean ready = false;
    switch(autoStep){
      case 0:
        ready = scorePrep();

      case 1:
        ready = sConeEl();

      case 2:
        ready = Score();

      case 3:
        ready = HitchEscape();

      case 4:
        ready = HitchDrift();

      case 5: 
        ready = Arrival();

      case 6:
        ready = Gunit();

      case 7:
        ready = Balance();

      case 8:
        break;
    }
    // if an autonomous step is complete, move on to the next one!
    if(ready){
      ready = false;
      autoStep++;
    }
  }

  // This autonomous routine starts in the left position, scores a cone,
  // backs up past the charge station, strafes to the left, and drives back on it
  public void AutoFadeAway(){
    boolean ready = false;
    switch(autoStep){
      case 0:
        ready = scorePrep();

      case 1:
        ready = sConeEl();

      case 2:
        ready = Score();

      case 3:
        ready = FadeEscape();

      case 4:
        ready = FadeDrift();

      case 5: 
        ready = Arrival();

      case 6:
        ready = Gunit();

      case 7:
        ready = Balance();

      case 8:
        break;
    }
    if(ready){
      ready = false;
      autoStep++;
    }
  }
}
