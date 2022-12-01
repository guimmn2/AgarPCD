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

	public boolean isOcupied() {
		return player != null;
	}

	public boolean hasObstacle() {
		return (player != null && (!player.isAlive() || player.getCurrentStrength() > 10));
	}

	public Contestant getPlayer() {
		return player;
	}

	public void leave() {
		lock.lock();
		player = null;
		available.signalAll();
		lock.unlock();
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
	public synchronized void spawnPlayer(Contestant player) {
		lock.lock();
		try {
			//TODO
			// if a player is put in a place where lies an obstacle. Unlikely, but possible
			// gets same treatment as Daemon that chooses Cell with obstacle
			while (isOcupied()) {
				System.out.println("Concurrence Ocurred!\n[Pos: " + getPosition() + "| Occupyed by: "
						+ getPlayer().getIdentification() + "| Wants to occupy: " + getPosition() + "]");
				available.await();
			}
			this.player = player;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
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
		ReentrantLock currCellLock = player.getCurrentCell().getLock();
		currCellLock.lock();
		try {
			/*
			 * if (hasObstacle()) { System.out.println("player " +
			 * player.getIdentification() + " escolheu cell com obstáculo"); }
			 */
			while (hasObstacle() && player instanceof Daemon) {
				new ThreadSlapper(Thread.currentThread()).start();
				wait();
			}

			if (!isOcupied()) {
				player.getCurrentCell().leave();
				this.player = player;
			} else {
				Contestant occupying = getPlayer();
				if (occupying.isAlive() && occupying.getCurrentStrength() < 10)
					fight(player, occupying);
			}
			game.notifyChange();

		} catch (InterruptedException e) {
			System.out.println("Daemon: " + player.getIdentification() + " interrompido após 2 segundos");
		} finally {
			currCellLock.unlock();
		}
	}

	private void fight(Contestant one, Contestant two) {
		if (one.getCurrentStrength() > two.getCurrentStrength()) {
			one.increaseStrengthBy(two.getCurrentStrength());
			two.increaseStrengthBy((byte) -two.getCurrentStrength());
		} else {
			two.increaseStrengthBy(two.getCurrentStrength());
			one.increaseStrengthBy((byte) -one.getCurrentStrength());
		}

	}
}
