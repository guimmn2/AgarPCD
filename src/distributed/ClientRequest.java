package distributed;

import java.io.Serializable;

public class ClientRequest implements Serializable {
	private int id;
	private int keyCode;

	public ClientRequest(int id, int keyCode) {
		super();
		this.id = id;
		this.keyCode = keyCode;
	}

	public int getId() {
		return id;
	}

	public int getKeyCode() {
		return keyCode;
	}
}
