package distributed;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import game.Slayer;

public class Client {

	private ObjectInputStream in;
	private PrintWriter out;
	private Socket socket;


	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Client clt = new Client();
		clt.connectToServer();
		clt.communicateWithServer();
	}

	// server related
	public void connectToServer() throws IOException {
		InetAddress addr = InetAddress.getByName(null);
		socket = new Socket(addr, Server.PORT);
		System.out.println("connecting to address: " + addr + " on port: " + Server.PORT);
		in = new ObjectInputStream(socket.getInputStream());
		System.out.println("in done");
		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())),
				true);
		System.out.println("out done");
	}

	public void communicateWithServer() throws IOException, ClassNotFoundException {
		System.out.println("sent request to server");
		ServerResponse resp = ((ServerResponse) in.readObject());
		System.out.println("assigned id by server: " + resp.getId());
	}

	private Slayer slayer;

	public Slayer getSlayer() {
		return slayer;
	}

	public void setSlayer(Slayer slayer) {
		this.slayer = slayer;
	}

}
