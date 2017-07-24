package parser.abilities;

import game.GameBoard;
import game.Player;
import parser.commons.TargetProperty;

public class AbilityPartSwap extends AbilityPart{

    private final TargetProperty source;
    private final TargetProperty destination;

    public AbilityPartSwap(TargetProperty source, TargetProperty destination) {
        super("swap");
        this.source = source;
        this.destination = destination;
        
        properties.add(source);
        properties.add(destination);
    }

    @Override
    public boolean use(GameBoard targetBoard, Player owner) {
        return false;
    }

    @Override
    public String getDescriptionString() {
        return "Swap from " + source + " to " +destination;
    }
}