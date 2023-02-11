package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.hal.simulation.AnalogInDataJNI;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vacuum {
    CANSparkMax intake = new CANSparkMax(8, MotorType.kBrushless);
    AnalogInput distancesense = new AnalogInput(1);
    double distcm = 0;

public void DistanceCheck(){
    distcm = (distancesense.getAverageVoltage())*(1000);
    SmartDashboard.putNumber("Distance (cm)", distcm);
    
}

    public void inrun(){
        intake.set(0.7);
    }
    
    public void outrun(){
        intake.set(-1);
    }

    public void stoprun(){
        intake.set(0);
    }

}
