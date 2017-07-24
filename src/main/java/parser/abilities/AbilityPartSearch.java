package parser.abilities;

import game.GameBoard;
import game.Player;
import parser.commons.Filter;
import parser.commons.TargetProperty;
import parser.tokenizer.Token;
import parser.tokenizer.TokenString;

public class AbilityPartSearch extends AbilityPart {
    private TargetProperty target;
    private TokenString source;
    private Filter filter;
    private Token amount;

    public AbilityPartSearch(TargetProperty target, TokenString source, Filter filter, Token amount) {
        super("Search");
        this.target = target;
        this.source = source;
        this.filter = filter;
        this.amount = amount;
    }

    @Override
    public boolean use(GameBoard targetBoard, Player owner) {
        return false;
    }

    @Override
    public String getDescriptionString() {
        return "Search " + target + " at source "+source.value+" with filter " + filter + " for "+amount;
    }

    @Override
    public String getCurrentDescription(GameBoard targetBoard, Player callingPlayer) {
        return "Search " + target + " at source "+source.value+" for "+amount.evaluateAsExpression(targetBoard, callingPlayer)+" ["+amount.getDisplayString()+"]";
    }
}