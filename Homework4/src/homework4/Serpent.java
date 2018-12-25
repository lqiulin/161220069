package homework4;

import javafx.scene.image.Image;

public class Serpent extends Creature {
	public Serpent(Creature[][] map) {
		velX = -50;
		goodguy = false;
		aliveImage = new Image(getClass().getResourceAsStream("serpent.png"));
		deadImage = new Image(getClass().getResourceAsStream("death.png"));
		creatureMap = map;
	}
}
