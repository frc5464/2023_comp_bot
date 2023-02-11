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
            ledBuffer.setRGB(i, 50, 0, 0);
        }
        ledStrip.setData(ledBuffer);
    }

    public void DefaultLight(){
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 0, 0,50);
        }
        ledStrip.setData(ledBuffer);        
    }

    public void Manualmode(){
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 20, 0, 20);
        }
        ledStrip.setData(ledBuffer);        
    }

    public void Pidmode(){
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 20, 20, 50);
        }
        ledStrip.setData(ledBuffer);        
    }

    public void PickCone(){
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 50,20 ,0 );
        }
        ledStrip.setData(ledBuffer);  
    }

    public void PickCube(){
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, 20,0 ,20);
        }
        ledStrip.setData(ledBuffer);  
    }
}
