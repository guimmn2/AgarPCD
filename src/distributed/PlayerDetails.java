package distributed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import environment.Coordinate;
import game.Contestant;

public class PlayerDetails implements Serializable {

	private int id;
	private byte strength;
	private Coordinate coordinate;
	private boolean isHuman;
	
	
	public PlayerDetails(int id, byte strength, Coordinate coordinate, boolean isHuman) {
		this.id = id;
		this.strength = strength;
		this.coordinate = coordinate;
		this.isHuman = isHuman;
		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public byte getStrength() {
		return strength;
	}


	public void setStrength(byte strength) {
		this.strength = strength;
	}


	public Coordinate getCoordinate() {
		return coordinate;
	}


	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}


	public boolean isHuman() {
		return isHuman;
	}


	public void setHuman(boolean isHuman) {
		this.isHuman = isHuman;
	}
	
	@Override
	public String toString() {
		return "Strength: " +getStrength() + ", Coordinate: " + getCoordinate() + " isHuman: " + isHuman();
		
	}
	
}
