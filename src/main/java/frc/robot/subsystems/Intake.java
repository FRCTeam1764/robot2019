/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.PIDUtil;
import frc.robot.util.TalonUtil;

/**
 * Add your docs here.
 */
public class Intake extends Subsystem 
{
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private TalonSRX wrist_motor;
  private TalonSRX intake_wheels_motor;

  private PIDUtil wrist_tuner = new PIDUtil("Wrist",20.f,0.011f,.7f);

  @Override
  public void initDefaultCommand() 
  {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public Intake()
  {
    wrist_motor = new TalonSRX(9);
    
    //TalonUtil.conf_pidf(wrist_motor,.055f,.001f,.07f,0.f);

    intake_wheels_motor = new TalonSRX(7);
  }

  private float zero_wrist_pos;


  public void zero_wrist()
  {
      System.out.println("--ZERO WRIST--");
      zero_wrist_pos = wrist_motor.getSelectedSensorPosition(0);
  }
  public double get_wrist()
  {
    return wrist_motor.getSelectedSensorPosition()-zero_wrist_pos;
  }
  public void set_wrist_with_percent(double percent)
  {
    wrist_motor.set(ControlMode.PercentOutput,percent);
    System.out.println("zeroval");
    System.out.println(zero_wrist_pos);
    System.out.println("encodie");
    System.out.println(get_wrist());
  }
  public void set_wrist_with_velocity(double velocity)
  {
    wrist_motor.set(ControlMode.Velocity,velocity);
  }
  public void set_wrist_with_position(double position)
  {
    if (wrist_tuner.had_change)
    {
        TalonUtil.conf_pidf(wrist_motor,wrist_tuner.p,wrist_tuner.i,wrist_tuner.d,0.f);
    }
    wrist_tuner.update();

    wrist_motor.set(ControlMode.Position,position+zero_wrist_pos);
    System.out.println("encodie");
    System.out.println(get_wrist());
  }



  public void set_intake_wheels_with_percent(double percent)
  {
    intake_wheels_motor.set(ControlMode.PercentOutput, percent);
  }

}
