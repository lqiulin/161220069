package homework4;

import javafx.scene.image.Image;

public class Scorpion extends Creature {
	boolean littlescrop;

	public Scorpion(boolean b, Creature[][] map) {
		littlescrop = b;
		velX = -50;
		goodguy = false;
		if (littlescrop)
			aliveImage = new Image(getClass().getResourceAsStream("littlescorpion.png"));
		else
			aliveImage = new Image(getClass().getResourceAsStream("scorpion.png"));
		deadImage = new Image(getClass().getResourceAsStream("death.png"));
		creatureMap = map;
	}

}
