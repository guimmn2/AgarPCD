package game;

import java.util.Observable;
import java.util.Random;

import environment.Cell;
import environment.Coordinate;

public class Game extends Observable {

	public static final int DIMY = 10;
	public static final int DIMX = 10;
	private static final int NUM_PLAYERS = 40;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME = 3;

	public static final long REFRESH_INTERVAL = 400;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 2000;
	
	protected Cell[][] board;
	private int numFinishedPlayers;

	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY];

		for (int x = 0; x < Game.DIMX; x++)
			for (int y = 0; y < Game.DIMY; y++)
				board[x][y] = new Cell(new Coordinate(x, y), this);
	}
	
	public synchronized void incrementNumFinishedPlayers() {
		numFinishedPlayers++;
	}
	
	public boolean running() {
		return numFinishedPlayers != NUM_FINISHED_PLAYERS_TO_END_GAME;
	}
	
	/**
	 * @param player
	 * @throws InterruptedException
	 */
	public void addPlayerToGame(Contestant player) throws InterruptedException {
		Cell initialPos = getRandomCell();
		initialPos.spawnPlayer(player);
	}

	public void createThreads() {
		Random rn = new Random();
		for (int i = 1; i <= NUM_PLAYERS; i++) {
			new Thread(new Daemon(i, this, (byte) (rn.nextInt(3 - 1 + 1) + 1))).start();
		}
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
