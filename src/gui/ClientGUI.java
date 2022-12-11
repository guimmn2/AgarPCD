package gui;

import java.util.List;

import javax.swing.JFrame;

import environment.Cell;
import game.Contestant;
import game.Game;

public class ClientGUI{
	private JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private Game game;
	
	public ClientGUI() {
		super();
		game = new Game();
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
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setContestants(List<Contestant> contestants) {
		for(Contestant c:contestants) 
			game.addPlayerToCell(c, c.getCurrentCell().getPosition());
		boardGui.repaint();
	}
	
}
