package distributed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import game.Slayer;

public class Client {

//	private ObjectInputStream in;
//	private ObjectOutputStream out;
	private Socket socket;

	private BufferedReader in;
	private PrintWriter out;

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
//		in = new ObjectInputStream(socket.getInputStream());
//		out = new ObjectOutputStream(socket.getOutputStream());
		in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())),
				true);
	}

	public void communicateWithServer() throws IOException, ClassNotFoundException {
//		ClientRequest req = new ClientRequest(null);
//		out.writeObject(req);
//		System.out.println("sent request to server");
//		ServerResponse resp = ((ServerResponse) in.readObject());
		String resp = in.readLine();
//		System.out.println("assigned id by server: " + resp.getId());
		System.out.println("assigned id by server: " + resp);
	}

	private Slayer slayer;

	public Slayer getSlayer() {
		return slayer;
	}

	public void setSlayer(Slayer slayer) {
		this.slayer = slayer;
	}

}
