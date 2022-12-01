package game;

import java.util.Observable;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import environment.Cell;
import environment.Coordinate;

public class Game extends Observable {

	public static final int DIMY = 5;
	public static final int DIMX = 5;
	private static final int NUM_PLAYERS = 10;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME = 3;

	public static final long REFRESH_INTERVAL = 400;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 2000;
	

	private Lock lock = new ReentrantLock();
	private Condition available = lock.newCondition();

	protected Cell[][] board;

	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY];

		for (int x = 0; x < Game.DIMX; x++)
			for (int y = 0; y < Game.DIMY; y++)
				board[x][y] = new Cell(new Coordinate(x, y), this);
	}

	/**
	 * @param player
	 * @throws InterruptedException
	 */
	public void addPlayerToGame(Contestant player) throws InterruptedException {
		Cell initialPos = getRandomCell();
		System.out.println("spawning player: " + player.getIdentification());

		//lock.lock();
		initialPos.spawnPlayer(player);
		//lock.unlock();

		// To update GUI
		//notifyChange();
	}

	public void createThreads(int num) {
		Random rn = new Random();
		for (int i = 1; i <= num; i++)
			// new SlayerThread(i,this, (byte)(rn.nextInt(3 - 1 + 1) + 1)).start();
			new Thread(new Daemon(i, this, (byte) (rn.nextInt(3 - 1 + 1) + 1))).start();
	}

	// /**
	// * returns cell occupied by player, if the player is not occupying any cell
	// returns null
	// * @param player
	// * @return Cell | null
	// */
	// public Cell getCellByPlayer(Player player) {
	// //linear search. For now, at least ...
	// for (int i = 0; i < DIMX; i++) {
	// for (int j = 0; j < DIMY; j++) {
	// Player occupying = board[i][j].getPlayer();
	// if (occupying != null && occupying.equals(player)) {
	// return board[i][j];
	// }
	// }
	// }
	// return null;
	// }

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

	/**
	 * Updates GUI. Should be called anytime the game state changes
	 */
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

}
