/*
 * description:  The class of trainer card
 * author(s):    Eric(Haotao) Lai
 * reviewer(s):
 * date:         2017-05-17
 */

package card;

public class TrainerCard extends Card {

    public enum TrainerType {ITEM, SUPPORTER, STADIUM,}
    private Ability ability;
    private TrainerType trainerType;

    public TrainerCard(String name, String type, Ability ability) {
    	
    	
    	this.name = name;
        this.cardType = CardType.TRAINER;
        this.trainerType = Enum.valueOf(TrainerType.class, type.toUpperCase());
        this.ability = ability;
    }
    
    public TrainerCard copy(){
        return new TrainerCard(name, trainerType.toString(), ability);
    }

    public Ability getAbility(){
        return ability;
    }

    public TrainerType getTrainerType() {
        return trainerType;
    }
}
