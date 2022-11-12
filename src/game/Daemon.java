package game;

public class Daemon extends Player {

	public Daemon(int id, Game game) {
		super(id, game);
	}

	//TODO
	public void move() {
	}

	@Override
	public boolean isHumanPlayer() {
		//temporarily true
		return true;
	}

}
