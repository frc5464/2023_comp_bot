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
    
    // maximum brightness value
    int br = 120;

    // sets the whole string to the same color
    private void setSolidLedColor(String color){
        
        int[] rgb = colorPicker(color);

        int r = rgb[0];
        int g = rgb[1];
        int b = rgb[2];

        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i,r,g,b);
        }
        ledStrip.setData(ledBuffer);
    }

    // Sends RGB integers based on a requested color string
    private int[] colorPicker(String color){
        int[] rgb = new int[3];
        int r,g,b;
        // feel free to add more colors onto this list
        switch(color){
            case "blue":
                r = 0;
                g = 0;
                b = br;
            case "green":
                r = 0;
                g = br;
                b = 0;
            case "red":
                g = 0;
                b = 0;
                r = br;    
            case "purple":
                r = br/2;
                g = 0;
                b = br/2;
            case "orange":
                r = br/ (3/2);
                g = br / 2;
                b = 0;
            case "light_blue":
                r = br/2;
                g = br/2;
                b = br;
            case "cyan":
                r = 0;
                g = br/2;
                b = br/2;
            default:
               r=0;g=0;b=0;
        }
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
        return rgb;
    }

    // ============================================= Public Functions
    public void Init(){
        // first time setting the length of the LEDs
        ledStrip.setLength(ledBuffer.getLength()); 
        ledStrip.setData(ledBuffer);
        ledStrip.start();
    }

    public void QuestionError(){
        setSolidLedColor("red");
    }

    public void DefaultLight(){
        setSolidLedColor("blue");
    }

    public void Manualmode(){
        setSolidLedColor("purple");    
    }

    public void Pidmode(){
        setSolidLedColor("light_blue");      
    }

    public void PickCone(){
        setSolidLedColor("orange");
    }

    public void PickCube(){
        setSolidLedColor("purple");
    }

    public void HybridPickConeCube(){
        setSolidLedColor("green");
    }
}
