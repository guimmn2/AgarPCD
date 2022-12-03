package environment;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.Contestant;
import game.Daemon;
import game.ThreadSlapper;
import game.Game;

public class Cell {
	private Coordinate position;
	private Game game;
	private Contestant player = null;

	private Lock lock = new ReentrantLock();
	private Condition available = lock.newCondition();

	public Cell(Coordinate position, Game g) {
		super();
		this.position = position;
		this.game = g;
	}

	public Coordinate getPosition() {
		return position;
	}
	
	public boolean isEmpty() {
		return player == null;
	}

	public boolean hasLivingPlayer() {
		return (!isEmpty() && player.isAlive());
	}

	public boolean hasObstacle() {
		return (!isEmpty() && player.isObstacle());
	}

	public Contestant getPlayer() {
		return player;
	}
	
	private void setPlayer(Contestant player) {
		this.player = player;
	}

	public void leave() {
		lock.lock();
		try {
			player = null;
			available.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public ReentrantLock getLock() {
		return (ReentrantLock) lock;
	}

	/**
	 * used for setting players positions on init
	 * 
	 * @param player
	 * @throws InterruptedException
	 */
	public void spawnPlayer(Contestant player) {
		lock.lock();
		try {
			// TODO
			// if a player is put in a place where lies an obstacle. Unlikely, but possible
			// gets same treatment as Daemon that chooses Cell with obstacle
			while (hasLivingPlayer()) {
				System.out.println("Concurrence Ocurred!\n[Pos: " + getPosition() + "| Occupied by: " + getPlayer().getIdentification() + " | Player: " + player.getIdentification()  +" wants to occupy ]");
				available.await();
			}
			System.out.println("spot vacated, spawning player: " + player.getIdentification());
			this.player = player;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			game.notifyChange();
			lock.unlock();
		}
	}

	/**
	 * moves player to this cell, immediately if cell not occupied, if cell is
	 * occupied by a player that is alive and whose strength is inferior to 10
	 * triggers a fight (obstacle) should sleep for 2 seconds or smthing, i dunno..
	 * 
	 * @param player
	 */
	public synchronized void movePlayer(Contestant player) {
		ReentrantLock movingPlayerCellLock = player.getCurrentCell().getLock();
		movingPlayerCellLock.lock();
		lock.lock();
		try {
			while (hasObstacle() && player instanceof Daemon) {
				System.out.println("bot tried to move to: " + getPosition() + ", has obstacle");
				new ThreadSlapper(Thread.currentThread()).start();
				wait();
			}
			if (!hasLivingPlayer()) {
				player.getCurrentCell().leave();
				setPlayer(player);
			} else {
				Contestant occupying = getPlayer();
				if (occupying.isAlive() && occupying.getCurrentStrength() < 10)
					fight(player, occupying);
			}

		} catch (InterruptedException e) {
			System.out.println("bot: " + player.getIdentification() + " interrupted after 2 seconds");
		} finally {
			game.notifyChange();
			lock.unlock();
			movingPlayerCellLock.unlock();
		}
	}

	private void fight(Contestant one, Contestant two) {
		System.out.println("player one: " + one.getIdentification() + " fighting with player two: " + two.getIdentification());
		if (one.getCurrentStrength() > two.getCurrentStrength()) {
			one.increaseStrengthBy(two.getCurrentStrength());
			two.increaseStrengthBy((byte) -two.getCurrentStrength());
		} else {
			two.increaseStrengthBy(two.getCurrentStrength());
			one.increaseStrengthBy((byte) -one.getCurrentStrength());
		}

	}
}
