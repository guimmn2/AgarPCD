package distributed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import game.Contestant;

public class ServerResponse implements Serializable {

	//private int id;
	// SlayerS
	ArrayList<Contestant> contestants;
	
	public ServerResponse(ArrayList<Contestant> contestants) {
		this.contestants = contestants;
//		System.out.println("AAAAAAAAAAAAA");
//		contestants.forEach(o->{System.out.println(o);});
		
	}

	public ArrayList<Contestant> getContestants(){
		return contestants;
	}

}
