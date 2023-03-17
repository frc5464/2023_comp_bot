package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vacuum {
    CANSparkMax intake = new CANSparkMax(8, MotorType.kBrushless);
    RelativeEncoder intakeEncoder;

    public double intakeRotations;

    AnalogInput distancefrontsense = new AnalogInput(1);
    public double distfront = 0;

    AnalogInput intakeDistance = new AnalogInput(2);
    double intdist = 0;

    AnalogInput pickupDistance = new AnalogInput(0);
    public double pickdist = 0;

    public DigitalInput BreakBeamIntake = new DigitalInput(6);   
    public boolean bbintake;

public void Init(){
    intakeEncoder = intake.getEncoder();
    distancefrontsense.setAverageBits(12);
}

public void BreakBeam(){
    SmartDashboard.putBoolean("Break Beam Cone", bbintake);
}

public void DisplayStats(){
    intakeRotations = intakeEncoder.getPosition();
}

public void DistanceCheck(){
    distfront = (distancefrontsense.getAverageVoltage());
    SmartDashboard.putNumber("FrontDistance", distfront);
}

public void inrun(){
        intake.set(0.6);
}
    
public void outrun(){
        intake.set(-0.6);
}

public void AutoOutconeIncubeintakerun100(){
    intake.set(-1.0);
}

public void stoprun(){
        intake.set(0);
}

public void IntakeLength(){
    intdist = (intakeDistance.getAverageVoltage())*(1000); //multplying by 1000 makes more readable bigggg numbers hehe 
    SmartDashboard.putNumber("Intake Length from Cone", intdist);
}

public void PickupLength(){
    pickdist = (pickupDistance.getAverageVoltage())*(1000);
    SmartDashboard.putNumber("Ditance from the requested object", pickdist);
}

}
