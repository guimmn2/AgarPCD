package game;

import environment.Cell;
import environment.Direction;

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

	public void moveKeys(Direction dir) {
		if(isAlive() && !isObstacle()) {
			Cell nextCell = game.getCell(getCurrentCell().getPosition().translate(dir.getVector()));
			if (nextCell == null || (nextCell.getPlayer() != null && nextCell.getPlayer().isObstacle()))
				return;
			nextCell.movePlayer(this);
		}
	}

	@Override
	public void movement() {
	}

	@Override
	public void run() {
	}
}
