package game;

import environment.Cell;

public class VacantSpotSeeker implements Runnable {
	
	private Thread thread;
	private Contestant player;
	private Cell[][] board;

	public VacantSpotSeeker(Thread thread, Contestant player, Cell[][] board) {
		this.thread = thread;
		this.player = player;
		this.board = board;
	}
	
	private Cell seek() {
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[0].length; y++) {
				if (!board[x][y].hasObstacle()) {
					return board[x][y];
				}
			}
		}
		return null;
	}

	@Override
	public void run() {
		Cell vacant = seek();
		if (vacant != null) {
			
		}
	}

}
