package distributed;

import java.io.Serializable;

public class ServerResponse implements Serializable {

	private int id;
	// Slayer
	// Board
	
	public ServerResponse(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
