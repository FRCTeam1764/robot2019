/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import java.util.Vector;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Assists in tuning PIDs.
 * 
 * Simply check if had_change is true, and if it is, update you motor
 * controller to this class's p,i,d. 
 * 
 * Then call .update()
 * 
 * Do it in that order so that your PIDs will default to your default values. Trust me.
 */
public class PIDUtil {


    public boolean had_change = true;
    public double p;
    public double i;
    public double d;

    private double old_p;
    private double old_i;
    private double old_d;

    private double def_p;
    private double def_i;
    private double def_d;
    
    private String name; 
    public PIDUtil(String _name, double _def_p, double _def_i, double _def_d)
    {
        name = _name;
        def_p = _def_p;
        def_i = _def_i;
        def_d = _def_d;

        had_change = false;

        p = def_p;
        i = def_i;
        d = def_d;

        SmartDashboard.putNumber(name +" d",def_d);
        SmartDashboard.putNumber(name +" i",def_i);
        SmartDashboard.putNumber(name +" p",def_p);
    }
    public void update()
    {
        old_p=p;
        old_i=i;
        old_d=d;

        d = SmartDashboard.getNumber(name+" d",def_d);
        i = SmartDashboard.getNumber(name+" i",def_i);
        p = SmartDashboard.getNumber(name+" p",def_p);

        had_change = false;
        if ( (p != old_p) || (i != old_i) || (d != old_d))
        {
            had_change = true;
        }
    }
}
