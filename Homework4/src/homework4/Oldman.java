package homework4;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Oldman extends Creature {
	public Oldman(Creature[][] map, ArrayList<String> info) {
		velX = 50;
		goodguy = true;
		aliveImage = new Image(getClass().getResourceAsStream("oldman.png"));
		deadImage = new Image(getClass().getResourceAsStream("death.png"));
		creatureMap = map;
		infoItem = info;
	}

}
