package homework4;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Game extends Application {
	final int ROWS = 9;
	final int COLS = 15;
	final int PHOTOX = 80;
	final int PHOTOY = 100;
	private Creature[][] creatureMap = new Creature[ROWS][COLS];
	private Oldman oldman = new Oldman(creatureMap);
	private Calabash[] calabrothers = new Calabash[7];
	private Serpent serpent = new Serpent(creatureMap);
	private List<Scorpion> scorpion = new ArrayList<Scorpion>();
	private List<Creature> goodCreature = new ArrayList<Creature>();
	private List<Creature> badCreature = new ArrayList<Creature>();
	private List<CreatureThread> creaturethds = new ArrayList<CreatureThread>();
	boolean starthds;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Collect the Money Bags!");

		Group root = new Group();
		Scene theScene = new Scene(root);
		primaryStage.setScene(theScene);

		Canvas canvas = new Canvas(PHOTOX * (COLS + 1), PHOTOY * (ROWS + 1));
		System.out.println(PHOTOX * (COLS + 1) + "," + PHOTOY * (ROWS + 1));
		root.getChildren().add(canvas);

		GraphicsContext gc = canvas.getGraphicsContext2D();
		buildTheWorld(gc);

		// gc.drawImage( image, positionX, positionY );
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (creatureMap[i][j] != null) {
					// System.out.println(i * PHOTOX + "," + j * PHOTOY);
					// gc.drawImage(creatureMap[i][j].getAliveImage(), j * PHOTOY, i * PHOTOX);
					creatureMap[i][j].drawCreature(gc);
				}
			}
		}

		ArrayList<String> input = new ArrayList<String>();
		theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();
				if (!input.contains(code)) {
					System.out.println(e.getCode().toString());
					input.add(code);
				}
			}
		});

		/*
		 * theScene.setOnKeyReleased(new EventHandler<KeyEvent>() { public void
		 * handle(KeyEvent e) { String code = e.getCode().toString();
		 * input.remove(code); } });
		 */

		Image background = new Image(getClass().getResourceAsStream("background.png"));
		// LongValue lastNanoTime = new LongValue(System.nanoTime());

		gc.setFill(Color.RED);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2);
		Font theFont = Font.font("Times New Roman", FontWeight.BOLD, 60);
		gc.setFont(theFont);

		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				// Clear the canvas
				// gc.clearRect(0, 0, 512,512);

				// double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
				// lastNanoTime.value = currentNanoTime;

				if (input.contains("SPACE")) {
					if (!starthds) {
						starthds = true;
						System.out.println("start");
						for (int i = 0; i < creaturethds.size(); i++) {
							creaturethds.get(i).start();
						}
					}
				}

				gc.clearRect(0, 0, PHOTOX * (COLS + 1), PHOTOY * (ROWS + 1));
				gc.drawImage(background, 0, 0);

				if (!starthds) {
					for (int i = 0; i < ROWS; i++) {
						for (int j = 0; j < COLS; j++) {
							if (creatureMap[i][j] != null)
								creatureMap[i][j].drawCreature(gc);
						}
					}
				}
				if (gameOver(badCreature)) {
					for (int i = 0; i < creaturethds.size(); i++) {
						creaturethds.get(i).close();

					}
					gc.fillText("YOU WIN!", 60, 100);
					gc.strokeText("YOU WIN", 60, 100);
				} else if (gameOver(goodCreature)) {
					for (int i = 0; i < creaturethds.size(); i++) {
						creaturethds.get(i).close();
					}
					gc.fillText("GAME OVER!", 60, 100);
					gc.strokeText("GAME OVER!", 60, 100);
				}
			}
		}.start();

		primaryStage.show();
	}

	public void buildTheWorld(GraphicsContext gc) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				creatureMap[i][j] = null;
			}
		}
		for (COLOR c : COLOR.values()) {
			calabrothers[c.ordinal()] = new Calabash(c, creatureMap);
			// creatureMap[c.ordinal()][0] = calabrothers[c.ordinal()];
			calabrothers[c.ordinal()].setMap(c.ordinal(), c.ordinal() % 2);
			goodCreature.add(calabrothers[c.ordinal()]);
			creaturethds.add(new CreatureThread("calabash" + c.toString(), calabrothers[c.ordinal()], gc));
		}
		// creatureMap[ROWS - 1][0] = oldman;
		oldman.setMap(ROWS - 1, 0);
		goodCreature.add(oldman);
		creaturethds.add(new CreatureThread("oldman", oldman, gc));

		int midscorpion = 3;
		int midscorpionCol = COLS - 4;
		scorpion.add(new Scorpion(false, creatureMap));
		scorpion.get(0).setMap(midscorpion, midscorpionCol);
		for (int i = 1, j = 1; i <= 3; i++, j = j + 2) {
			scorpion.add((Scorpion) new Scorpion(true, creatureMap));
			scorpion.add((Scorpion) new Scorpion(true, creatureMap));
			scorpion.get(j).setMap(midscorpion - i, midscorpionCol + i);
			scorpion.get(j + 1).setMap(midscorpion + i, midscorpionCol + i);
		}
		for (int i = 0; i < scorpion.size(); i++) {
			badCreature.add(scorpion.get(i));
			creaturethds.add(new CreatureThread("scorpion" + i, scorpion.get(i), gc));
		}
		// creatureMap[ROWS - 1][COLS - 1] = serpent;
		serpent.setMap(ROWS - 1, COLS - 1);
		creaturethds.add(new CreatureThread("serpent", serpent, gc));
		badCreature.add(serpent);
		starthds = false;
	}

	public boolean gameOver(List<Creature> creatureset) {
		for (int i = 0; i < creatureset.size(); i++) {
			if (creatureset.get(i).getStatus())
				return false;
		}
		return true;
	}

}
