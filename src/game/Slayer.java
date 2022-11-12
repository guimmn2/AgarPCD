package game;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class Slayer extends Player {
	public Slayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public boolean isHumanPlayer() {
		return true;
	}
}
