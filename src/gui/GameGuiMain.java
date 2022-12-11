package gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import game.Game;

public class GameGuiMain implements Observer {
	private JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private Game game;

	public GameGuiMain(Game game) {
		super();
		this.game = game;
		game.addObserver(this);

		buildGui();

	}

	private void buildGui() {
		boardGui = new BoardJComponent(game, false);
		frame.add(boardGui);


		frame.setSize(800,800);
		frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init()  {
		frame.setVisible(true);

		// Demo players, should be deleted
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		game.createThreads();
	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public Game getGame() {
		return game;
	}

}
