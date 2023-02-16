package frc.robot;

import javax.swing.text.StyleContext.SmallAttributeSet;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Pneumatics {

    Compressor compressor = new Compressor(0, PneumaticsModuleType.CTREPCM); 

    DoubleSolenoid pcm1 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
    DoubleSolenoid pcm2 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 3);
    DoubleSolenoid pcm3 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 4, 5);
    DoubleSolenoid pcm4 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 6, 7);

    boolean compoffon = false;
    boolean solbreak = false; 
    
public void Init(){

    compressor.disable();
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
    SmartDashboard.getNumber("Compressor's Pressure", compressor.getPressure());
}

}


