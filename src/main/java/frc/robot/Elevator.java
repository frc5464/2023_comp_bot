package frc.robot;


import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// This class contains all of the code needed for the elevator portion
// of 5464's 2023 comp bot
// Mostly contains two motor controllers, and their configurations

public class Elevator {
    // ============================================== Public Variables
    // Variables we want the rest of the robot to know about

    // this is the motor safety flag which prevents us from moving winch/ext until we zero things out
    public boolean elevator_zeroed = false;
    // ============================================== Private Variables
    // Variables the rest of the robot does not care about
    private CANSparkMax elextend = new CANSparkMax(3, MotorType.kBrushless);
    private CANSparkMax elwinch = new CANSparkMax(4, MotorType.kBrushless);
    private RelativeEncoder elExtendEncoder;
    private RelativeEncoder elWinchEncoder;
    private SparkMaxPIDController elExtendPid;
    private SparkMaxPIDController elWinchPid;
    
    private DigitalInput elExtendLimitSwitch = new DigitalInput(0);
    private DigitalInput elRetractLimitSwitch = new DigitalInput(1);
    private DigitalInput elRotateLimitSwitch = new DigitalInput(2);

    private double extP, extI, extD, extIz, extFF, extMaxOutput, extMinOutput;
    private double winchP, winchI, winchD, winchIz, winchFF, winchMaxOutput, winchMinOutput;

    private double extTargetRotations;
    private double winchTargetRotations;

    private static final String kDrive = "Drive";
    private static final String kConePickupHigh = "ConePickupHigh";
    private static final String kConeCubePickupLow = "ConeCubePickupLow";
    private static final String kScoreHighCone = "ScoreHighCone"; 
    private static final String kScoreMidCone = "ScoreMidCone";
    private static final String kCubePickupHigh = "CubePickupHigh";
    private static final String kScoreHighCube = "ScoreHighCube"; 
    private static final String kScoreMidCube = "ScoreMidCube"; 
    private static final String kScoreLowConeCube = "ScoreLowConeCube";
    private static final String KAprilTags = "AprilTagEncoder";
    private static final String kConePickupLowforHighScore = "ConePickupLowforHighScore";


    double winchDangerZone = 40;
    double extCurrentRotations = 10;
    double winchCurrentRotations = 10;

    boolean extend_zone_ok = false;
    boolean retract_zone_ok = false;
    boolean winch_up_zone_ok = false;
    boolean waiting_for_ext = false;


    // ============================================= Public Functions
    public void Init(){
        elExtendEncoder = elextend.getEncoder();
        elWinchEncoder = elwinch.getEncoder();
        elExtendPid = elextend.getPIDController();
        elWinchPid = elwinch.getPIDController();
        extendPidSetup();
        winchPidSetup();
    }

    public void setElevatorToCoast(){
        //elwinch.setIdleMode(IdleMode.kCoast);
        elextend.setIdleMode(IdleMode.kCoast);
    }

    public void setElevatorToBrake(){
        elwinch.setIdleMode(IdleMode.kBrake);
        elextend.setIdleMode(IdleMode.kBrake);
    }

    public void PeriodicTasks(){

        // check our encoder values once per tick
        extCurrentRotations = elExtendEncoder.getPosition();
        winchCurrentRotations = elWinchEncoder.getPosition();
        
        checkForPidChanges();
        checkForSafeToExtend();
        checkForSafeToRetract();
        checkForSafeToRotate();
        
        SmartDashboard.putNumber("extension encoder",extCurrentRotations);
        SmartDashboard.putNumber("winch encoder",winchCurrentRotations);
        SmartDashboard.putNumber("Extender Current Output", elextend.getOutputCurrent());
        SmartDashboard.putBoolean("Rotate limit switch", elRotateLimitSwitch.get());
        SmartDashboard.putBoolean("Extend limit switch", elExtendLimitSwitch.get());
        SmartDashboard.putBoolean("Retract Limit Switch", elRetractLimitSwitch.get());
        SmartDashboard.putBoolean("Elevator Zeroed?", elevator_zeroed);
        SmartDashboard.putNumber("Elevator current", elextend.getOutputCurrent());
        SmartDashboard.putNumber("Winch current", elextend.getOutputCurrent());
    }

    private void checkForSafeToExtend(){
        // Check that we are not extending in the dangerous "low zone"
        if ((winchCurrentRotations > winchDangerZone) && (extCurrentRotations < 80)){
            // Check the we are not near the limits of the extension zone
            extend_zone_ok = true;
        }
        else{
            extend_zone_ok = false;
        }
    }

    public void checkForSafeToRetract(){
        // Check that we are not extending in the dangerous "low zone"
        if ((winchCurrentRotations > winchDangerZone) && (extCurrentRotations > 5)){
            // Check the we are not near the limits of the extension zone
            retract_zone_ok = true;
        }
        else{
            retract_zone_ok = false;
        }
    }

    public void checkForSafeToRotate(){
        // Check if we are above the danger zone, and moving into it!
        // If this is the case, extension/retraction must be complete before rotation.
        if((winchCurrentRotations > winchDangerZone)&&(winchTargetRotations < winchDangerZone)&&
                (Math.abs(extCurrentRotations - extTargetRotations) > 5)){
            waiting_for_ext = true;
        }
        else{
            waiting_for_ext = false;
        }

        if(winchCurrentRotations < 130){
            winch_up_zone_ok = true;
        }
        else{
            winch_up_zone_ok = false;
        }
    }

    public void checkForPidChanges(){
        // step into two private functions which check for PID updates
        checkForExtPidChanges();
        checkForWinchPidChanges();
    }

    private void checkForExtPidChanges(){
        // read Extension PID coefficients from SmartDashboard
        double p = SmartDashboard.getNumber("ext P Gain", 0);
        double i = SmartDashboard.getNumber("ext I Gain", 0);
        double d = SmartDashboard.getNumber("ext D Gain", 0);
        double iz = SmartDashboard.getNumber("ext I Zone", 0);
        double ff = SmartDashboard.getNumber("ext Feed Forward", 0);
        double max = SmartDashboard.getNumber("ext Max Output", 0);
        double min = SmartDashboard.getNumber("ext Min Output", 0);
        //extTargetRotations = SmartDashboard.getNumber("ext Set Rotations", 0);

        // if Ext PID coefficients on SmartDashboard have changed, write new values to controller
        if((p != extP)) { elExtendPid.setP(p); extP = p; }
        if((i != extI)) { elExtendPid.setI(i); extI = i; }
        if((d != extD)) { elExtendPid.setD(d); extD = d; }
        if((iz != extIz)) { elExtendPid.setIZone(iz); extIz = iz; }
        if((ff != extFF)) { elExtendPid.setFF(ff); extFF = ff; }
        if((max != extMaxOutput) || (min != extMinOutput)) { 
        elExtendPid.setOutputRange(min, max); 
        extMinOutput = min; extMaxOutput = max; 
        }   

        SmartDashboard.putNumber("ext SetPoint", extTargetRotations); 
    }

    private void checkForWinchPidChanges(){
        // read winchension PID coefficients from SmartDashboard
        double p = SmartDashboard.getNumber("winch P Gain", 0);
        double i = SmartDashboard.getNumber("winch I Gain", 0);
        double d = SmartDashboard.getNumber("winch D Gain", 0);
        double iz = SmartDashboard.getNumber("winch I Zone", 0);
        double ff = SmartDashboard.getNumber("winch Feed Forward", 0);
        double max = SmartDashboard.getNumber("winch Max Output", 0);
        double min = SmartDashboard.getNumber("winch Min Output", 0);
        //winchTargetRotations = SmartDashboard.getNumber("winch Set Rotations", 0);

        // if winch PID coefficients on SmartDashboard have changed, write new values to controller
        if((p != winchP)) { elWinchPid.setP(p); winchP = p; }
        if((i != winchI)) { elWinchPid.setI(i); winchI = i; }
        if((d != winchD)) { elWinchPid.setD(d); winchD = d; }
        if((iz != winchIz)) { elWinchPid.setIZone(iz); winchIz = iz; }
        if((ff != winchFF)) { elWinchPid.setFF(ff); winchFF = ff; }
        if((max != winchMaxOutput) || (min != winchMinOutput)) { 
            elWinchPid.setOutputRange(min, max); 
            winchMinOutput = min; winchMaxOutput = max; 
        }   

        SmartDashboard.putNumber("winch SetPoint", winchTargetRotations);
    }

    public void Extend(){
        if(elExtendLimitSwitch.get() == false){
            if(extend_zone_ok){
                if(elevator_zeroed){
                    elextend.set(0.6);
            }
        }
        }
        else{
            elextend.set(0);
        }
    }

    public void Retract(){
        if(elRetractLimitSwitch.get() == false){
            if(retract_zone_ok){
                if(elevator_zeroed){
                    elextend.set(-0.6);
                }
            }
        }
        else{
            elextend.set(0);
        }
    }

    public void Shutdown(){
        elextend.set(0);
        elwinch.set(0);
    }

    public void jogWinch(double val){
        elwinch.set(val);
    }

    public void pidHoming(){
        // This is going to ignore a lot of the fancy stuff we did before
        // Something is not working right, and programming has not had time to deal with it

        // ONLY if we're out of the danger zone,
        if(winchCurrentRotations > winchDangerZone){
            // back off for illegal things!
            if(extCurrentRotations > 80){
                elextend.set(-0.2);
            }
            else if(extCurrentRotations < 2){
                elextend.set(0.2);
            }
            // stop the extension arm if we're close enough!
            else if(Math.abs(extCurrentRotations - extTargetRotations) < 3){
                //elextend.set(0);
                elExtendPid.setReference(extTargetRotations, CANSparkMax.ControlType.kPosition);
            }

            // otherwise, extend if we're not out far enough
            else if(extCurrentRotations < extTargetRotations){
                //elextend.set(nonPidExtHomingSpeed);
                elExtendPid.setReference(extTargetRotations, CANSparkMax.ControlType.kPosition);
            }

            // or retract if we're too far out
            else if(extCurrentRotations > extTargetRotations){
                //elextend.set(-nonPidExtHomingSpeed);
                elExtendPid.setReference(extTargetRotations, CANSparkMax.ControlType.kPosition);
            }
            
            // by default, do nothing.
            else{
                elextend.set(0);
            }
        }

        // if we are above the danger zone, AND the extender still has to move,
        if((winchCurrentRotations > winchDangerZone) && (winchTargetRotations < winchDangerZone) && 
                (Math.abs(extCurrentRotations - extTargetRotations) > 5)){
            // stop the winch and wait for it to home in!
            elwinch.set(0); 
        }

        // otherwise,
        else{
            // back off for illegal things!
            if(winchCurrentRotations > 128){
                elwinch.set(-0.2);
            }
            // stop the winch if we're close enough!
            else if(Math.abs(winchCurrentRotations - winchTargetRotations) < 3){
                // elwinch.set(0);
                elWinchPid.setReference(winchTargetRotations, CANSparkMax.ControlType.kPosition);
            }
            // otherwise, winch us higher if we need to go higher
            else if(winchCurrentRotations < winchTargetRotations){
                //elwinch.set(nonPidWinchHomingSpeed);
                elWinchPid.setReference(winchTargetRotations, CANSparkMax.ControlType.kPosition);
            }
            // and winch us lower if we need to go lower
            else if(winchCurrentRotations > winchTargetRotations){
                // elwinch.set(-nonPidWinchHomingSpeed);
                elWinchPid.setReference(winchTargetRotations, CANSparkMax.ControlType.kPosition);
            }                
            else{
                elwinch.set(0);
            }
        }
    }

    public boolean zeroRotations(){
      // check our limit switches to make sure that we are actually at the zero point
      // this should prevent the possibility of zeroing during a match
      if(elRetractLimitSwitch.get()){
        elExtendEncoder.setPosition(0);
        elWinchEncoder.setPosition(130);
        elevator_zeroed = true;
        System.out.println("Elevator zeroed out!");
      }
      else{
        System.out.println("Elevator NOT zeroed out! Check Limit Switches!");
      }
      return elevator_zeroed;        
    }

    public void setElevatorPosition(String str){
        switch (str){
            case kDrive:
                winchTargetRotations = 83;
                extTargetRotations = 8;
                break;
            case kConePickupHigh:
                //TODO: HIGH: extTargetRotations seemed a few too low on Saturday. Fix and retry.
                winchTargetRotations = 100;
                extTargetRotations = 18;
                break;
            case kConeCubePickupLow:
                winchTargetRotations = 2;
                extTargetRotations = 8;
                break;
            case kScoreHighCone:
                winchTargetRotations = 77;
                extTargetRotations = 77;
                break;
            case kScoreMidCone:
                winchTargetRotations = 105;
                extTargetRotations = 7;
                break;
            case kCubePickupHigh:
                winchTargetRotations = 100;
                extTargetRotations = 35;
                break;
            case kScoreHighCube:
                winchTargetRotations = 70;
                extTargetRotations = 79;
                break;
            case kScoreMidCube:
                winchTargetRotations = 88;
                extTargetRotations = 56;
                break;
            case kScoreLowConeCube:
                winchTargetRotations = 37;
                extTargetRotations = 8;
                break;
            case KAprilTags:
                winchTargetRotations = 20;
                extTargetRotations = 8;
                break;
            case kConePickupLowforHighScore:
                winchTargetRotations = 16;
                winchTargetRotations = 0; 
                break;
            default: 
                extTargetRotations = 8;
                winchTargetRotations = 83;    
                System.out.println("Default elevator val");
        }
    }

    // ============================================= Private Functions
    private void extendPidSetup(){
        // PID coefficients
        extP = 0.6; 
        extI = 0.0001;
        extD = 0.001; 
        extIz = 0; 
        extFF = 0.001; 
        extMaxOutput = 0.7; 
        extMinOutput = -0.7;

        // set PID coefficients
        elExtendPid.setP(extP);
        elExtendPid.setI(extI);
        elExtendPid.setD(extD);
        elExtendPid.setIZone(extIz);
        elExtendPid.setFF(extFF);
        elExtendPid.setOutputRange(extMinOutput, extMaxOutput);
        
        // display PID coefficients on SmartDashboard
        SmartDashboard.putNumber("ext P Gain", extP);
        SmartDashboard.putNumber("ext I Gain", extI);
        SmartDashboard.putNumber("ext D Gain", extD);
        SmartDashboard.putNumber("ext I Zone", extIz);
        SmartDashboard.putNumber("ext Feed Forward", extFF);
        SmartDashboard.putNumber("ext Max Output", extMaxOutput);
        SmartDashboard.putNumber("ext Min Output", extMinOutput);
        SmartDashboard.putNumber("ext Set Rotations", extTargetRotations);
          

    }  
    
    private void winchPidSetup(){
        // PID coefficients
        winchP = 0.8; 
        winchI = 0.0001;
        winchD = 0.001;
        winchIz = 0; 
        winchFF = 0.001; 
        winchMaxOutput = 0.8; 
        winchMinOutput = -0.8;

        // set PID coefficients
        elWinchPid.setP(winchP);
        elWinchPid.setI(winchI);
        elWinchPid.setD(winchD);
        elWinchPid.setIZone(winchIz);
        elWinchPid.setFF(winchFF);
        elWinchPid.setOutputRange(winchMinOutput, winchMaxOutput);
        
        // display PID coefficients on SmartDashboard
        SmartDashboard.putNumber("winch P Gain", winchP);
        SmartDashboard.putNumber("winch I Gain", winchI);
        SmartDashboard.putNumber("winch D Gain", winchD);
        SmartDashboard.putNumber("winch I Zone", winchIz);
        SmartDashboard.putNumber("winch Feed Forward", winchFF);
        SmartDashboard.putNumber("winch Max Output", winchMaxOutput);
        SmartDashboard.putNumber("winch Min Output", winchMinOutput);
        SmartDashboard.putNumber("winch Set Rotations", winchTargetRotations);    
        
    }

}
