package parser.debug;

import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parser.cards.CardParser;
import parser.tokenizer.LanguageTokenizer;
import parser.tokenizer.TokenScope;
import parser.tokenizer.TokenStream;

/**
 * Created by frede on 2017-05-30.
 */
public class CardDebugParser {
  
  LanguageTokenizer tokenizer;

  static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
  
  public CardDebugParser(String fileName){
      tokenizer = new LanguageTokenizer(fileName);
  }
  
  public void parse(){
      List<TokenScope> lineScopes = tokenizer.tokenize();
      lineScopes.forEach((lineScope)->{
          parseCard(lineScope);
      });
  }
  
  private void parseCard(TokenScope scope){
    if(scope.tokens.size() < 2){
      return;
    }
      TokenStream tokenStream = new TokenStream(scope.tokens);
      String name = tokenStream.validateTokenString().value;
      
      String type = tokenStream.validateTokenString().value;
      
      switch (type){
        case "pokemon": {
            parsePokemon(name, tokenStream);
        }break;
            
      }
      
      
  }
  
  private void parsePokemon(String name, TokenStream tokenStream){
      if(tokenStream.validateTokenString("cat") == null){
          log.error("No category");
          return;
      }
      
      String stage = tokenStream.validateTokenString().value;
      
      switch(stage){
        case "basic":
          parseBasicPokemon(name, tokenStream);
          break;
      }
      
  }
  
  private void parseBasicPokemon(String name, TokenStream tokenStream){
      log.debug(name+": ");
    
      if(tokenStream.validateTokenString("cat") == null){
          log.error(name + " No cat");
          return;
      }
      String type = tokenStream.validateTokenString().value;
      
      int hp = tokenStream.validateTokenInteger().value;
      
      if(tokenStream.validateTokenString("retreat") != null){
        
        EnergyCost retreatCost = new EnergyCost();
        
        if(tokenStream.validateTokenString("cat") == null){
            log.error(name + " No cat");
            return;
        }
          String retreatCostName = tokenStream.validateTokenString().value;
        int retreatCostNum = tokenStream.validateTokenInteger().value;
        retreatCost.addEnergy(retreatCostName,retreatCostNum);
          log.debug("retreat: " + retreatCost);

        
      }
      
      
      if(tokenStream.validateTokenString("attacks") != null){
        tokenStream.backtrack();
        while(tokenStream.getNextToken() != null) {
          EnergyCost cost = new EnergyCost();
          do {
            if (tokenStream.validateTokenString("cat") == null) {
              log.error(name + " No cat");
              return;
            }
            String energyType = tokenStream.validateTokenString().value;
            int energyNum = tokenStream.validateTokenInteger().value;
            cost.addEnergy(energyType, energyNum);
          } while (tokenStream.validateTokenSeparator() != null);

          int abilityLineNum = tokenStream.validateTokenInteger().value;
          log.debug(abilityLineNum + " : " + cost);
          
          
        }
      }
  }

}
