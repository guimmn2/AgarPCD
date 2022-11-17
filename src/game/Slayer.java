package game;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class Slayer extends Contestant implements Runnable {

	public Slayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public boolean isHumanPlayer() {
		return true;
	}

	@Override
	public void movement() {
	}

	@Override
	public void run() {
			try {
				game.addPlayerToGame(this);
				Thread.sleep(Game.INITIAL_WAITING_TIME);
				movement();
			} catch (InterruptedException e) {}
	}
}
