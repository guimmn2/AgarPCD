package distributed;

import game.Game;
import gui.GameGuiMain;
import gui.ClientGUI;

public class GUIThread implements Runnable {

	private GameGuiMain servGUI;
	private Game game;
	
	public GUIThread(Game game) {
		this.game = game;
		servGUI = new GameGuiMain(game);
	}
	
	public void run() {
		System.out.println("Entrei no gameinit");
		servGUI.init();
		game.createThreads();
	}
}
