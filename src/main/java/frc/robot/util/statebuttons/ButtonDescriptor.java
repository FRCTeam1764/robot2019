package frc.robot.util.statebuttons;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

public class ButtonDescriptor
{

    public static int[] all_states(int size)
    {
        int[] result = new int[size];
        int max_result = 0;
        for (int i=0;i < size;i++)
        {
            result[max_result] = i;
            max_result++;
        }
        return result;
    }
    public static int[] all_states_but(int[] rejects, int size)
    {
        int[] result = new int[size-rejects.length];
        int max_result = 0;
        for (int i=0; i < size; i++)
        {
            boolean rejected = false;
            for (int reject : rejects)
            {
                if (i == reject)
                {
                    rejected = true;
                }
            }
            if (rejected)
            {
                continue;
            }
            result[max_result] = i;
            max_result++;
        }
        return result;
    }

    public JoystickButton button;
    public int[] states_active;
    public Command command_to_use;
    public int new_state_after_run;
    boolean stop_after_release;
    /*!
     * If _new_state_after_run is set to -1, then a
     * state change will not occur.
     */
    public ButtonDescriptor(JoystickButton _button, int[] _states_active, Command _command_to_use, int _new_state_after_run, boolean _stop_after_release)
    {
        button = _button;
        states_active = _states_active;
        command_to_use = _command_to_use;
        new_state_after_run = _new_state_after_run;
        stop_after_release = _stop_after_release;
    }

    protected void update()
    {
        boolean last_pushed = pushed;
        pushed = button.get();

        just_pushed = (pushed && !last_pushed);
        just_released = (!pushed && last_pushed);
    }
    protected boolean just_pushed = false;
    protected boolean just_released = false;
    protected boolean pushed = false;
    //Note: If stop_after_release isn't true, then
    //running may not be accurate
    protected boolean running = false;
}