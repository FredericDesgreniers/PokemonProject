package parser.abilities.parts;

import card.Card;
import game.GameBoard;
import game.Player;
import parser.abilities.properties.TargetProperty;
import parser.abilities.properties.TokenProperty;
import parser.tokenizer.Token;

/**
 * Draw an amount of cards for a player
 */
public class AbilityPartDraw extends AbilityPart{

  private TargetProperty target;
  private Token amount;
  
  public AbilityPartDraw(TargetProperty target, Token amount) {
    super("Draw");
    this.target = target;
    this.amount = amount;
    
    properties.add(target);
    properties.add(new TokenProperty("Amount", amount));
  }

  @Override
  public boolean use(GameBoard targetBoard, Player owner, Card callingCard) {
      int cardAmount = amount.evaluateAsExpression(targetBoard, owner);
      Player player;
      if (target.target == null) {
          player = owner;
      } else {
          player = targetBoard.getOtherPlayer(owner);
      }
      for (int i = 0; i < cardAmount; i++) {
          player.drawOneCard();
      }
      return true;
  }

  @Override
  public String getDescriptionString() {
    return "Draw "+amount.getDisplayString()+" card(s) on "+target ;
  }

    public String getCurrentDescription(GameBoard targetBoard, Player callingPlayer){
        return "Draw "+amount.evaluateAsExpression(targetBoard, callingPlayer)+"["+amount.getDisplayString()+"] card(s) on "+target ;
    }
}
