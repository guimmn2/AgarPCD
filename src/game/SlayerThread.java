package game;

public class SlayerThread extends Thread {
	private Slayer slayer;
	private int SlayerThreadID;
	private Game game;
	
	public SlayerThread(int id, Game game, byte strength) {
		SlayerThreadID = id;
		this.game = game; 
		slayer = new Slayer(id, game, strength);
	}

	@Override
	public void run() {
			try {
				game.addPlayerToGame(slayer);
				sleep(Game.INITIAL_WAITING_TIME);
				slayer.movement();
			} catch (InterruptedException e) {}
	}
}