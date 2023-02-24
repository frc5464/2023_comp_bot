// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */

  public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  boolean elManualMode = true;
  boolean zeroed = false;
  Timer autoTimer = new Timer();

  //Joystick
  Joystick stick = new Joystick(0);
  Joystick stick2 = new Joystick(1);

  PowerDistribution PDThing = new PowerDistribution(1,ModuleType.kCTRE);

   //Charge station autonomous
   private static final String kTokyoDrift = "Tokyo Drift";
   private static final String kHitchRoute = "Hitch Route";
   private static final String kFadeAway = "Fade Away";
 
   //Scoring autonomous
   private static final String kFirstScore = "First Score";
   private static final String kSecondScore = "Second Score";
   private static final String kThirdScore = "Third Score"; 

   //Presets for cone scoring
   private static final String kHigh = "High";
   private static final String kMid = "Mid";
   private static final String kLow = "Low";

   private String score_preset_selected;
   private final SendableChooser<String> score_preset_chooser = new SendableChooser<>();

   private DigitalInput zeroedbutton = new DigitalInput(3);
   boolean buttonpressed = false;

   double startingYAW;

  //5464-created classes!
  Drivetrain drivetrain = new Drivetrain();
  Elevator elevator = new Elevator();
  Gyro gyro = new Gyro();
  Leds Leds = new Leds();
  Vacuum intake = new Vacuum();
  Vision vision = new Vision();  
  Pneumatics pneumatics = new Pneumatics();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    elevator.Init();
    Leds.Init();
    drivetrain.Init();
    pneumatics.Init();
    vision.Init();
    intake.Init();

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);

    m_chooser.addOption("Tokyo Drift", kTokyoDrift);
    m_chooser.addOption("Hitch Route", kHitchRoute);
    m_chooser.addOption("Fade Away", kFadeAway);
    m_chooser.addOption("First Score", kFirstScore);
    m_chooser.addOption("Second Score", kSecondScore);
    m_chooser.addOption("Third Score", kThirdScore);

    SmartDashboard.putData("Auto choices", m_chooser);

    score_preset_chooser.setDefaultOption("High", kHigh);
    score_preset_chooser.addOption("Mid", kMid);
    score_preset_chooser.addOption("Low", kLow);
    SmartDashboard.putData("Score Preset Choices", score_preset_chooser);
    Leds.QuestionError();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    drivetrain.DisplayStats();
    elevator.PeriodicTasks();
    gyro.DisplayStats();
    gyro.UpdateGyro();
    vision.DisplayStats();
    intake.DisplayStats();
    pneumatics.DisplayPressure();

    vision.ReturnBestTargetXY(); 

    intake.DistanceCheck();
    
    // the 'back' key will run the 'zeroing' process for elevator safety
    // TODO: Give Clayton (stick2) control over manualmode
    if(stick.getRawButtonPressed(7)){
      zeroed = elevator.zeroRotations();
      if(zeroed){ 
        if(elManualMode){Leds.Manualmode();}
        else{ Leds.Pidmode();}
      }
    }

    // this is the MANUAL OVERRIDE to the PID loop
    if(stick.getRawButtonPressed(8)){
      if(elManualMode){
        System.out.println("We're in automatic mode!");
        if(zeroed){Leds.Pidmode();}
        else{Leds.QuestionError();}
        elManualMode = false;
        
      }
      else{
        if(zeroed){Leds.Manualmode();}
        
        System.out.println("We're in manual mode!");
        elManualMode = true;
      }
    }

  }

  // This is step 0 in 'Tokyo Drift' subroutine!
  // Drives forward with Limelight, so we can be at the correct distance to score
  public void scorePrep(){

    double xcord = vision.USBcamerax;
    double ycord = vision.USBcameray;
    //vision.changeVisionType("reflective"); 
    
    // do all the stuff we want during this step
    // at some point, once we satisfy conditions, we will do the following:
    // TODO: verify our x & y coords

    drivetrain.Move(0, vision.USBcamerax/120, 0); 

    if ((xcord < 2) && (xcord > -2) && (ycord < 2) && (ycord > -2)){
      autoStep++;
    }
  }
 
  public void sConeEl(){

    // checking elevator encoder
    // checking extension encoder

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
    // TODO: Use a function from the elevator class to set 'ready' correctly!
    // Otherwise, we might spit out the game piece early!!!! Oh no!

  }

  public void Score(){
    // run the intake to spit out the cone
    intake.outrun();
    if(intake.intakeRotations > 20){
      intake.stoprun();
      autoStep++; //TODO:Decide rotations
    }
  }
  
  //TODO: Make everything but 'hitchEscape' a generic escape
  public void TokyoEscape(){
    drivetrain.Move(-0.5,0 , 0);
    System.out.println(drivetrain.frontleftrotations);
    if(drivetrain.frontleftrotations < -58.0){
      autoStep++;
    }
  }

  public boolean HitchEscape(){   // This one is different than the rest
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
    drivetrain.Move(0, 0.5 , 0);
    System.out.println(drivetrain.frontleftrotations);
    if(drivetrain.frontleftrotations > 0){  // TODO: Fill in encoder values correctly
      autoStep++;
    }
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

  public void Arrival(){
    
    double x = vision.tag7x;
    double y = vision.tag7y;

    // TODO: Get actual x & y vals needed here, and put in drivetrain.move method, look at ScorePrep



    if((x < 2) && (x > -2) && (y < 2) && (y > -2)){
      autoStep++;
    }

    
  }

  public void Gunit(){
    // TODO: Make this charge forward until we are level on the charge station.
    // Hint: We start level, end up tilted high one direction, and then end up level on top.
    // Make it read out 'ready' when that final level value is seen.
    drivetrain.Move(0.7, 0 , 0);
    System.out.println(drivetrain.frontleftrotations);
    if(drivetrain.frontleftrotations > 20){  // TODO: Fill in encoder values correctly
      autoTimer.start();
      autoStep++;
    }
  }

  public void Balance(){
    //When pitch ~ 0 then stop
    if((gyro.Pitch < 1)&&(gyro.Pitch > -1)){
      drivetrain.Move(0,0 ,0 );
      if(autoTimer.get() > 2.0){
        autoStep++;
      }
    }
    
    //When pitch > 0 then move forward
    if(gyro.Pitch > 0){
      autoTimer.reset();
      drivetrain.Move(0.2,0 ,0 );
    }
      
    //When pitch < 0 then move backward
    if(gyro.Pitch < 0){
      autoTimer.reset();
      drivetrain.Move(-0.2,0 ,0 );
    }
    //Focus on pitch when level value reads around 0
  }

  public boolean Generic_Backup(){
    boolean ready = false;
    // TODO: Set ready to 'true' based on encoder rotations on the drivetrain
    return ready;
  }

  // This autonomous routine is for a start in front of a cone-scoring post
  // It scores a cone, then zooms around the charging station
  // It then drives us onto the charging station, keeping us there with a gyro/brake

  // This autonomous routine starts in the right position, scores a cone,
  // backs up past the charge station, strafes right, and drives back on it
  public void AutoTokyoDrift(){
    switch(autoStep){
      case 0:
        scorePrep();

      case 1:
        sConeEl();

      case 2:
        Score();

      case 3:
        TokyoEscape();

      case 4:
        TokyoDrift();

      case 5: 
        Arrival();

      case 6:
        Gunit();

      case 7:
        Balance();

      case 8:
        break;
      default:
        drivetrain.Move(0, 0, 0);
    }
    // if(ready){
    //   ready = false;
    //   autoStep++;
    // }
  }



  // This autonomous routine starts anywhere in front of a cone scoring location
  // It drives forward, scores, backs out of community
  public void AutoDefault(){
    boolean ready = false;
    switch(autoStep){
      case 0:
        ready = scorePrep();

      case 1:
        ready = sConeEl();

      case 2:
        ready = Score();

      case 3:
        ready = Generic_Backup();

      case 8:
        break;
    }
    if(ready){
      ready = false;
      autoStep++;
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
  public int autoStep=0;

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
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    m_autoSelected = m_chooser.getSelected();
    score_preset_selected = score_preset_chooser.getSelected(); 

    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    System.out.println("Preset selected: " + score_preset_selected);

    exampleTimer.stop();
    exampleTimer.reset();
    //exampleTimer.start();

    startingYAW = gyro.Yaw;
  
    autoStep = 0;

    drivetrain.DriveEncodersZeroed();
  }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {
      System.out.println(autoStep);
      
      switch (m_autoSelected) {
        
        case kTokyoDrift:
          // If we select 'Tokyo Drift' on Drivers' station, it will run this function!
          AutoTokyoDrift();     
          break;
  
        // case kHitchRoute:
        //   AutoHitchRoute();
        //   break;
  
        // case kFadeAway:
        //   AutoFadeAway();
        //   break;
  
        // case kFirstScore:
        //   AutoFirstScore();
        // break;
  
        // case kSecondScore:
        //   AutoSecondScore();
        // break;
  
        // case kThirdScore:
        //   AutoThirdScore();
        // break;
  
        // case kDefaultAuto:
        //   AutoDefault();
        //   break;
          
        default:
          AutoDefault();
          break;
      }

      SmartDashboard.putNumber("autoSep", autoStep);
    }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    elevator.setElevatorToBrake();
    elevator.Init();
    pneumatics.Init();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    
    if(stick.getRawAxis(2) > 0.1){
      drivetrain.Move(-stick.getRawAxis(1), vision.USBcamerax/120, stick.getRawAxis(0)); 
    }
    else{
      drivetrain.Move(-stick.getRawAxis(1), stick.getRawAxis(4), stick.getRawAxis(0));
    }

    // drivetrain.driveCartesian(-stick.getRawAxis(1)*maxspeed, stick.getRawAxis(4)*maxspeed, stick.getRawAxis(0)*maxspeed);

    if(elManualMode){
      if(stick2.getRawButton(3)){
        elevator.Extend();
      }
      else if(stick2.getRawButton(4)){
        elevator.Retract();
      }
      else{
        elevator.Shutdown();
      }     
      if(stick2.getRawButton(1)){
        elevator.jogWinch(0.7);
      }
      else if(stick2.getRawButton(2)){
        elevator.jogWinch(-0.7);
      }
      else{
        elevator.jogWinch(0);
      }   
    }
    else{
      // holds the elevator according to PID control
      elevator.pidControl();   
    }


    // TODO: Give Clayton these controls, find new buttons for his homing stuff
    if(stick.getRawButton(5)){
      intake.inrun();
    }
    else if(stick.getRawButton(6)){
      intake.outrun();
    }
    else{
      intake.stoprun();
    }   

    if(stick2.getRawAxis(2) > 0.1){
      Leds.PickCone();
    }

    if(stick2.getRawAxis(3) > 0.1){
      Leds.PickCube();
    }

    //Encoders
    if(stick2.getRawButtonPressed(7)){
      elevator.setElevatorPosition("Drive");
      //Leds stay in PID
    }

    if(stick2.getRawButtonPressed(3)){
      elevator.setElevatorPosition("ConePickupHigh");
      Leds.PickCone();
    }

    if(stick2.getRawButtonPressed(1)){
      elevator.setElevatorPosition("ConePickupLow");
      Leds.PickCone();
    }
    
    if(stick2.getRawButtonPressed(4)){
       elevator.setElevatorPosition("ScoreHighCone");
      Leds.PickCone();
    }

    if(stick2.getRawButtonPressed(2)){
      elevator.setElevatorPosition("ScoreMidCone");
      Leds.PickCone();
    }

    if(stick2.getPOV() == 270){
      elevator.setElevatorPosition("CubePickupHigh");
      Leds.PickCube();
    }

    if(stick2.getPOV() == 180){
      elevator.setElevatorPosition("CubePickupLow");
      Leds.PickCube();
    }

    if(stick2.getPOV() == 0){
      elevator.setElevatorPosition("ScoreHighCube");
      Leds.PickCube();
    }

    if(stick2.getPOV(0) == 90){
      elevator.setElevatorPosition("ScoreMidCube");
      Leds.PickCube();
    }

    if(stick2.getRawButtonPressed(6)){
      elevator.setElevatorPosition("ScoreLowCone/Cube");
      Leds.HybridPickConeCube();
    }

    if(stick2.getRawButton(9)){
      PDThing.setSwitchableChannel(true);
    }
    else{
      PDThing.setSwitchableChannel(false);
    }

      if(stick.getRawButtonPressed(9)){
        //pneumatics.CompOnOffOn();
      }

      if(stick.getRawButtonPressed(10)){ 
        //pneumatics.SolBreak();
      }

    }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    elevator.setElevatorToBrake();
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic(){

    // if(){
    // buttonpressed = true;
    // elevator.elevator_zeroed;
    // }

  } 

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
  }