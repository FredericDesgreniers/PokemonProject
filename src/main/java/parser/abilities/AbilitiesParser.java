package parser.abilities;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parser.commons.DestinationProperty;
import parser.commons.TargetProperty;
import parser.tokenizer.LanguageTokenizer;
import parser.tokenizer.Token;
import parser.tokenizer.TokenCondition;
import parser.tokenizer.TokenInteger;
import parser.tokenizer.TokenScope;
import parser.tokenizer.TokenSeparator;
import parser.tokenizer.TokenStream;
import parser.tokenizer.TokenString;
import parser.tokenizer.TokenType;
import parser.ui.AbilityTemplatePane;
import parser.ui.TokenPane;

/**
 * Created by frede on 2017-05-23.
 * 
 * Parses abilities from text file using a LanguageTokenizer
 */
public class AbilitiesParser {

  static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
  
  LanguageTokenizer tokenizer;

  /**
   * 
   * @param fileName File name that needs to be parsed
   */
  public AbilitiesParser(String fileName){
    tokenizer = new LanguageTokenizer(fileName);
  }

  /**
   * Parse a file into a series of abilities
   * @return List of all abilities parsed
   */
  public AbilityTemplate[] parse() {
    //Get a list of all tokens in the file using the tokenizer
    List<TokenScope> scopes = tokenizer.tokenize();
    
    //Where the abilities are going to be stored
    List<AbilityTemplate> templates = new ArrayList<>();
    
    //For each "Scope" token, process it as a null / parent scope
    int i = 0;
    scopes.forEach((scope)->{
        templates.add(parseNullScope(scope));
    });
    
/**
    Stage stage = new Stage();
    stage.setTitle("Parser");
    
    
    BorderPane mainView = new BorderPane();
    
    TreeItem tokenItemPane = new TokenPane(scopes.toArray(new Token[0]));
    
    TreeView tokenView = new TreeView<>(tokenItemPane);
    tokenView.setMinWidth(600);
    mainView.setLeft(tokenView);
    
    TreeItem abilityItemPane = new AbilityTemplatePane((templates.toArray(new AbilityTemplate[0])));
    TreeView abilityView = new TreeView(abilityItemPane);
    abilityView.setMinWidth(600);
    mainView.setRight(abilityView);
    
    
    stage.setScene(new Scene(mainView, 1200,800));
    stage.show();
 **/

    
    return templates.toArray(new AbilityTemplate[0]);
  }

  /**
   * Parse a parent scope
   * @param scope scope to parse
   * @return
   */
  private AbilityTemplate parseNullScope(TokenScope scope){

    //get a token stream for this scope
    TokenStream tokenStream = new TokenStream(scope.tokens);
    
    //First token should always be the name of the ability
    String name = tokenStream.validateTokenString().value;
    if(name == null){
      log.error("Name token needs to be a string: " + tokenStream.getNextToken());
      return null;
    }
    
    //blank ability template
    AbilityTemplate template = new AbilityTemplate(name);
    
    //log.debug(" " + name+ " ");
    
    //Go through tokens, parsing them into ability parts
    AbilityPart abilityPart = null;
    while((abilityPart = parseNextPart(tokenStream)) != null) {
        template.parts.add(abilityPart);
    }
    
    return template;
  }

  private AbilityPart parseNextPart(TokenStream tokenStream){
      Token token = tokenStream.getNextToken();
      if(token == null){
          return null;
      }

    if(token instanceof TokenString){
      TokenString tokenString = (TokenString)token;
      switch(tokenString.value){
        case "deck":
          return parsePartDeck(tokenStream);
        case  "dam":
          return parseDamPart(tokenStream);
        case "draw":
          return parseDrawPart(tokenStream);
        case "cond":
          return parseCondPart(tokenStream);
        default:
          waitUntil(tokenStream, TokenType.SEPERATOR);
          return parseNextPart(tokenStream);
      }

    }else if(token instanceof TokenSeparator){
      return parseNextPart(tokenStream);
    }
    return null;
  }
  
  /**
   * Go through tokens until we reach the desired one
   * @param stream
   * @param type
   */
  private void waitUntil(TokenStream stream, TokenType type){
    Token token = null;  
    while((token = stream.getNextToken()) != null){
        if(token.type == type){
          break;
        }
    }
  }

  /**
   * Parse the deck ability part
   * @param tokenStream
   * @return
   */
  private AbilityPart parsePartDeck(TokenStream tokenStream){
    
      //log.debug("---Deck---");

      TargetProperty targetPart = TargetProperty.read(tokenStream);
    
      DestinationProperty destinationPart = DestinationProperty.read(tokenStream);
      
      TokenString choice = null;
      if(tokenStream.validateTokenString("choice") != null){
        choice = tokenStream.validateTokenString();
      }
      
      Token amount = tokenStream.getNextToken();
      if(!(amount instanceof TokenScope) && !(amount instanceof TokenInteger))
      {
        tokenStream.backtrack();
      }
      //log.debug(targetPart);
      //log.debug(destinationPart);
      //log.debug("Choice: "+Formatting.toSafeString(choice));
      return new AbilityPartDeck(targetPart, destinationPart, choice, amount);
  }

  /**
   * Parse the Dam (damage) ability part
   * @param tokenStream
   * @return
   */
  private AbilityPart parseDamPart(TokenStream tokenStream){
    //log.debug("---Dam---");

    TargetProperty targetPart = TargetProperty.read(tokenStream);
    
    Token amount = tokenStream.getNextToken();
    
    //log.debug(targetPart);
    //log.debug("Amount: " + amount);
    
    return new AbilityPartDam(targetPart, amount);
  }

  /**
   * Parse the Draw ability part
   * @param tokenStream
   * @return
   */
  private AbilityPart parseDrawPart(TokenStream tokenStream){
    TargetProperty target = new TargetProperty(tokenStream.validateTokenString(), null);
    
    Token amount = tokenStream.getNextToken();
    if(!(amount instanceof TokenScope) && !(amount instanceof TokenInteger))
    {
      tokenStream.backtrack();
    }
    
    return new AbilityPartDraw(target, amount);
  }

  private AbilityPart parseCondPart(TokenStream tokenStream){    
      Token type = tokenStream.getNextToken();
      
      AbilityPartCond abilityPartCond = new AbilityPartCond();
      if(type instanceof TokenString) {
        switch (((TokenString)type).value) {
          case "healed":
            TargetProperty targetProperty = TargetProperty.read(tokenStream);
            //apply if target has been healed
            break;
          case "flip":
            //apply 50% of the time
            break;
          case "ability":
            // ???
            break;
          case "choice":
            //player choses
            break;
        }
      }else if(type instanceof TokenCondition){

      }
      
      AbilityPart truePart = parseNextPart(tokenStream);
      AbilityPart falsePart = null;
      if(tokenStream.validateTokenString("else") != null){
        falsePart = parseNextPart(tokenStream);
      }
      
      abilityPartCond.setResults(truePart, falsePart);
    
      return abilityPartCond;
      
  }
}
