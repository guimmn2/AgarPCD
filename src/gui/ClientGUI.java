package gui;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import distributed.PlayerDetails;
import environment.Cell;
import environment.Direction;
import game.Contestant;
import game.Daemon;
import game.Game;
import game.Slayer;

public class ClientGUI implements Observer{
	private JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private Game game;
	private boolean alternativeKeys;

	public ClientGUI(boolean alternativeKeys) {
		super();
		this.alternativeKeys = alternativeKeys;
		game = new Game();
		game.addObserver(this);
		
		buildGui();
	}

	private void buildGui() {
		boardGui = new BoardJComponent(game, alternativeKeys);
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
	}
	
	@Override
	public void update(Observable o, Object arg) {
//		System.out.println("REPAINT CLIENT GUI");
		boardGui.repaint();
	}
	
	public Game getGame() {
		return game;
	}
	
	public BoardJComponent getBoard() {
		return boardGui;
	}
	
	public Direction getLastDir() {
		Direction dir = boardGui.getLastPressedDirection();
		return dir;
	}
}
