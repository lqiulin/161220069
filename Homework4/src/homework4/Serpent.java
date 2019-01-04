package homework4;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Serpent extends Creature {
	public Serpent(Creature[][] map, ArrayList<String> info) {
		velX = -50;
		goodguy = false;
		aliveImage = new Image(getClass().getResourceAsStream("/serpent.png"));
		deadImage = new Image(getClass().getResourceAsStream("/death.png"));
		creatureMap = map;
		infoItem = info;
	}
}
