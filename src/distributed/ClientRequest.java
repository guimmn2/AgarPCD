package distributed;

import java.io.Serializable;

public class ClientRequest implements Serializable {
	
	private Integer id;
	//private Something input;
	
	public ClientRequest(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

}
