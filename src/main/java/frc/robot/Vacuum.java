package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Vacuum {
    CANSparkMax intake = new CANSparkMax(8, MotorType.kBrushless);

    public void inrun(){
        intake.set(0.7);
    }
}