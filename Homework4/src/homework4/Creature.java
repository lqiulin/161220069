package homework4;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Creature {
	final int PHOTOX = 80;
	final int PHOTOY = 100;
	final int ROWS = 9;
	final int COLS = 15;
	int hp;
	int attack;
	int range;
	boolean alive;
	boolean goodguy;
	boolean fighting;
	protected Image aliveImage;
	protected Image deadImage;
	protected Creature[][] creatureMap;
	protected ArrayList<String> infoItem;
	protected int mrow;
	protected int mcol;
	protected double posX;
	protected double posY;
	protected double velX;
	protected double velY;
	protected String name;

	public Creature() {
		hp = 30;
		attack = 10;
		range = 2;
		alive = true;
		fighting = false;
		posX = 0;
		posY = 0;
		velX = 0;
		velY = 0;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Image getAliveImage() {
		return aliveImage;
	}

	public Image getDeadImage() {
		return deadImage;
	}

	public boolean isGoodGuy() {
		return goodguy;
	}

	public void setMap(int row, int col) {
		creatureMap[row][col] = this;
		posX = col * PHOTOX;
		posY = row * PHOTOY;
		mrow = row;
		mcol = col;
	}

	public synchronized boolean updateLocation(double time) {
		if (!alive)
			return false;
		double tmpx = posX + velX * time;
		double tmpy = posY + velY * time;
		int tmpcol = (int) (tmpx / PHOTOX);
		if (velX > 0)
			tmpcol = tmpcol + 1;
		int tmprow = (int) (tmpy / PHOTOY);
		if (velY > 0)
			tmprow = tmprow + 1;
		if (tmprow >= ROWS || tmpcol >= COLS || tmprow < 0 || tmpcol < 0) {
			velX = 0;
			velY = 0;
			return false;
		}
		if (tmprow == mrow && tmpcol == mcol) {
			posX += velX * time;
			posY += velY * time;
			return true;
		}
		if (creatureMap[tmprow][tmpcol] == null) {
			// System.out.println("&&&&&&&&&&&");
			posX += velX * time;
			posY += velY * time;
			creatureMap[tmprow][tmpcol] = creatureMap[mrow][mcol];
			creatureMap[mrow][mcol] = null;
			mrow = tmprow;
			mcol = tmpcol;
			return true;
		} else {
			// System.out.println(mrow + "," + mcol + "." + tmprow + "," + tmpcol);
			fighting = true;
			velX = 0;
			return false;
		}
		// return false;
//		if (goodguy) {
//			if ((posX + velX * time) > mcol * PHOTOX) {
//				if (creatureMap[mrow][mcol + 1] == null) {
//					posX += velX * time;
//					posY += velY * time;
//					// System.out.println(mrow + "," + (mcol + 1) + "." + tmprow + "," + tmpcol);
//					creatureMap[mrow][mcol + 1] = creatureMap[mrow][mcol];
//					creatureMap[mrow][mcol] = null;
//					mcol = mcol + 1;
//				} else {
//					fighting = true;
//					velX = 0;
//				}
//			} else {
//				posX += velX * time;
//				posY += velY * time;
//			}
//		} else {
//			if ((posX + velX * time) < mcol * PHOTOX) {
//				if (creatureMap[mrow][mcol - 1] == null) {
//					posX += velX * time;
//					posY += velY * time;
//					System.out.println(mrow + "," + (mcol - 1) + "." + tmprow + "," + tmpcol);
//					creatureMap[mrow][mcol - 1] = creatureMap[mrow][mcol];
//					creatureMap[mrow][mcol] = null;
//					mcol = mcol - 1;
//				} else {
//					fighting = true;
//					velX = 0;
//				}
//			} else {
//				System.out.println(mrow + "," + mcol + "." + tmprow + "," + tmpcol);
//				posX += velX * time;
//				posY += velY * time;
//			}
//		}

	}

	public boolean getStatus() {
		return alive;
	}

	private Creature getTarget() {
		// for(int i=mrow;i<mrow+range;i++)
		// {

//			for (int j = mcol-range; j <= mcol + range; j++) {
//				if (creatureMap[mrow][j] != null && goodguy != creatureMap[mrow][j].isGoodGuy()) {
//					return creatureMap[mrow][j];
//				}
//			}

//			for(int i=mrow-range;i<=mrow+range;i++)
//			{
//				for(int j=mcol-range;j<=mcol+range;j++)
//				{
//					if (creatureMap[i][j] != null && goodguy != creatureMap[i][j].isGoodGuy()) {
//						return creatureMap[i][j];
//					}
//				}
//			}
		for (int i = 0; i <= range; i++) {
			int maxj = (mcol + range) < COLS ? (mcol + range) : (COLS - 1);
			for (int j = (mcol - range) >= 0 ? (mcol - range) : 0; j <= maxj; j++) {
				if (mrow + i < ROWS) {
					if (creatureMap[mrow + i][j] != null && goodguy != creatureMap[mrow + i][j].isGoodGuy()) {
						if (creatureMap[mrow + i][j].getStatus())
							return creatureMap[mrow + i][j];
					}
				}
				if (mrow - i >= 0) {
					if (creatureMap[mrow - i][j] != null && goodguy != creatureMap[mrow - i][j].isGoodGuy()) {
						if (creatureMap[mrow - i][j].getStatus())
							return creatureMap[mrow - i][j];
					}
				}
			}
		}

//			for(int i=1;i<=range;i++)
//			{
//				if (creatureMap[mrow][mcol+i] != null && goodguy != creatureMap[mrow][mcol+i].isGoodGuy()) {
//					return creatureMap[mrow][mcol+i];
//				}
//				if (creatureMap[mrow+i][mcol+i] != null && goodguy != creatureMap[mrow+1][mcol+i].isGoodGuy()) {
//					return creatureMap[mrow+i][mcol+i];
//				}
//				if (creatureMap[mrow-i][mcol+i] != null && goodguy != creatureMap[mrow-1][mcol+i].isGoodGuy()) {
//					return creatureMap[mrow-i][mcol+i];
//				}
//			}

		return null;

	}

	public synchronized boolean fight() {
		if (!alive)
			return false;
		if (!fighting)
			return false;
		Creature target = getTarget();
		if (target == null) {
			fighting = false;
			setDirection();
			return false;
		}
		attackTarget(target);
		// System.out.println(name + " attack " + target.getName());
		updateInfoItem(name + " attack " + target.getName());
		return true;
	}

	private void updateInfoItem(String line) {
		infoItem.add(line);
	}

	public void setDirection() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (creatureMap[i][j] != null && creatureMap[i][j].getStatus()) {
					if (creatureMap[i][j].isGoodGuy() != goodguy) {
						if (i > mrow)
							velY = 50;
						else if (i < mrow)
							velY = -50;
						if (j > mcol)
							velX = 50;
						else if (j < mcol)
							velX = -50;
					}
				}
			}
		}
		return;
	}

	public void attackTarget(Creature target) {
		target.receiveAttack(attack);
	}

	private void receiveAttack(int atk) {
		if (atk < hp) {
			hp = hp - atk;
		} else {
			hp = 0;
			alive = false;
		}
	}

	public void drawCreature(GraphicsContext gc) {
		if (alive)
			gc.drawImage(aliveImage, posX, posY);
		else
			gc.drawImage(deadImage, posX, posY);
	}

}
