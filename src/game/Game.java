package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import environment.Cell;
import environment.Coordinate;

public class Game extends Observable implements Serializable {

	public static final int DIMY = 10;
	public static final int DIMX = 10;
	private static final int NUM_PLAYERS = 10;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME = 3;

	public static final long REFRESH_INTERVAL = 400;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 2000;
	
	private Cell[][] board;
	private ArrayList<Contestant> contestants;
	
	private List<Thread> danoninhos = new ArrayList<>();
	public transient CountDownLatch portõesDaQuinta = new CountDownLatch(NUM_FINISHED_PLAYERS_TO_END_GAME);
	private boolean running;
	
	//private int numFinishedPlayers;

	public Game() {
		running = true;
		board = new Cell[Game.DIMX][Game.DIMY];
		contestants = new ArrayList<>();
		for (int x = 0; x < Game.DIMX; x++)
			for (int y = 0; y < Game.DIMY; y++)
				board[x][y] = new Cell(new Coordinate(x, y), this);
	}
	
//	public synchronized void incrementNumFinishedPlayers() {
//		numFinishedPlayers++;
//	}
//	
	public void decrementLatch() {
		portõesDaQuinta.countDown();
	}
	
	public boolean running() {
		return running;
	}
	
	/**
	 * @param player
	 * @throws InterruptedException
	 */
	public void addPlayerToGame(Contestant player) throws InterruptedException {
		System.out.println("ADDING PLAYER TO GAME");
		Cell initialPos = getRandomCell();
		contestants.add(player);
		initialPos.spawnPlayer(player);
	}

	public void addPlayerToCell(Contestant player, Coordinate pos) {
		getCell(pos).spawnPlayer(player);
	}
	
	public void createThreads() {
		Random rn = new Random();
		for (int i = 1; i <= NUM_PLAYERS; i++) {
			 Thread yogi = new Thread(new Daemon(i, this, (byte) (rn.nextInt(3 - 1 + 1) + 1)));
			 danoninhos.add(yogi);
			 yogi.start();
		}
		
		try {
			
			portõesDaQuinta.await();
			System.err.println("Portões da Quinta fechados!");
			running = false;
			for(Thread t: danoninhos)
				t.interrupt();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Contestant> getContestants(){
		System.out.println("FLA FLA " + contestants.size());
		return contestants;
	}

	// Returns Null if cell out of bounds!
	public Cell getCell(Coordinate at) {
		if (at.x >= DIMX || at.x < 0 || at.y >= DIMY || at.y < 0)
			return null;
		return board[at.x][at.y];
	}

	public Cell getCell(Contestant player) {
		for (int x = 0; x < Game.DIMX; x++) {
			for (int y = 0; y < Game.DIMY; y++) {
				Contestant match = board[x][y].getPlayer();
				if (match != null && match.equals(player)) {
					return board[x][y];
				}
			}
		}
		return null;
	}

	public Cell getRandomCell() {
		Cell newCell = getCell(new Coordinate((int) (Math.random() * Game.DIMX), (int) (Math.random() * Game.DIMY)));
		return newCell;
	}

	// for testing purposes
	public synchronized Cell getOccupiedCell() {
		for (int x = 0; x < Game.DIMX; x++) {
			for (int y = 0; y < Game.DIMY; y++) {
				if (board[x][y].hasObstacle())
					return board[x][y];
			}
		}
		return null;
	}

	/**
	 * Updates GUI. Should be called anytime the game state changes
	 */
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

}
