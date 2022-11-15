package game;

import environment.Cell;
import environment.Coordinate;

public class Daemon extends Contestant {

	public Daemon(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	@Override
	public boolean isHumanPlayer() {
		return false;
	}


	@Override
	public void movement() {
		Cell nextCell = game.getCell(getCurrentCell().getPosition().translate(Coordinate.randomDirection()));
		if(nextCell!=null)
			game.moveContestant(this, nextCell);
	}

}
