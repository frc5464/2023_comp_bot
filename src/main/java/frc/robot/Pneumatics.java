package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//JAKE IMPORTANT: we can pre-charge pneumatics before each match so we will start false and disabled so not to waste battery

public class Pneumatics {

    Compressor compressor = new Compressor(0, PneumaticsModuleType.CTREPCM); 

    DoubleSolenoid pcm1 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 0);
    DoubleSolenoid pcm2 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 3, 2);
    DoubleSolenoid pcm3 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 5, 4);
    DoubleSolenoid pcm4 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 7, 6);

    boolean compoffon = false;
    boolean solbreak = false; 
    
public void Init(){

    compressor.enableDigital();
    pcm1.set(Value.kReverse);
    pcm2.set(Value.kReverse);
    pcm3.set(Value.kForward);
    pcm4.set(Value.kForward);

}

public void CompOnOffOn(){
    if(compoffon == false){
        compoffon = true;
        compressor.enableDigital();
    }
    else if(compoffon == true){
        compoffon = false;
        compressor.disable();
    }
}

public void SolBreak(){
    if(solbreak == false){
        solbreak = true;
        pcm1.set(Value.kForward);
        pcm2.set(Value.kForward);
        pcm3.set(Value.kReverse);
        pcm4.set(Value.kReverse);
    }
    else if(solbreak == true){
        solbreak = false;
        pcm1.set(Value.kReverse);
        pcm2.set(Value.kReverse);
        pcm3.set(Value.kForward);
        pcm4.set(Value.kForward);
    }
}

public void DisplayPressure(){
    SmartDashboard.putNumber("Compressor's Pressure", compressor.getPressure());
}

}


