// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import pabeles.concurrency.IntRangeTask;

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

  //Joystick
  Joystick stick = new Joystick(0);
  Joystick stick2 = new Joystick(1);

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

  //5464-created classes!
  Drivetrain drivetrain = new Drivetrain();
  Elevator elevator = new Elevator();
  Gyro gyro = new Gyro();
  Leds Leds = new Leds();
  Vacuum intake = new Vacuum();
  Vision vision = new Vision();  

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

    if(elevator.zeroRotations() == false){
      Leds.QuestionError();
    }
      else{
        Leds.Pidmode();
     }
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
    elevator.DisplayStats();
    elevator.checkForPidChanges();
    gyro.DisplayStats();
    vision.DisplayStats();

    vision.ReturnBestTargetXY(); 
  }

  // This is step 0 in 'Tokyo Drift' subroutine!
  // Drives forward with Limelight, so we can be at the correct distance to score
  public boolean scorePrep(){
    // flag indicating we are lined up
    boolean ready = false;

    double xcord = vision.camerax;
    double ycord = vision.cameray;
    vision.changeVisionType("reflective"); 
    
    // do all the stuff we want during this step
    // at some point, once we satisfy conditions, we will do the following:
    // TODO: drive forward and check distance with Vision.
    
    // if(  /* check some limelight variable here */    ){
    //   ready = true;
    // }

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
        //reach elevator and extension for high cone
        //set ready to true once conditions are met

        break;

      case kMid:
        //reach elevator and extension for mid cone
        //set ready to true once conditions are met

        break;

      case kLow:
        //reach elevator and extension for low cone
        //set ready to true once conditions are met

        break;

      default:
        //high preset code, in case selector BREAKS!
        //set ready to true once conditions are met

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
    //What this mean yo. Just drive with the drivetrain instance

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

  // This autonomous routine is for a start in front of a cone-scoring post
  // It scores a cone, then zooms around the charging station
  // It then drives us onto the charging station, keeping us there with a gyro/brake

  // This autonomous routine starts in the right position, scores a cone,
  // backs up past the charge station, strafes right, and drives back on it
  public void AutoTokyoDrift(){
    boolean ready = false;
    switch(autoStep){
      case 0:
        ready = scorePrep();

      case 1:
        ready = sConeEl();

      case 2:
        ready = Score();

      case 3:
        ready = TokyoEscape();

      case 4:
        ready = TokyoDrift();

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
        // ready = ?
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
  }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {
      switch (m_autoSelected) {
        
        case kTokyoDrift:
          // If we select 'Tokyo Drift' on Drivers' station, it will run this function!
          AutoTokyoDrift();        
        break;
  
        case kHitchRoute:
          AutoHitchRoute();
          break;
  
        case kFadeAway:
          AutoFadeAway();
          break;
  
        case kFirstScore:
          AutoFirstScore();
        break;
  
        case kSecondScore:
          AutoSecondScore();
        break;
  
        case kThirdScore:
          AutoThirdScore();
        break;
  
        case kDefaultAuto:
          AutoDefault();
          break;
          
        default:
          AutoDefault();
          break;
      }
    }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    elevator.setElevatorToBrake();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    
    drivetrain.Move(-stick.getRawAxis(1), stick.getRawAxis(4), stick.getRawAxis(0));

    // drivetrain.driveCartesian(-stick.getRawAxis(1)*maxspeed, stick.getRawAxis(4)*maxspeed, stick.getRawAxis(0)*maxspeed);

    // this is the MANUAL OVERRIDE to the PID loop
    // For now, this will be the following id:
    if(stick.getRawButtonPressed(7)){
      if(elManualMode){
        Leds.Pidmode();
        elManualMode = false;
      }
      else{
        Leds.Manualmode();
        elManualMode = true;
      }
    }
    if(elManualMode){
      if(stick.getRawButton(3)){
        elevator.Extend();
      }
      else if(stick.getRawButton(4)){
        elevator.Retract();
      }
      else{
        elevator.Shutdown();
      }     
      if(stick.getRawButton(2)){
        elevator.jogWinch(1);
      }
      else if(stick.getRawButton(1)){
        elevator.jogWinch(-1);
      }
      else{
        elevator.jogWinch(0);
      }   
    }
    else{
      // holds the elevator according to PID control
      elevator.pidControl(); 
    }

    // the 'back' key will run the 'zeroing' process for elevator safety
    if(stick.getRawButtonPressed(7)){
      elevator.zeroRotations();
    }

    if(stick.getRawButton(5)){
      intake.inrun();
    }
    else if(stick.getRawButton(6)){
      intake.outrun();
    }
    else{
      intake.stoprun();
    }   


  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    elevator.setElevatorToCoast();
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

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