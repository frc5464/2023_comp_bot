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
    
    // these strings will deteremine what position we go to
    public String extPosition;
    public String winchPosition;

    // ============================================== Private Variables
    // Variables the rest of the robot does not care about
    private CANSparkMax elextend = new CANSparkMax(3, MotorType.kBrushless);
    private CANSparkMax elwinch = new CANSparkMax(4, MotorType.kBrushless);
    private RelativeEncoder elExtendEncoder;
    private RelativeEncoder elWinchEncoder;
    private SparkMaxPIDController elExtendPid;
    private SparkMaxPIDController elWinchPid;
    private DigitalInput elRotateLimitSwitch = new DigitalInput(0);
    private DigitalInput elExtendLimitSwitch = new DigitalInput(1);
    private boolean elevator_zeroed = false;
    private double extP, extI, extD, extIz, extFF, extMaxOutput, extMinOutput;
    private double winchP, winchI, winchD, winchIz, winchFF, winchMaxOutput, winchMinOutput;

    private double extTargetRotations;
    private double winchTargetRotations;

    // ============================================= Public Functions
    public void Init(){
        elExtendEncoder = elextend.getEncoder();
        elWinchEncoder = elwinch.getEncoder();
        elExtendPid = elextend.getPIDController();
        elWinchPid = elwinch.getPIDController();
        extendPidSetup();
    }

    public void setElevatorToCoast(){
        elwinch.setIdleMode(IdleMode.kCoast);
        elextend.setIdleMode(IdleMode.kCoast);
    }

    public void setElevatorToBrake(){
        elwinch.setIdleMode(IdleMode.kBrake);
        elextend.setIdleMode(IdleMode.kBrake);
    }

    public void DisplayStats(){
        SmartDashboard.putNumber("extension encoder",elExtendEncoder.getPosition());
        SmartDashboard.putNumber("winch encoder",elWinchEncoder.getPosition());
        SmartDashboard.putNumber("Extender Current Output", elextend.getOutputCurrent());

        SmartDashboard.putBoolean("Rotate limit switch", elRotateLimitSwitch.get());
        SmartDashboard.putBoolean("Extend limit switch", elExtendLimitSwitch.get());
    }

    public void checkForPidChanges(){
        // read Extension PID coefficients from SmartDashboard
        double p = SmartDashboard.getNumber("ext P Gain", 0);
        double i = SmartDashboard.getNumber("ext I Gain", 0);
        double d = SmartDashboard.getNumber("ext D Gain", 0);
        double iz = SmartDashboard.getNumber("ext I Zone", 0);
        double ff = SmartDashboard.getNumber("ext Feed Forward", 0);
        double max = SmartDashboard.getNumber("ext Max Output", 0);
        double min = SmartDashboard.getNumber("ext Min Output", 0);
        double rotations = SmartDashboard.getNumber("ext Set Rotations", 0);

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

        SmartDashboard.putNumber("SetPoint", rotations);
        SmartDashboard.putNumber("ProcessVariable", elExtendEncoder.getPosition());        
    }

    public void jogExtend(double val){
        elextend.set(val);
    }

    public void jogWinch(double val){
        elwinch.set(val);
    }

    public void pidControl(){
        // ONLY ALLOW THIS TO RUN IF WE HAVE ZEROED OUT THE ENCODERS ON THIS RUN
        if(elevator_zeroed){
            elExtendPid.setReference(extTargetRotations, CANSparkMax.ControlType.kPosition);
            elWinchPid.setReference(winchTargetRotations, CANSparkMax.ControlType.kPosition);
        }
    }

    public void zeroRotations(){
      // check our limit switches to make sure that we are actually at the zero point
      // this should prevent the possibility of zeroing during a match
      if(!elExtendLimitSwitch.get() && !elRotateLimitSwitch.get()){
        elExtendEncoder.setPosition(0);
        elWinchEncoder.setPosition(0);
        elevator_zeroed = true;
      }        
    }

    public void setElevatorPosition(String str){
        switch (str){
            case "ScoreHighCone":
                // TODO: JAKE JUST FILLED IN GARBAGE HERE! FIX IT!
                extTargetRotations = 250;
                winchTargetRotations = -150;
                // TODO: FILL IN MORE CASES!
            default:
                extTargetRotations = 250;
                winchTargetRotations = -150;    
        }
           

    }
    // ============================================= Private Functions
    private void extendPidSetup(){
        // PID coefficients
        extP = 0.05; 
        extI = 1e-4;
        extD = 0; 
        extIz = 0; 
        extFF = 0.001; 
        extMaxOutput = 1; 
        extMinOutput = -1;

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
        SmartDashboard.putNumber("ext Set Rotations", 0);    

    }  
    
    private void winchPidSetup(){
        //TODO: fill in with control variables
    }


}
