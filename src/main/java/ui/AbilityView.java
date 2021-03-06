/*
 * description:  Displays info about an ability
 * author(s):    frede
 * reviewer(s):
 * date:         2017-05-17
 */

package ui;

import card.Ability;
import game.GameBoard;
import game.Player;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class AbilityView extends BorderPane {

    //TODO pass ability object
    public AbilityView(GameBoard gameBoard, Player owner, Ability ability) {

        this.getStyleClass().add("Ability");

        Label name = new Label(ability.getTemplate().name);
        name.getStyleClass().add("Name");

        setLeft(name);
        setCenter(new Label(ability.getEnergyCost().toCondensedString()));
        StringBuilder descriptionBuilder = new StringBuilder();
        ability.getTemplate().parts.forEach(part->{
                descriptionBuilder.append(part.getCurrentDescription(gameBoard, owner) + "\n");
        });
        ScrollPane descriptionScrollPane = new ScrollPane();
        Text description = new Text(descriptionBuilder.toString());

        descriptionScrollPane.setContent(description);
        setRight(descriptionScrollPane);
    }

}
