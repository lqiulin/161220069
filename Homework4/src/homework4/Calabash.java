package homework4;

import java.util.ArrayList;

import javafx.scene.image.Image;

enum COLOR {
	RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, PURPLE;
}

public class Calabash extends Creature {
	private COLOR color;

	public Calabash(COLOR color, Creature[][] map, ArrayList<String> info) {
		this.color = color;
		// attack = 5;
		velX = 50;
		goodguy = true;
		// aliveImage = new Image(getClass().getResourceAsStream("calabash2.png"));
		// System.out.println(color.ordinal());
		aliveImage = new Image(getClass().getResourceAsStream("/calabash" + color.ordinal() + ".png"));
		deadImage = new Image(getClass().getResourceAsStream("/death.png"));
		creatureMap = map;
		infoItem = info;

	}

}
