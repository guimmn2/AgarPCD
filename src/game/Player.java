package game;



import environment.Cell;

/**
 * Represents a player.
 * @author luismota
 * 
 * Parent class for Daemon (bot) and Slayer (human)
 *
 */
public abstract class Player  {


	protected  Game game;

	private int id;

	private byte currentStrength;
	protected byte originalStrength;

	// TODO: get player position from data in game
	public Cell getCurrentCell() {
		return game.getCellByPlayer(this);
	}
	
	public Player(int id, Game game) {
		this.id = id;
		this.game = game;
		currentStrength = (byte) (Math.random() * 3);
		originalStrength = currentStrength;
	}

	public Player(int id, Game game, byte strength) {
		super();
		this.id = id;
		this.game=game;
		currentStrength=strength;
		originalStrength=strength;
	}

	public abstract boolean isHumanPlayer();
	
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
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public boolean isAlive() {
		return currentStrength > 0;
	}

	public byte getCurrentStrength() {
		return currentStrength;
	}
	
	public void increaseStrengthBy(byte strength) {
		currentStrength += strength;
	}
	
	public int getIdentification() {
		return id;
	}
}
