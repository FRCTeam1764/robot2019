/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 * An example command.  You can replace me with your own command.
 */
public class ExampleCommand extends Command {
  public ExampleCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.lift);
    requires(Robot.intake);
    requires(Robot.chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.lift.zero();
    Robot.intake.zero_wrist();


    Robot.chassis.zero();
    Robot.chassis.enable();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    //Robot.lift.set_speed_with_velocity(200.f); 

     //7500 be the top hole
    //Robot.lift.set_speed_with_position((-1000/2.f)-300);

    //Robot.intake.set_wrist_with_position(100.f);
    //Robot.lift.set_speed_with_percent(-.6f);

    Robot.chassis.setSetpoint(180.f);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.intake.set_wrist_with_percent(0.f);
    Robot.chassis.disable();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
