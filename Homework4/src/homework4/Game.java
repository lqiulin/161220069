package homework4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Game extends Application {
	final int ROWS = 9;
	final int COLS = 15;
	final int PHOTOX = 80;
	final int PHOTOY = 100;
	private Creature[][] creatureMap = new Creature[ROWS][COLS];
	private ArrayList<String> infoItem = new ArrayList<String>();
	private Oldman oldman = new Oldman(creatureMap, infoItem);
	private Calabash[] calabrothers = new Calabash[7];
	private Serpent serpent = new Serpent(creatureMap, infoItem);
	private List<Scorpion> scorpion = new ArrayList<Scorpion>();
	private List<Creature> goodCreature = new ArrayList<Creature>();
	private List<Creature> badCreature = new ArrayList<Creature>();
	private List<CreatureThread> creaturethds = new ArrayList<CreatureThread>();
	boolean starthds;
	boolean runninggame;
	boolean gameover;
	String attacker;
	String receiver;
	InputStream infile;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	public void startTheGame() {
		launch();
	}

	public void writeToFile() {
		String filepath = "test.txt";
		try {
			File file = new File(filepath);
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			// ps.println("http://www.jb51.net");// 往文件里写入字符串
			// ps.append("http://www.jb51.net");// 在已有的基础上添加字符串
			for (int i = 0; i < infoItem.size(); i++)
				ps.println(infoItem.get(i));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readTheFile(String filepath) throws IOException {
		InputStream is = new FileInputStream(filepath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = reader.readLine();
		infoItem.clear();
		while (line != null) { // 如果 line 为空说明读完了
			infoItem.add(line);
//			StringTokenizer st = new StringTokenizer(line, " ");
//			while (st.hasMoreElements()) {
//				System.out.println(st.nextElement() + ",");
//			}
			line = reader.readLine(); // 读取下一行
		}
	}

	public void runTheGame(GraphicsContext gc, Image background) {
		if (gameover)
			return;
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
			writeToFile();
			// System.out.println("write");
			starthds = false;
			gameover = true;
			gc.fillText("YOU WIN!", 60, 100);
			gc.strokeText("YOU WIN", 60, 100);
		} else if (gameOver(goodCreature)) {
			for (int i = 0; i < creaturethds.size(); i++) {
				creaturethds.get(i).close();
			}
			writeToFile();
			// System.out.println("write");
			gameover = true;
			starthds = false;
			gc.fillText("GAME OVER!", 60, 100);
			gc.strokeText("GAME OVER!", 60, 100);
		}
	}

	public void playbackTheGame(GraphicsContext gc, Image background, double elapsedTime) {

		gc.clearRect(0, 0, PHOTOX * (COLS + 1), PHOTOY * (ROWS + 1));
		gc.drawImage(background, 0, 0);
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (creatureMap[i][j] != null) {
					if (!creatureMap[i][j].updateLocation(elapsedTime)) {

						for (int k = 0; k < infoItem.size(); k++) {
							StringTokenizer st = new StringTokenizer(infoItem.get(k), " ");
							String ac = (String) st.nextElement();
							String cc = creatureMap[i][j].getName();
							// System.out.println(ac + "*" + creatureMap[i][j].getName() + "*");
							if (ac.equals(cc)) {
								// System.out.println("*******");
								st.nextElement();
								ac = (String) st.nextElement();
								Creature x = findCreatureByName(ac);
								if (x != null) {
									System.out.println(cc + " attack " + ac);
									creatureMap[i][j].attackTarget(x);
									infoItem.remove(k);
									k--;
								}
							}
						}
						creatureMap[i][j].setDirection();
					}

				}
			}
		}

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (creatureMap[i][j] != null)
					creatureMap[i][j].drawCreature(gc);
			}
		}

		if (gameOver(badCreature)) {
			for (int i = 0; i < creaturethds.size(); i++) {
				creaturethds.get(i).close();
			}
			gameover = true;
			runninggame = false;
			gc.fillText("YOU WIN!", 60, 100);
			gc.strokeText("YOU WIN", 60, 100);
		} else if (gameOver(goodCreature)) {
			for (int i = 0; i < creaturethds.size(); i++) {
				creaturethds.get(i).close();
			}
			gameover = true;
			runninggame = false;
			gc.fillText("GAME OVER!", 60, 100);
			gc.strokeText("GAME OVER!", 60, 100);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		// primaryStage.setTitle("Collect the Money Bags!");

		Group root = new Group();
		Scene theScene = new Scene(root);
		primaryStage.setScene(theScene);

		Canvas canvas = new Canvas(PHOTOX * (COLS + 1), PHOTOY * (ROWS + 1));
		System.out.println(PHOTOX * (COLS + 1) + "," + PHOTOY * (ROWS + 1));
		root.getChildren().add(canvas);

		GraphicsContext gc = canvas.getGraphicsContext2D();
		buildTheWorld(gc);

		// gc.drawImage( image, positionX, positionY );
		Image background = new Image(getClass().getResourceAsStream("background.png"));
		gc.drawImage(background, 0, 0);
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

		// LongValue lastNanoTime = new LongValue(System.nanoTime());

		gc.setFill(Color.RED);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2);
		Font theFont = Font.font("Times New Roman", FontWeight.BOLD, 60);
		gc.setFont(theFont);

		LongValue lastNanoTime = new LongValue(System.nanoTime());
		;
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				// Clear the canvas
				// gc.clearRect(0, 0, 512,512);

				if (input.contains("SPACE")) {
					if (!starthds) {
						starthds = true;
						System.out.println("start");
						for (int i = 0; i < creaturethds.size(); i++) {
							creaturethds.get(i).start();
						}
						input.clear();
					}
				}

				if (input.contains("L")) {
					if (!runninggame) {
						FileChooser fileChooser = new FileChooser();
						File file = fileChooser.showOpenDialog(primaryStage);
						String path = file.getPath();
						try {
							readTheFile(path);
							// runninggame = true;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							lastNanoTime.value = currentNanoTime;
							runninggame = true;
						}
						input.clear();
//						try {
//							infile = new FileInputStream(file);
//						} catch (FileNotFoundException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}

					}

				}

				if (starthds)
					runTheGame(gc, background);
				else if (runninggame) {
					double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
					lastNanoTime.value = currentNanoTime;
					// System.out.println(elapsedTime);
					playbackTheGame(gc, background, 0.015);
				}
//				gc.clearRect(0, 0, PHOTOX * (COLS + 1), PHOTOY * (ROWS + 1));
//				gc.drawImage(background, 0, 0);
//
//				if (!starthds) {
//					for (int i = 0; i < ROWS; i++) {
//						for (int j = 0; j < COLS; j++) {
//							if (creatureMap[i][j] != null)
//								creatureMap[i][j].drawCreature(gc);
//						}
//					}
//				}
//				if (gameOver(badCreature)) {
//					for (int i = 0; i < creaturethds.size(); i++) {
//						creaturethds.get(i).close();
//
//					}
//					gc.fillText("YOU WIN!", 60, 100);
//					gc.strokeText("YOU WIN", 60, 100);
//				} else if (gameOver(goodCreature)) {
//					for (int i = 0; i < creaturethds.size(); i++) {
//						creaturethds.get(i).close();
//					}
//					gc.fillText("GAME OVER!", 60, 100);
//					gc.strokeText("GAME OVER!", 60, 100);
//				}
			}
		}.start();

		primaryStage.show();
	}

	public void buildTheWorld(GraphicsContext gc) {
		goodCreature.clear();
		badCreature.clear();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				creatureMap[i][j] = null;
			}
		}
//		try {
//			OutputStream outfile = new FileOutputStream("test.txt");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		File outfile = new File("output.txt");
//		PrintStream ps;
//		try {
//			ps = new PrintStream(new FileOutputStream(outfile));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		for (COLOR c : COLOR.values()) {
			calabrothers[c.ordinal()] = new Calabash(c, creatureMap, infoItem);
			// creatureMap[c.ordinal()][0] = calabrothers[c.ordinal()];
			calabrothers[c.ordinal()].setMap(c.ordinal(), c.ordinal() % 2);
			goodCreature.add(calabrothers[c.ordinal()]);
			creaturethds.add(new CreatureThread<Calabash>("calabash" + c.toString(), calabrothers[c.ordinal()], gc));
		}
		// creatureMap[ROWS - 1][0] = oldman;
		oldman.setMap(ROWS - 1, 0);
		goodCreature.add(oldman);
		creaturethds.add(new CreatureThread<Oldman>("oldman", oldman, gc));

		int midscorpion = 3;
		int midscorpionCol = COLS - 4;
		scorpion.add(new Scorpion(false, creatureMap, infoItem));
		scorpion.get(0).setMap(midscorpion, midscorpionCol);
		for (int i = 1, j = 1; i <= 3; i++, j = j + 2) {
			scorpion.add((Scorpion) new Scorpion(true, creatureMap, infoItem));
			scorpion.add((Scorpion) new Scorpion(true, creatureMap, infoItem));
			scorpion.get(j).setMap(midscorpion - i, midscorpionCol + i);
			scorpion.get(j + 1).setMap(midscorpion + i, midscorpionCol + i);
		}
		for (int i = 0; i < scorpion.size(); i++) {
			badCreature.add(scorpion.get(i));
			creaturethds.add(new CreatureThread<Scorpion>("scorpion" + i, scorpion.get(i), gc));
		}
		// creatureMap[ROWS - 1][COLS - 1] = serpent;
		serpent.setMap(ROWS - 1, COLS - 1);
		creaturethds.add(new CreatureThread<Serpent>("serpent", serpent, gc));
		badCreature.add(serpent);
		starthds = false;
	}

	public Creature findCreatureByName(String name) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (creatureMap[i][j] != null)
					if (creatureMap[i][j].getName().equals(name))
						return creatureMap[i][j];
			}
		}
		return null;
	}

	public boolean gameOver(List<Creature> creatureset) {
		for (int i = 0; i < creatureset.size(); i++) {
			if (creatureset.get(i).getStatus())
				return false;
		}
		return true;
	}

}
