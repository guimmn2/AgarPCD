package game;


import java.util.Observable;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import environment.Cell;
import environment.Coordinate;

public class Game extends Observable {

	public static final int DIMY = 30;
	public static final int DIMX = 30;
	private static final int NUM_PLAYERS = 90;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME=3;

	public static final long REFRESH_INTERVAL = 400;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 10000;
	
	private Lock lock=new ReentrantLock();
	private Condition available=lock.newCondition();

	protected Cell[][] board;

	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY];
	
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(new Coordinate(x, y),this);
	}
	
	/** 
	 * @param player 
	 * @throws InterruptedException 
	 */
	public void addPlayerToGame(Player player) throws InterruptedException {
		Cell initialPos=getRandomCell();
		lock.lock();
		
		try {
			while(initialPos.isOcupied()) {
				System.out.println("Concurrence Ocurred!\n[Pos: "+initialPos.getPosition().toString()+"| Occupyed by: "+initialPos.getPlayer().getIdentification()+"| Wants to occupy: "+ player.getIdentification()+"]");
				available.await();
			}
			initialPos.setPlayer(player);
		}finally {lock.unlock();}
		// To update GUI
		notifyChange();
		
	}
	
	public void createThreads(int num) {
		Random rn = new Random();
		for(int i=1; i<=num; i++)
			new PlayerThread(i,this, (byte)(rn.nextInt(3 - 1 + 1) + 1)).start();
	}

	public Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}

	/**	
	 * Updates GUI. Should be called anytime the game state changes
	 */
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

	public Cell getRandomCell() {
		Cell newCell=getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
		return newCell; 
	}
}
