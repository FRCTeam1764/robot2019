package frc.robot.util.statebuttons;

public class StateManager
{

    int current_state;

    private ButtonDescriptor[] button_descriptors;

    



    /*!
     * Allows you to have different buttons perform different actions depending on the current state.
     * Default state is zero.
     */
    public StateManager(ButtonDescriptor[] _button_descriptors)
    {
        button_descriptors = _button_descriptors;
    }
    public void reset()
    {
        current_state = 0;
        for (ButtonDescriptor desc : button_descriptors)
        {
            desc.running = false;
        }
    }
    public void execute()
    {
        for (ButtonDescriptor desc : button_descriptors)
        {
            desc.update();
            //check if its active in this current state
            boolean is_active = false;
            for (int state_active : desc.states_active)
            {
                if (state_active == current_state)
                {
                    is_active = true;
                    break;
                }
            }
            //is the button's pushed, run the code and maybe change state
            if (is_active && desc.just_pushed)
            {
                desc.command_to_use.start();
                desc.running = true;
                if (desc.new_state_after_run != -1)
                {
                    current_state = desc.new_state_after_run;
                }
            }
            else if (desc.just_released && desc.running && desc.stop_after_release)
            {
                desc.command_to_use.cancel();
                desc.running = false;
            }
        }
    }
}


