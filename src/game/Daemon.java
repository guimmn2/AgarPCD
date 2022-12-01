package game;

import environment.Cell;
import environment.Coordinate;

public class Daemon extends Contestant implements Runnable {

	public Daemon(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	@Override
	public boolean isHumanPlayer() {
		return false;
	}


	@Override
	public void movement() {
		//check if has spawned
		if (getCurrentCell() != null) {
			Cell nextCell = game.getCell(getCurrentCell().getPosition().translate(Coordinate.randomDirection()));
			if (nextCell != null) {
				nextCell.movePlayer(this);
			}
		}
	}

	@Override
	public void run() {
		try {
			game.addPlayerToGame(this);
			Thread.sleep(Game.INITIAL_WAITING_TIME);
			move();
		} catch (InterruptedException e) {}
	}

}
