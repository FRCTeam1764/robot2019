package frc.robot.util;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;

public class MaxUtil
{
    public static void conf_pidf(CANSparkMax this_max, double p, double i, double d, double f)
    {
        CANPIDController ctrl = this_max.getPIDController();
        ctrl.setP(p);
        ctrl.setI(i);
        ctrl.setD(d);
        ctrl.setFF(f);
    }
}