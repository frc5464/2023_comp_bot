package frc.robot;

import com.fasterxml.jackson.databind.util.PrimitiveArrayBuilder;
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
    private DigitalInput elRotateLimitSwitch = new DigitalInput(2);
    private DigitalInput elExtendLimitSwitch = new DigitalInput(0);
    private DigitalInput elRetractLimitSwitch = new DigitalInput(1);
    
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
        winchPidSetup();
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
        SmartDashboard.putBoolean("Rectract Limit Switch", elRetractLimitSwitch.get());
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

    private void checkForWinchPidChanges(){
        // read winchension PID coefficients from SmartDashboard
        double p = SmartDashboard.getNumber("winch P Gain", 0);
        double i = SmartDashboard.getNumber("winch I Gain", 0);
        double d = SmartDashboard.getNumber("winch D Gain", 0);
        double iz = SmartDashboard.getNumber("winch I Zone", 0);
        double ff = SmartDashboard.getNumber("winch Feed Forward", 0);
        double max = SmartDashboard.getNumber("winch Max Output", 0);
        double min = SmartDashboard.getNumber("winch Min Output", 0);
        double rotations = SmartDashboard.getNumber("winch Set Rotations", 0);

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

        SmartDashboard.putNumber("SetPoint", rotations);
        SmartDashboard.putNumber("ProcessVariable", elWinchEncoder.getPosition());  
    }

    public void Extend(){
        if(elExtendLimitSwitch.get() == false){
            elextend.set(0.7);
        }
            else{
                elextend.set(0);
            }
    }

    
    public void Retract(){
        if(elRetractLimitSwitch.get() == false){
            elextend.set(-0.7);
        }
            else{
                elextend.set(0);
            }
    }

    public void Shutdown(){
        elextend.set(0);
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
                // TODO: Jake filled in garbage here. Fix this with actual values!
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
        SmartDashboard.putNumber("winch P Gain", winchP);
        SmartDashboard.putNumber("winch I Gain", winchI);
        SmartDashboard.putNumber("winch D Gain", winchD);
        SmartDashboard.putNumber("winch I Zone", winchIz);
        SmartDashboard.putNumber("winch Feed Forward", winchFF);
        SmartDashboard.putNumber("winch Max Output", winchMaxOutput);
        SmartDashboard.putNumber("winch Min Output", winchMinOutput);
        SmartDashboard.putNumber("ext Set Rotations", 0);    
    }


}
