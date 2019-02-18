/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.util.statebuttons.ButtonDescriptor;
import frc.robot.util.statebuttons.StateManager;

public class DriveWithJoystick extends Command {
  private StateManager lift_state_manager;
  private ButtonDescriptor[] buttons;
  public DriveWithJoystick() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.chassis);


    buttons = new ButtonDescriptor[1];
    {
      int[] exceptions = {LiftState.ground};
      buttons[0] = new ButtonDescriptor(new JoystickButton(OI.joy0,10), ButtonDescriptor.all_states_but(exceptions,LiftState.SIZE), new LiftToPosition(100.f), LiftState.ground, false);
    }
    lift_state_manager = new StateManager(buttons);
    
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    lift_state_manager.reset();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double left = (OI.getAdjustedY()-OI.getAdjustedZ());
    double right = (OI.getAdjustedY()+OI.getAdjustedZ());

    /*double start_speed = 0.f;
    if (left > 0)
    {
      left = Math.max(start_speed,left);
    }
    else if (left < 0)
    {
      left = Math.min(-start_speed,left);
    }
    if (right > 0)
    {
      right = Math.max(start_speed,right);
    }
    else if (left < 0)
    {
      right = Math.min(-start_speed,right);
    }*/


    
    //Robot.chassis.set_speed_with_percent(left,right);

    lift_state_manager.execute();
    //Robot.chassis.debug();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.chassis.set_speed_with_percent(0,0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
