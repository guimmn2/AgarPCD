package game;

public class PlayerThread extends Thread {
	private Slayer slayer;
	private int PlayerThreadID;
	private Game game;
	
	public PlayerThread(int id, Game game, byte strength) {
		PlayerThreadID = id;
		this.game = game; 
		slayer = new Slayer(id, game, strength);
	}

	@Override
	public void run() {
			try {
				game.addPlayerToGame(slayer);
			} catch (InterruptedException e) {}
	}
}