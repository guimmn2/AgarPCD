package distributed;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import environment.Direction;
import game.Game;
import gui.ClientGUI;

public class Client {

	private ObjectInputStream in;
	private PrintWriter out;
	private Socket conSocket;
	private String hostName;

	private ClientGUI clientGUI;
	private Game game;

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
		} catch (IOException e) {
			System.err.println("Error connecting to server.");
			System.exit(1);
		}

		try {
			in = new ObjectInputStream(conSocket.getInputStream());
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(conSocket.getOutputStream())), true);

		} catch (IOException e) {
			System.err.println("Error setting up Streams.");
			System.exit(1);
		}
	}

	@SuppressWarnings("unchecked")
	public void communicateWithServer() {
		clientGUI = new ClientGUI();
		clientGUI.init();
		game = clientGUI.getGame();

		// start input listener thread after game init
		new InputSender().start();

		try {
			while (true) {
//				System.out.println("Waiting for server msg.");
				GameState gameState = (GameState) in.readObject();
//				System.out.println("Received server msg.");
				game.clearBoard();

				List<PlayerDetails> playerDetailsList = gameState.getPlayerDetailsList();
				game.setContestants(playerDetailsList);
			}
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error receiving Server Response");
			e.printStackTrace();
		}
	}

	public class InputSender extends Thread {

		private void verifyAndSendInput() {
			Direction dir = clientGUI.getUI().getLastPressedDirection();
			if (dir != null) {
				System.out.println("sending dir to server: " + dir.getVector().toString());
				out.write(dir.getVector().toString());
				clientGUI.getUI().clearLastPressedDirection();
			}
		}

		@Override
		public void run() {
			while (true) {
				verifyAndSendInput();
			}
		}
	}
}
