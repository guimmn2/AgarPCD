package environment;

import game.Game;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.Contestant;

public class Cell {
	private Coordinate position;
	private Game game;
	private Contestant player=null;

	private Lock lock=new ReentrantLock();
	private Condition available=lock.newCondition();

	public Cell(Coordinate position,Game g) {
		super();
		this.position = position;
		this.game=g;
	}

	public Coordinate getPosition() {
		return position;
	}

	public boolean isOcupied() {
		return player!=null;
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
	
	/**
	 * used for setting players positions on init
	 * @param player
	 * @throws InterruptedException
	 */
	// Should not be used like this in the initial state: cell might be occupied, must coordinate this operation
	public void setPlayer(Contestant player) throws InterruptedException {

		//		lock.lock();
		//		try {
		//
		//			if(player != null) {
		//				if(player.getCurrentCell()!=null) {
		//				System.out.println(player.getIdentification()+ " " + player.getCurrentCell().getPosition().toString());
		//				}else {
		//					System.out.println(player.getIdentification());
		//
		//				}
		//				while(isOcupied()) {
		//					System.out.println("Concurrence Ocurred!\n[Pos: "+getPosition().toString()+"| Occupyed by: "+getPlayer().getIdentification()+"| Wants to occupy: "+ player.getIdentification()+"]");
		//					available.await();
		//				}
		//				this.player=player;
		//				player.setCurrentCell(this);
		//			}else { 
		//				
		//				this.player = null;
		//				System.out.println("avisa aí os puto");
		//				available.signalAll();
		//			}
		//		}finally {
		//			System.out.println("Sai wi"); 
		//			lock.unlock();}
		//		
		//	}
		lock.lock();
		try {
			while(isOcupied()) {
				System.out.println("Concurrence Ocurred!\n[Pos: "+getPosition().toString()+"| Occupyed by: "+getPlayer().getIdentification()+"| Wants to occupy: "+ player.getIdentification()+"]");
				available.await();
			}
			this.player=player;
			player.setCurrentCell(this);

		}finally{lock.unlock();};
	}

	/**
	 * moves player to this cell,
	 * immediately if cell not occupied,
	 * if cell is occupied by a player that is alive and whose strength is inferior to 10 triggers a fight
	 * STILL NOT IMPLEMENTED: if cell occupied by a dead player (obstacle) should sleep for 2 seconds or smthing, i dunno..
	 * @param player
	 */
	public synchronized void movePlayer(Contestant player) {
		if (!isOcupied()) {
			player.getCurrentCell().leave();
			this.player = player;
			player.setCurrentCell(this);
		} 
		else if (hasObstacle()) {
			System.out.println("player " + player.getIdentification() + " escolheu cell com obstáculo");
		}
		else {
			Contestant occupying = getPlayer();
			if (occupying.isAlive() && occupying.getCurrentStrength() < 10)
				fight(player, occupying);
		}
		game.notifyChange();
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
