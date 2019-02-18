package frc.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXPIDSetConfiguration;

import frc.robot.util.PIDUtil;
import frc.robot.util.TalonUtil;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class Lift extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
    final int NUM_LIFT_TALONS=4;

    TalonSRX[] lift_talons;
    TalonSRX[] non_encoded_talons;
    TalonSRX encoded_talon;

    private float zero_pos;

    private PIDUtil tuner = new PIDUtil("Lift",.001f,.001f,.011f);


    public void zero()
    {
        System.out.println("--ZERO LIFT--");
        zero_pos = encoded_talon.getSelectedSensorPosition(0);
    }


    public Lift()
    {
        lift_talons = new TalonSRX[NUM_LIFT_TALONS];
        non_encoded_talons = new TalonSRX[NUM_LIFT_TALONS-1];
        
        lift_talons[0] = new TalonSRX(1);
        lift_talons[1] = new TalonSRX(2);
        lift_talons[2] = new TalonSRX(4);
        lift_talons[3] = new TalonSRX(3);

        lift_talons[2].setInverted(true);
        lift_talons[3].setInverted(true);
        

        encoded_talon = lift_talons[2];

        
        non_encoded_talons[0] = lift_talons[0];
        non_encoded_talons[1] = lift_talons[1];
        non_encoded_talons[2] = lift_talons[3];


/*
        lift_talons[0] = new TalonSRX(3);
        lift_talons[1] = new TalonSRX(4);

        encoded_talon = lift_talons[0];
        non_encoded_talons[0] = lift_talons[1];
*/
        

        //TalonUtil.conf_pidf(encoded_talon, 0.029, 0.0002, 2.5f, 0);
        for (TalonSRX this_srx : lift_talons)
        {
            this_srx.setNeutralMode(NeutralMode.Coast);
        }
    }
    

    public void update_other_talons()
    {
        for (TalonSRX this_srx : non_encoded_talons)
        {
            this_srx.set(ControlMode.PercentOutput,encoded_talon.getMotorOutputPercent());
        }
    }
    public void set_speed_with_velocity(double speed)
    {
        System.out.println(encoded_talon.getSelectedSensorPosition(0));
        encoded_talon.set(ControlMode.Velocity,speed);
        update_other_talons();
    }
    public void set_speed_with_position(double position)
    {
        if (tuner.had_change)
        {
            TalonUtil.conf_pidf(encoded_talon, tuner.p, tuner.i, tuner.d, 0);
        }
        tuner.update();


        System.out.println("nonzeroed pos "+Float.toString(encoded_talon.getSelectedSensorPosition(0)));

        System.out.println("liftpos "+Float.toString(encoded_talon.getSelectedSensorPosition(0)-zero_pos));
        encoded_talon.set(ControlMode.Position,position+zero_pos);
        update_other_talons();
    }
    public void set_speed_with_percent(double percent)
    {
        System.out.println(encoded_talon.getSelectedSensorPosition(0));
        for (TalonSRX this_srx : lift_talons)
        {
            this_srx.set(ControlMode.PercentOutput,percent);
        }
    }


}
