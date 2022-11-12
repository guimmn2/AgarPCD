package game;

public class DaemonThread extends Thread {
	
	private Daemon daemon;
	
	public DaemonThread(Daemon daemon) {
		this.daemon = daemon;
	}
	
	public long getId() {
		return daemon.getIdentification();
	}

	@Override
	public void run() {
			try {
				daemon.game.addPlayerToGame(daemon);
				System.out.println(daemon.getCurrentCell().getPosition().toString());
			} catch (InterruptedException e) {}
	}

}
