package frc.robot.util;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.ErrorCode;

public class TalonUtil
{
    public static void conf_pidf(TalonSRX this_srx,double p, double i, double d, double f)
    {
        int base_id = this_srx.getDeviceID();
        if ( this_srx.config_kP(0, p) != ErrorCode.OK)
        {
            throw new RuntimeException("failed to configure p on motor "+base_id);
        }
        if ( this_srx.config_kI(0, i) != ErrorCode.OK)
        {
            throw new RuntimeException("failed to configure i on motor "+base_id);
        }
        if ( this_srx.config_kP(0, d) != ErrorCode.OK)
        {
            throw new RuntimeException("failed to configure d on motor "+base_id);
        }
        if ( this_srx.config_kF(0, f) != ErrorCode.OK)
        {
            throw new RuntimeException("failed to configure f on motor "+base_id);
        }
    }   
}