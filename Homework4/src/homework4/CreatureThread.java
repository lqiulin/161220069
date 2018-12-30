package homework4;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class CreatureThread<T extends Creature> implements Runnable {
	private Thread t;
	private String threadName;
	T theCreature;
	GraphicsContext gc;
	boolean working;
	// PrintStream output;

	public CreatureThread(String name, T c, GraphicsContext gc) {
		threadName = name;
		theCreature = c;
		theCreature.setName(threadName);

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
				try {

					if (working) {
						theCreature.updateLocation(elapsedTime);
						// if (theCreature.fight())
						// output.println(theCreature.getInfoLine());
						// outfile>>theCreature.getInfoLine();
						theCreature.fight();
						// Platform.runLater(() -> {
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Exception thrown  :" + e);
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
