/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.concurrent.Semaphore;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.util.network.Network;

/**
 * Add your docs here.
 */
public class Jetson extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  Network jetson_net;

  public float camera_angle_main = 0.f;
  public int update_count = 0;

  public boolean has_connection = false;

  public Semaphore info_change_mutex = new Semaphore(1);


  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  public void try_to_connect()
  {
    if (jetson_net != null)
    {
      try {
        jetson_net.close();
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    //because wpilib does not provide close function for the robot (as far as I know),
    //there is no way close the port. thus, each time the robot code is restarted it makes
    //a port into an orphan. this is a workarround.
    short start_port = 5667;
    for (short i = start_port; i < start_port + 11; i++)
    {
      try
      {
        jetson_net = new Network("1764-tegra.local",i);
        has_connection = true;
        System.out.println("---JETSON CONNECTED---");
        break;
      }
      catch (Exception e)
      {
        has_connection = false;
        //e.printStackTrace();
      }
    }
    if (!has_connection)
    {
      System.out.println("---NO CONNECTION TO JETSON---");
    }
  }
  public Jetson()
  {
    try_to_connect();
  }

  public void close()
  {
    try {
      jetson_net.close();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  /*!
   * Update all public variable in this class to their current state on the jetson.
   */
  public void update_jetson_info() throws Exception
  {
    if (!has_connection)
    {
      throw new RuntimeException("No connection to jetson");
    }
    else
    {
      byte[] msg = jetson_net.read_variable_size_message();
      try
      {
        info_change_mutex.acquire();
        try
        {
          int c = 0; //This is the position we're currently reading from. It must be bumped up each read, by exactly the size of the read.
          //Long = 8 bytes
          //Int = 4 bytes
          //Float = 4 bytes
          int serialization_version = Network.decode_int(msg,c); //Get the serialization version
          c += 4;

          camera_angle_main = Network.decode_float(msg, c); 
          c += 4;

         //System.out.println("camera angle "+camera_angle_main);

          //jetson_net.write("T");
          update_count++;
        }
        finally
        {
          info_change_mutex.release();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
}
