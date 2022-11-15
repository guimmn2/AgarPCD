package game;

import gui.GameGuiMain;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class Slayer extends Contestant {
	public Slayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public boolean isHumanPlayer() {
		return true;
	}

	@Override
	public void movement() {
			
	}
}
