package frc.robot;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class Leds {
    // ============================================== Public Variables
    // What we want the rest of the robot to know


    // ============================================== Private Variables
    // What the rest of the robot does not care about
    AddressableLED ledStrip = new AddressableLED(0);
    AddressableLEDBuffer ledBuffer = new AddressableLEDBuffer(62); 


    // ============================================= Public Functions
    public void Init(){
        // first time setting the length of the LEDs
        ledStrip.setLength(ledBuffer.getLength()); 
        ledStrip.setData(ledBuffer);
        ledStrip.start();
    }

    public void QuestionError(){
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 120, 0, 0);
        }
        ledStrip.setData(ledBuffer);
    }

    public void DefaultLight(){
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 0, 0,120);
        }
        ledStrip.setData(ledBuffer);        
    }

    public void Manualmode(){
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 60, 0, 60);
        }
        ledStrip.setData(ledBuffer);        
    }

    public void Pidmode(){
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 60, 60, 120);
        }
        ledStrip.setData(ledBuffer);        
    }

    public void PickCone(){
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 90,40 ,0 );
        }
        ledStrip.setData(ledBuffer);  
    }

    public void PickCube(){
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 60,0 ,60);
        }
        ledStrip.setData(ledBuffer);  
    }

    public void HybridPickConeCube(){
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 0,120 ,0);
        }
        ledStrip.setData(ledBuffer);  
    }
}
