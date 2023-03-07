package frc.robot;

public class Auto{
    //5464-created classes!    
    Drivetrain drivetrain = new Drivetrain();
    Elevator elevator = new Elevator();
    Gyro gyro = new Gyro();
    Leds Leds = new Leds();
    Vacuum intake = new Vacuum();
    Vision vision = new Vision();  
    Pneumatics pneumatics = new Pneumatics();
    Robot robot = new Robot();

    // This actually works! We can grab our 'gyro' instance from our main robot program!
    // That means all of the autonomous code could actually go into here!
    // TODO: refactor all of our autonomous code into this file, or separate ones!
    Gyro ourGyro = robot.gyro;

    public int autoStep = 0;
    String score_preset_selected = "";
   //Presets for cone scoring
   private static final String kHigh = "High";
   private static final String kMid = "Mid";
   private static final String kLow = "Low";


       // Angles the bot so it can score
  public void scorePrep(){
    
    // TODO: HIGH: Verify that these new scoreprep variables work! Everything should work the same.

    // The X value we will be homing to
    double targetX = 5;

    // INCREASE this value to make us home faster, but possibly less stable. Decrease if overshooting.
    double divisor = 50;

    // How wide of a range are we going to be looking for when homing? DECREASE to look for smaller window.
    double window = 3;

    // The value we are fetching from Photonvision
    double xcord = vision.USBcamerax;

    drivetrain.Move(0, 0, (vision.USBcamerax-targetX)/divisor); 

    if ((xcord < (targetX + window)) && (xcord > (targetX - window))){
      autoStep++;
      robot.autoTimer.start();
    }
  }

  public void EscapePrep(){
    // What exact X value are we trying to home in on?
    // TODO: HIGH: This was not homing correctly on Saturday! Is x = 23 too far to travel?
    // TODO: HIGH: Check the physical camera alignment is correct (x = 0 when aiming directly at post)

    // The X value we will be homing to
    double targetX = 23;

    // INCREASE this value to make us home faster, but possibly less stable. Decrease if overshooting.
    double divisor = 50;

    // How wide of a range are we going to be looking for when homing? DECREASE to look for smaller window.
    double window = 3;

    // The value we are fetching from Photonvision
    double xcord = vision.USBcamerax;

    drivetrain.Move(0, 0, (vision.USBcamerax-targetX)/divisor); 

    if ((xcord < (targetX + window)) && (xcord > (targetX - window))){
      autoStep++;
    }
  }
 
  public void sConeEl(){

    switch (score_preset_selected) {
      
      case kHigh:
        elevator.setElevatorPosition("ScoreHighCone");
        autoStep++;
        break;

      case kMid:
        elevator.setElevatorPosition("ScoreHScoreMidCone");
        autoStep++;
        break;

      case kLow:
        elevator.setElevatorPosition("ScoreLowCone");
         autoStep++;
        break;
    }    

  }

  public void Score(){  
    // TODO: HIGH: Verify that this turns on/off intake correctly. Adjust timer if needed.
    
    // after shortly running the intake, then move on.
    if(robot.autoTimer.get() > 2){
      intake.stoprun();
      autoStep++;
    }
    
    // run the intake (a few seconds into auto) to spit out the cone
    else if(robot.autoTimer.get() > 1){
      intake.outrun();      
    }
  }
  
  public void TokyoEscape(){
    drivetrain.Move(-0.5,0 , 0);
    System.out.println(drivetrain.frontleftrotations);
    if(drivetrain.frontleftrotations < -58.0){
      elevator.setElevatorPosition("AprilTagEncoder");
      robot.TargetYaw = gyro.Yaw;
      autoStep++;
    }
  }

  public void HitchEscape(){   // This one is different than the rest
    drivetrain.Move(-0.5, 0, 0);
    //TODO: find encoder values
      if(drivetrain.frontleftrotations > 0){
        autoStep++;
      }
    
 }

  public void FadeEscape(){
    drivetrain.Move(0.5,0 , 0);
    if(drivetrain.frontleftrotations < -58.0){
      elevator.setElevatorPosition("AprilTagEncoder");
      robot.TargetYaw = gyro.Yaw;
      autoStep++;
    }
  }

  public void FirstEscape(){
    drivetrain.Move(-0.5, 0, 0);
    if(drivetrain.frontleftrotations < -58.0){
      robot.TargetYaw = gyro.Yaw;
      elevator.setElevatorPosition("Drive");
      autoStep++;
    }
  }

  public void SecondEscape(){
    drivetrain.Move(-0.5, 0, 0);
    if(drivetrain.frontleftrotations < -58.0){
      robot.TargetYaw = gyro.Yaw;
      elevator.setElevatorPosition("Drive");
      autoStep++;
    }
  }

  public void ThirdEscape(){
    drivetrain.Move(-0.5, 0, 0);
    if(drivetrain.frontleftrotations < -58.0){
      robot.TargetYaw = gyro.Yaw;
      elevator.setElevatorPosition("Drive");
      autoStep++;
    }
  }

  public void Spin180Gyro(){
    //Gyro will preform a 180
    //TODO: varify degree and roll direction
  drivetrain.Move(0, 0, 0.5);
    if(gyro.Yaw > 180){
      autoStep++;
    }
  }

  public void ConeDetect(){
    //TODO: find distance from a cone
    if(intake.dist > 0){ 
    intake.inrun();
    elevator.setElevatorPosition("ScoreLowConeCube"); 
    robot.intakeTimer.reset();
    robot.intakeTimer.start();
    autoStep++;
    }
  }

  public void IntakeRun(){
    //TODO: verify time
    if(robot.intakeTimer.get() < 2){
      IntakeRun();
    }
    else if(robot.intakeTimer.get() > 2){
      intake.stoprun();
      robot.intakeTimer.stop();
      autoStep++;
    }
  }

  public void FirstFormation(){

  }

  public void SecondFormation(){

  }

  public void ThirdFormation(){

  }

  public void TokyoDrift(){
    drivetrain.Move(0, -0.5, (robot.startingYAW-gyro.Yaw)/100);
    System.out.println(drivetrain.frontleftrotations);
    if(drivetrain.frontleftrotations < -133){  
      autoStep++;
    }
  }
  
  public void FadeDrift(){
    drivetrain.Move(0, 0.5, (robot.startingYAW-gyro.Yaw)/100);
    //TODO: find encoder values
      if(drivetrain.frontleftrotations > 0){ 
        autoStep++;
      }
  }

  public void Arrival(){
    // This is currently instantly skipped because tag7x/y registers 0,0.
    // TODO: low: Get tag 7 to actually read out with the vision system, or switch this to Gyro lineup.

    double x = vision.tag7x;
    double y = vision.tag7y;

    if((x < 2) && (x > -2) && (y < 2) && (y > -2)){
      elevator.setElevatorPosition("Drive");
      autoStep++;
    }
  }

  public void Gunit(){
    drivetrain.Move(0.7, 0 , 0);
    System.out.println(drivetrain.frontleftrotations);
    if(drivetrain.frontleftrotations > -54){
      autoStep++;
      robot.balanceTimer.stop();
      robot.balanceTimer.reset();
      robot.balanceTimer.start();
    }
  }

  public void Balance(){
    //When pitch ~ 0 then stop
    if((gyro.Pitch < 1)&&(gyro.Pitch > -1)){
      drivetrain.Move(0,0 ,0 );
      if(robot.balanceTimer.get() > 2.0){
        autoStep++;
        robot.balanceTimer.stop();
       }
    }
    
    if(gyro.Pitch < 0){
      drivetrain.Move(0.2,0 ,0 );
      robot.balanceTimer.reset();
    }
      
    if(gyro.Pitch > 0){
      drivetrain.Move(-0.2,0 ,0 );
      robot.balanceTimer.reset();
    }
  }

  public void Generic_Backup(){
    drivetrain.Move(-0.5, 0, 0);
      if(drivetrain.frontleftrotations < -58.0){
        robot.TargetYaw = gyro.Yaw;
        autoStep++;
      }
  }

  // This autonomous routine is for a start in front of a cone-scoring post
  // It scores a cone, then zooms around the charging station
  // It then drives us onto the charging station, keeping us there with a gyro/brake

  // This autonomous routine starts in the right position, scores a cone,
  // backs up past the charge station, strafes right, and drives back on it
  public void AutoTokyoDrift(){
    switch(autoStep){
      case 0:
        sConeEl();
        break;
      case 1:
        scorePrep();
        break;
      case 2:
        Score();
        break;
      case 3:
        EscapePrep();
        break;
      case 4:
        TokyoEscape();
        break;
      case 5:
        TokyoDrift();
        break;
      case 6: 
        Arrival();
        break;
      case 7:
        Gunit();
        break;
      case 8:
        Balance();
        break;
      case 9:
        break;
      default:
        drivetrain.Move(0, 0, 0);
    }
  }

  // This autonomous routine starts anywhere in front of a cone scoring location
  // It drives forward, scores, backs out of community
  // TODO: HIGH: Finish Default auto steps. Once this works, Tokyo Drift will have a more solid start.
  public void AutoDefault(){
    switch(autoStep){
      case 0:
        sConeEl();
      break;
      case 1:
        scorePrep();
      break;
      case 2:
        Score();
      break;
      case 3:
        Generic_Backup();
      break;
      case 8:
        break;
    }
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
    
      public void IntakeDead(){
      }
    
      public void HitchDrift(){
      }

    // This class may end up containing our autonomous code. For now, non-implemented things go here.
    // This should help clean up the code until the team makes one good routine.
 
    
      public void AutoSecondScore(){
        switch(autoStep){
          case 0:
            sConeEl();
            break;
          case 1:
            scorePrep();
            break;
          case 2:
            Score();
            break;
          case 3:
            SecondEscape();
            break;
          case 4: 
            Spin180Gyro();
            break;
          case 5: 
            ConeDetect();
            break;
          case 6:
            IntakeRun();
            break;
          case 7: 
            IntakeDead();
            break;
          case 8:
            Spin180Gyro();
            break;
          case 9: 
            SecondFormation();
            break;
          case 10:
            scorePrep();
            break;
          case 11:
            sConeEl();
            break;
          case 12:
            Score();  
            break;
          case 13:
            break;
        }
      }
    
      public void AutoThirdScore(){
        switch(autoStep){
          case 0:
            sConeEl();
            break;
          case 1:
            scorePrep();
            break;
          case 2:
            Score();
            break;
          case 3:
            ThirdEscape();
            break;
          case 4: 
            Spin180Gyro();
            break;
          case 5: 
            ConeDetect();
            break;
          case 6:
            IntakeRun();
            break;
          case 7: 
            IntakeDead();
            break;
          case 8:
            Spin180Gyro();
            break;
          case 9: 
            ThirdEscape();
            break;
          case 10:
            scorePrep();
            break;
          case 11:
            sConeEl();
            break;
          case 12:
            Score();  
            break;
          case 13:
            break;
        }
      }

        // This autonomous routine starts in the middle position, scores a cone,
  // backs up past the charge station, and drives back on it
  public void AutoHitchRoute(){
    switch(autoStep){
      case 0:
        sConeEl();
        break;
      case 1:
        scorePrep();
        break;
      case 2:
        Score();
        break;
      case 3:
        HitchEscape();
        break;
      case 4:
        HitchDrift();
        break;
      case 5: 
        Arrival();
        break;
      case 6:
        Gunit();
        break;
      case 7:
        Balance();
        break;
      case 8:
        break;
    }
  }

  // This autonomous routine starts in the left position, scores a cone,
  // backs up past the charge station, strafes to the left, and drives back on it
  public void AutoFadeAway(){
    switch(autoStep){
      case 0:
        sConeEl();
        break;
      case 1:
        scorePrep();
        break;
      case 2:
        Score();
        break;
      case 3:
        FadeEscape();
        break;
      case 4:
        FadeDrift();
        break;
      case 5: 
        Arrival();
        break;
      case 6:
        Gunit();
        break;
      case 7:
        Balance();
        break;
      case 8:
        break;
    }
  }
}
