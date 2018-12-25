package homework4;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class CreatureThread implements Runnable {
	private Thread t;
	private String threadName;
	Creature theCreature;
	GraphicsContext gc;
	boolean working;

	public CreatureThread(String name, Creature c, GraphicsContext gc) {
		threadName = name;
		theCreature = c;
		this.gc = gc;
	}

	// @SuppressWarnings("deprecation")
	public void close() {
		working = false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		System.out.println("Running " + threadName);
		LongValue lastNanoTime = new LongValue(System.nanoTime());

		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				// Clear the canvas
				// gc.clearRect(0, 0, 512,512);
				double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
				lastNanoTime.value = currentNanoTime;
				if (working) {
					theCreature.updateLocation(elapsedTime);
					theCreature.fight();
					// Platform.runLater(() -> {
				}
				// if(!theCreature.alive)
				// stop();
				// });
				theCreature.drawCreature(gc);
				// if (!theCreature.getStatus())
				// return;
				Thread.yield();
			}
		}.start();

	}

	public void start() {
		// System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			working = true;
			t.start();
		}
	}
}
