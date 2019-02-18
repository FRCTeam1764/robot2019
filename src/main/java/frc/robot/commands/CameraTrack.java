/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.util.JetsonThread;


public class CameraTrack extends Command {
  public CameraTrack() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.jetson);
    requires(Robot.chassis);
   
  }
  JetsonThread my_jetson_thread;

  private int last_update = 0;

  // Called just before this Command runs the first time
  @Override
  protected void initialize() 
  {
    my_jetson_thread = new JetsonThread(Robot.jetson);
    synchronized(my_jetson_thread)
    {
      if (my_jetson_thread.do_stop)
      {
        my_jetson_thread.do_stop = false;
        my_jetson_thread.start();
        System.out.println("Autonomous initialized.");
      }
    }
    Robot.chassis.enable();
    
  }


  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() 
  {
    
    try
    {
      Robot.jetson.info_change_mutex.acquire();
      try  
      {
        //if there hasnt been an update, dont reset gyro
        if (Robot.jetson.update_count != last_update)
        {
          Robot.chassis.zero();
          System.out.println("reset setpoint "+(Robot.jetson.camera_angle_main*(180/3.141592654)));
        }
        Robot.chassis.setSetpoint(Robot.jetson.camera_angle_main*(180/3.141592654));
        /*float speed = 0.5f;
        float deg60 = 1.047197551333333f;
        speed *= Math.abs(Robot.jetson.camera_angle_main) / deg60;
        speed = Math.max(speed,.15f);
        System.out.println("MOVING SPEED "+speed);

        if (Robot.jetson.camera_angle_main >= 0.f)
        {
          Robot.chassis.set_speed_with_percent(-speed,speed);
        }
        else
        {
          Robot.chassis.set_speed_with_percent(speed,-speed);
        }*/
        last_update = Robot.jetson.update_count;
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      finally
      {
        Robot.jetson.info_change_mutex.release();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.chassis.set_speed_with_percent(0, 0);
    synchronized(my_jetson_thread)
    {
      my_jetson_thread.do_stop = true;
    }
    Robot.chassis.disable();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
