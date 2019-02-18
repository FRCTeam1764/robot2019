/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
/*
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
*/
/***
 * This operates off two PIDs.
 * 
 * The PIDSubsytem one is for gyro-based turning.
 * 
 * The other one utilizes the hardware PID in the Spark Max. 
 * It utilizes the encoders on the drive train.
 */
public class Chassis extends PIDSubsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  /*!
   * In terms of PID control, fronts are the master
   * and backs are the slaves.
   */
  CANSparkMax backLeft = new CANSparkMax(11,MotorType.kBrushless);
  CANSparkMax middleLeft = new CANSparkMax(12,MotorType.kBrushless);
  CANSparkMax frontLeft = new CANSparkMax(13,MotorType.kBrushless);

  CANSparkMax frontRight = new CANSparkMax(14,MotorType.kBrushless);
  CANSparkMax middleRight = new CANSparkMax(15,MotorType.kBrushless);
	CANSparkMax backRight = new CANSparkMax(16,MotorType.kBrushless);

  /*TalonSRX frontRight = new TalonSRX(9);
	TalonSRX backRight = new TalonSRX(8);
  TalonSRX backLeft = new TalonSRX(1);
  TalonSRX frontLeft = new TalonSRX(2);*/

  public static double KP = 0.0275;
	public static double KI = 0.00;
  public static double KD = 0.06;


  public Chassis()
  {

    super(KP,KI,KD);
    setAbsoluteTolerance(1);

    IdleMode idle_mode = IdleMode.kCoast;

    frontRight.setIdleMode(idle_mode);
    middleRight.setIdleMode(idle_mode);
    backRight.setIdleMode(idle_mode);

    frontLeft.setIdleMode(idle_mode);
    middleLeft.setIdleMode(idle_mode);
    backLeft.setIdleMode(idle_mode);
  }


  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  public void set_speed_with_percent(double percent_left,double percent_right)
  {
    frontLeft.set(-percent_left);
    middleLeft.set(-percent_left);
    backLeft.set(-percent_left);

    frontRight.set(percent_right);
    middleRight.set(percent_right);
    backRight.set(percent_right);
  }
  public void set_speed_with_velocity(double velocity_left, double velocity_right)
  {
    frontLeft.pidWrite(velocity_left);
    frontRight.pidWrite(velocity_right);

    backLeft.set(frontLeft.get());
    middleLeft.set(frontLeft.get());
    backRight.set(frontRight.get());
    middleRight.set(frontRight.get());
  }

  public void debug()
  {
    //System.out.println(frontRight.getEncoder().getVelocity());
  }

  public void reset_gyro()
  {
    Robot.ahrs.reset();
  }

  private double zero_angle = 0;
  public void zero()
  {
    zero_angle = Robot.ahrs.getAngle();
  }

  public double get_angle()
  {
    return Robot.ahrs.getAngle()-zero_angle;
  }
  

  @Override
  protected double returnPIDInput() {
    return get_angle();
  }

  @Override
  protected void usePIDOutput(double output) {

    set_speed_with_percent(-output,output); //TODO is this backwards?
  }
}
