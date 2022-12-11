package distributed;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import game.Contestant;
import game.Game;
import game.Slayer;
import gui.ClientGUI;

public class Client {

	private ObjectInputStream in;
	private PrintWriter out;
	private Socket conSocket;
	private String hostName;

	private ClientGUI clientGUI;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Client clt = new Client("localhost");
		clt.run();
	}

	public Client(String hostName) {
		this.hostName = hostName;
	}

	public void run() {
		connectToServer();
		communicateWithServer();

	}
	// server related
	public void connectToServer() {
		try {
			InetAddress addr = InetAddress.getByName(null);
			conSocket = new Socket(addr, Server.PORT);
			System.out.println("connecting to address: " + addr + " on port: " + Server.PORT);
		}catch(IOException e) {
			System.err.println("Error connecting to server.");
			System.exit(1);
		}

		try {
			in = new ObjectInputStream(conSocket.getInputStream());
			out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(conSocket.getOutputStream())),
					true);

		}catch(IOException e) {
			System.err.println("Error setting up Streams.");
			System.exit(1);
		}
	}

	@SuppressWarnings("unchecked")
	public void communicateWithServer(){
		clientGUI =  new ClientGUI();
		clientGUI.init();

		try {
			while(true) {
				List<Contestant> contestants = new ArrayList<>();
				System.out.println("Waiting for server msg.");
				contestants = ((ServerResponse) in.readObject()).getContestants();
				System.out.println(contestants.size());
				contestants.forEach(o -> {System.out.println(o);});
				
			}
		}catch(IOException | ClassNotFoundException e) {
			System.err.println("Error receiving Server Response");
			e.printStackTrace();
		}
	}
}
