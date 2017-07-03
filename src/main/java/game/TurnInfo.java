package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frede on 2017-07-03.
 */
public class TurnInfo {
    private TurnTrigger attackTrigger, energyTrigger, trainerTrigger;

    public void reset(){
        attackTrigger.reset();
        energyTrigger.reset();
        trainerTrigger.reset();
    }

    public TurnTrigger getAttackTrigger(){
        return attackTrigger;
    }

    public TurnTrigger getEnergyTrigger(){
        return energyTrigger;
    }

    public TurnTrigger getTrainerTrigger(){
        return trainerTrigger;
    }

    public class TurnTrigger{
        private boolean status = false;

        public void trigger(){
            status = true;
        }

        public void reset(){
            status = false;
        }

        public boolean getStatus(){
            return status;
        }
    }
}