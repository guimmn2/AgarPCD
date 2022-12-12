package game;



import java.io.Serializable;

import environment.Cell;

/**
 * Represents a player.
 * @author luismota
 * 
 * Parent class for Daemon (bot) and Slayer (human)
 *
 */
public abstract class Contestant  implements Serializable{


	protected  Game game;

	private int id;

	private byte currentStrength;
	protected byte originalStrength;

	public Cell getCurrentCell() {
		return game.getCell(this);
	}
	
	public Contestant(int id, Game game) {
		this.id = id;
		this.game = game;
		currentStrength = (byte) (Math.random() * 3);
		originalStrength = currentStrength;
	}

	public Contestant(int id, Game game, byte strength) {
		super();
		this.id = id;
		this.game=game;
		currentStrength=strength;
		originalStrength=strength;
	}
	
	public abstract boolean isHumanPlayer();
	public abstract void movement();
	
	public void move() {
		try {
			while(isAlive() && currentStrength < 10  && game.running()) {
				movement();
				Thread.sleep(originalStrength*Game.REFRESH_INTERVAL);
			}
			if (currentStrength == 10) {
				game.decrementLatch();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println("Thread Interrupted on Move!");
			return;
		}
	}
	
	public boolean isObstacle() {
		return (!isAlive() || currentStrength == 10);
	}
	
	public boolean isAlive() {
		return currentStrength > 0;
	}
	
	public void kill() {
		this.currentStrength = 0;
	}

	public byte getCurrentStrength() {
		return currentStrength;
	}
	
	public void increaseStrengthBy(byte strength) {
		currentStrength += strength;
		if(currentStrength > 10)
			currentStrength = 10;
	}
	
	public int getIdentification() {
		return id;
	}
	
	@Override
	public String toString() {
		return "Player [id=" + id + ", currentStrength=" + currentStrength + ", getCurrentCell()=" + getCurrentCell()
		+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contestant other = (Contestant) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
