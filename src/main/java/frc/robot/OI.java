/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.CameraServerJNI;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.JoystickBase;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.CameraTrack;
import frc.robot.commands.DriveWithJoystick;
import frc.robot.commands.LiftToPosition;
import frc.robot.commands.PulseLift;
import frc.robot.commands.PulseWheels;
import frc.robot.commands.PulseWrist;
import frc.robot.commands.WristToPosition;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);

  // There are a few additional built in buttons you can use. Additionally,
  // by sub%classing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());
  private static double adjustControl(double input)
	{
		boolean isNegative = input<0;
		input = Math.sqrt(Math.abs(input))* (isNegative? -1 : 1);
		return input;
	}
	
	public static Joystick joy0 = new Joystick(0);
	
	
	static double compensateController(double in, double crapControlerAdjust)
    {
        double crapControlAdjust = .22f;
        if (Math.abs(in) > crapControlAdjust) {

            double signOfControlScale = (in > 0.0f) ? 1.0f : -1.0f;

            double slopeAddjustment = 1.f / (1 - crapControlAdjust);

            double value = (Math.abs(in) - crapControlAdjust) * signOfControlScale * slopeAddjustment;

            return value;
        }
        else
        {
            return 0.f;
        }

    }
	public static double getAdjustedY()
	{
		double input = joy0.getY();
		return compensateController(input,.22f);
	}
	public static double getAdjustedZ()
	{
		double input = joy0.getZ();
		return compensateController(input,.22f);
	}
	public static double getXboxLeft()
	{
		return joy0.getRawAxis(1);
	}
	public static double getXboxRight()
	{
		return joy0.getRawAxis(5);
    }

    public JoystickButton camera_track_button = new JoystickButton(joy0, 9);
    public JoystickButton pulse_wrist_button = new JoystickButton(joy0,7);
    public JoystickButton pulse_wrist_back_button = new JoystickButton(joy0,8);
    public JoystickButton pulse_lift_button = new JoystickButton(joy0,11);
    public JoystickButton pulse_lift_back_button = new JoystickButton(joy0,12);

    public JoystickButton wrist_test = new JoystickButton (joy0,5);
    public JoystickButton lift_test = new JoystickButton(joy0,10);

    public JoystickButton ball_in = new JoystickButton(joy0,1);
    public JoystickButton ball_out = new JoystickButton(joy0,2);

    public OI()
    {
        camera_track_button.whileHeld(new CameraTrack());
        camera_track_button.whenReleased(new DriveWithJoystick());
        pulse_wrist_button.whileHeld(new PulseWrist(.25f));
        pulse_wrist_back_button.whileHeld(new PulseWrist(-.2f));

        pulse_lift_button.whileHeld(new PulseLift(1.f));
        pulse_lift_back_button.whileHeld(new PulseLift(-.3f));

        lift_test.whileHeld(new LiftToPosition(2000.f));
        wrist_test.whileHeld(new WristToPosition(-290.f));

        ball_in.whileHeld(new PulseWheels(1.f));
        ball_out.whileHeld(new PulseWheels(-1.f));
    }
    
}
