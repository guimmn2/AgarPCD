package game;

public class DaemonThread extends Thread {

	private Daemon daemon;
	private int DaemonThreadID;
	private Game game;

	public DaemonThread(int id, Game game, byte strength) {
		DaemonThreadID = id;
		this.game = game; 
		daemon = new Daemon(id, game, strength);
	}

	@Override
	public void run() {
		try {
			game.addPlayerToGame(daemon);
			sleep(Game.INITIAL_WAITING_TIME);
			daemon.move();
		} catch (InterruptedException e) {}
	}
}
