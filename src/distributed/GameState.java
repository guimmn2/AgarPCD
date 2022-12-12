package distributed;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<PlayerDetails> playerDetailsList;
	
	public GameState(List<PlayerDetails> playerDetailsList) {
		this.playerDetailsList = playerDetailsList;
	}
	
	public List<PlayerDetails> getPlayerDetailsList(){
		return playerDetailsList;
	}
}
