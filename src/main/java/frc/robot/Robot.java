// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
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

  private SparkMaxPIDController elExtendPid;
  // private SparkMaxPIDController elWinchPid;

  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;

  //Motor Controllers for the Drive Train
  CANSparkMax frontleft = new CANSparkMax(2, MotorType.kBrushless);
  CANSparkMax frontright = new CANSparkMax(6, MotorType.kBrushless);
  CANSparkMax backleft = new CANSparkMax(7, MotorType.kBrushless);
  CANSparkMax backright = new CANSparkMax(5, MotorType.kBrushless);

  //Motor Controllers for the Elevator 
  CANSparkMax elextend = new CANSparkMax(3, MotorType.kBrushless);
  CANSparkMax elwinch = new CANSparkMax(4, MotorType.kBrushless);
  CANSparkMax intake = new CANSparkMax(8, MotorType.kBrushless);

  //Drive Train
  MecanumDrive drivetrain = new MecanumDrive(frontleft, backright, frontright, backleft);
  double maxspeed = 1;
  double rampRate = 0.25;

  //Joystick
  Joystick stick = new Joystick(0);
  Joystick stick2 = new Joystick(1);

  AHRS navx = new AHRS();

  RelativeEncoder elExtendEncoder;
  RelativeEncoder elRotateEncoder;

  AbsoluteEncoder elExtendEncoderAbs;

  RelativeEncoder winch_encoder;

  // this is a flag which indicates if the elevator's encoders have been zeroed out.
  // if this is not done, the robot will not be able to safely operate
  // start out with this as false to indicate it has not been done
  boolean elevator_zeroed = false;
  
  DigitalInput elRotateLimitSwitch = new DigitalInput(0);
  DigitalInput elExtendLimitSwitch = new DigitalInput(1);

  AddressableLED ledStrip = new AddressableLED(0);
  AddressableLEDBuffer ledBuffer = new AddressableLEDBuffer(62);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    elExtendEncoder = elextend.getEncoder();
    elExtendEncoderAbs = elextend.getAbsoluteEncoder(Type.kDutyCycle);

    winch_encoder = elwinch.getEncoder();

    elExtendPid = elextend.getPIDController();
    
    // elWinchPid = elwinch.getPIDController();

    // PID coefficients
    kP = 0.1; 
    kI = 1e-4;
    kD = 1; 
    kIz = 0; 
    kFF = 0; 
    kMaxOutput = 1; 
    kMinOutput = -1;

    // set PID coefficients
    elExtendPid.setP(kP);
    elExtendPid.setI(kI);
    elExtendPid.setD(kD);
    elExtendPid.setIZone(kIz);
    elExtendPid.setFF(kFF);
    elExtendPid.setOutputRange(kMinOutput, kMaxOutput);
    
    // display PID coefficients on SmartDashboard
    SmartDashboard.putNumber("P Gain", kP);
    SmartDashboard.putNumber("I Gain", kI);
    SmartDashboard.putNumber("D Gain", kD);
    SmartDashboard.putNumber("I Zone", kIz);
    SmartDashboard.putNumber("Feed Forward", kFF);
    SmartDashboard.putNumber("Max Output", kMaxOutput);
    SmartDashboard.putNumber("Min Output", kMinOutput);
    SmartDashboard.putNumber("Set Rotations", 0);    

    frontleft.setOpenLoopRampRate(rampRate);
    frontright.setOpenLoopRampRate(rampRate);
    backleft.setOpenLoopRampRate(rampRate);
    backright.setOpenLoopRampRate(rampRate);

    elwinch.setIdleMode(IdleMode.kCoast);
    elextend.setIdleMode(IdleMode.kCoast);
    // DO NOT RUN THIS EVERY TIME! ONLY WHEN IT REEEEALLY NEEDS TO BE RUN!
    //navx.calibrate();

    ledStrip.setLength(ledBuffer.getLength());
    for (var i = 0; i < ledBuffer.getLength(); i++) {
      // Sets the specified LED to the RGB values for red
      ledBuffer.setRGB(i, 20, 0, 20);
   }
   
   ledStrip.setData(ledBuffer);
   ledStrip.start();
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
    // NAVX STUFF!!!
    SmartDashboard.putNumber("Yaw", navx.getYaw());
    SmartDashboard.putNumber("Roll", navx.getRoll());
    SmartDashboard.putNumber("Pitch", navx.getPitch());
    SmartDashboard.putNumber("RawX", navx.getRawGyroX());
    SmartDashboard.putNumber("RawY", navx.getRawGyroY());
    SmartDashboard.putNumber("RawZ", navx.getRawGyroZ());
    SmartDashboard.putNumber("dispX", navx.getDisplacementX());
    SmartDashboard.putNumber("dispY", navx.getDisplacementY());
    SmartDashboard.putNumber("dispZ", navx.getDisplacementZ());    

    SmartDashboard.putNumber("extension encoder",elExtendEncoder.getPosition());
    SmartDashboard.putNumber("winch encoder",winch_encoder.getPosition());

    SmartDashboard.putNumber("Absolute ext encoder", elExtendEncoderAbs.getPosition());
    SmartDashboard.putNumber("Extender Current Output", elextend.getOutputCurrent());

    SmartDashboard.putBoolean("Rotate limit switch", elRotateLimitSwitch.get());
    SmartDashboard.putBoolean("Extend limit switch", elExtendLimitSwitch.get());

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
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    elwinch.setIdleMode(IdleMode.kBrake);
    elextend.setIdleMode(IdleMode.kBrake);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    drivetrain.driveCartesian(-stick.getRawAxis(1)*maxspeed, stick.getRawAxis(4)*maxspeed, stick.getRawAxis(0)*maxspeed);

    // if(stick.getRawButton(3)){
    //   elextend.set(1);
    // }
    // else if(stick.getRawButton(4)){
    //   elextend.set(-1);
    // }
    // else{
    //   elextend.set(0);
    // }    

    if(stick.getRawButton(2)){
      elwinch.set(1);
    }
    else if(stick.getRawButton(1)){
      elwinch.set(-1);
    }
    else{
      elwinch.set(0);
    }    

    // the 'back' key will run the 'zeroing' process for elevator safety
    if(stick.getRawButtonPressed(7)){
      // check our limit switches to make sure that we are actually at the zero point
      // this should prevent the possibility of zeroing during a match
      if(!elExtendLimitSwitch.get() && !elRotateLimitSwitch.get()){
        elExtendEncoder.setPosition(0);
        elRotateEncoder.setPosition(0);
      }
    }

    if(stick.getRawButton(5)){
      intake.set(1);
    }
    else if(stick.getRawButton(6)){
      intake.set(-1);
    }
    else{
      intake.set(0);
    }   

    // read PID coefficients from SmartDashboard
    double p = SmartDashboard.getNumber("P Gain", 0);
    double i = SmartDashboard.getNumber("I Gain", 0);
    double d = SmartDashboard.getNumber("D Gain", 0);
    double iz = SmartDashboard.getNumber("I Zone", 0);
    double ff = SmartDashboard.getNumber("Feed Forward", 0);
    double max = SmartDashboard.getNumber("Max Output", 0);
    double min = SmartDashboard.getNumber("Min Output", 0);
    double rotations = SmartDashboard.getNumber("Set Rotations", 0);

    // if PID coefficients on SmartDashboard have changed, write new values to controller
    if((p != kP)) { elExtendPid.setP(p); kP = p; }
    if((i != kI)) { elExtendPid.setI(i); kI = i; }
    if((d != kD)) { elExtendPid.setD(d); kD = d; }
    if((iz != kIz)) { elExtendPid.setIZone(iz); kIz = iz; }
    if((ff != kFF)) { elExtendPid.setFF(ff); kFF = ff; }
    if((max != kMaxOutput) || (min != kMinOutput)) { 
      elExtendPid.setOutputRange(min, max); 
      kMinOutput = min; kMaxOutput = max; 
    }   

    elExtendPid.setReference(rotations, CANSparkMax.ControlType.kPosition);

    SmartDashboard.putNumber("SetPoint", rotations);
    SmartDashboard.putNumber("ProcessVariable", elExtendEncoder.getPosition());

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

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
