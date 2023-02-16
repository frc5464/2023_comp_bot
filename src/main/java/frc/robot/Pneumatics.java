package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Pneumatics {

    Compressor compressor = new Compressor(0, PneumaticsModuleType.CTREPCM); 

    DoubleSolenoid pcm1 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
    DoubleSolenoid pcm2 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 3);
    DoubleSolenoid pcm3 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 4, 5);
    DoubleSolenoid pcm4 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 6, 7);
    
public void Init(){

    compressor.enableDigital();
    pcm1.set(Value.kForward);
    pcm2.set(Value.kForward);
    pcm3.set(Value.kForward);
    pcm4.set(Value.kForward);
}

public void ToggleSolenoids(){
    pcm1.toggle();
    pcm2.toggle();
    pcm3.toggle();
    pcm4.toggle();
}

}


