package distributed;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import game.Game;
import gui.ClientGUI;

public class Client {

	private ObjectInputStream in;
	private PrintWriter out;
	private Socket conSocket;
	private String hostName;

	private ClientGUI clientGUI;
	private Game game;

	private KeyListener keyListener;
	private static boolean alternativeKeys = false;
	
	private static InetAddress addr;
	private static int port;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Client clt = new Client("localhost");

		addr = InetAddress.getByName(args[0]);
		port = Integer.parseInt(args[1]);
		if(args[2].equals("0")) {
			alternativeKeys = false;
		}else if(args[2].equals("1")) {
			alternativeKeys = true;
		}else{
			throw new IllegalArgumentException();
		}
		
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
			conSocket = new Socket(addr, port);
			System.out.println("connecting to address: " + addr + " on port: " + port);
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
		clientGUI = new ClientGUI(alternativeKeys);
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

		@Override
		public void run() {
			keyListener = new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyPressed(KeyEvent e) {
					if (alternativeKeys) {
						switch (e.getKeyCode()) {
						case KeyEvent.VK_A:
							out.println(environment.Direction.LEFT.toString());
							break;
						case KeyEvent.VK_D:
							out.println(environment.Direction.RIGHT.toString());
							break;
						case KeyEvent.VK_W:
							out.println(environment.Direction.UP.toString());
							break;
						case KeyEvent.VK_S:
							out.println(environment.Direction.DOWN.toString());
							break;
						}
					} else {
						switch (e.getKeyCode()) {
						case KeyEvent.VK_LEFT:
							out.println(environment.Direction.LEFT.toString());
							break;
						case KeyEvent.VK_RIGHT:
							out.println(environment.Direction.RIGHT.toString());
							break;
						case KeyEvent.VK_UP:
							out.println(environment.Direction.UP.toString());
							break;
						case KeyEvent.VK_DOWN:
							out.println(environment.Direction.DOWN.toString());
							break;
						}
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub

				}

			};
			clientGUI.getBoard().addKeyListener(keyListener);
		}
	}
}
