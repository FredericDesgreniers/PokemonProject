package parser.abilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frede on 2017-05-23.
 */
public class AbilityTemplate {
    public String name;
    
    public List<AbilityPart> parts = new ArrayList<>();
    
    public AbilityTemplate(String name){
        this.name = name;
    }
}