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


	public Contestant getPlayer() {
		return player;
	}
	
	public void leave() {
		lock.lock();
		player = null;
		available.signalAll();
		lock.unlock();
	}
	
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



}
