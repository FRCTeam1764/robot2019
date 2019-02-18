package frc.robot.util;

import frc.robot.Robot;
import frc.robot.subsystems.Jetson;


public class JetsonThread extends Thread
{

    private Jetson my_jetson;

    public boolean do_stop = true;

    public JetsonThread(Jetson passed_jetson)
    {
        my_jetson = passed_jetson;
    }
    @Override
    public void run()
    {
        while(true)
        {
            if (!Robot.jetson.has_connection)
            {
                System.out.println("---NO JETSON CONNECTION. TRYING TO RECONNECT TO CAMERA TRACK!---");
                Robot.jetson.try_to_connect();
            }
            try 
            {
                my_jetson.update_jetson_info();
            } 
            catch (Exception e)
            {
                e.printStackTrace();
                my_jetson.try_to_connect();
            }
            synchronized(this)
            {
                if(do_stop)
                {
                    break;
                }
            }
        }
    }

}