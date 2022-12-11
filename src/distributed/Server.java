package distributed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.Contestant;
import game.Game;
import game.Slayer;
import gui.GameGuiMain;
import gui.ClientGUI;

public class Server {

	private ServerSocket ss;
	public static final int PORT = 8080;
//	Map<Slayer, GameStateSender> clientMap = new HashMap<Slayer, GameStateSender>();
	HashMap<Integer, Slayer> onlinePlayers = new HashMap<>();
	Game game;
	ClientGUI gui;

	// private ClientHandlerSlapper clienthandlerslapper;

	public static void main(String[] args) {
		new Server().initServer();
	}

	public void initServer() {

		try {
			System.out.println("Server starting...");
			ss = new ServerSocket(PORT);
			startGame();
			startServer();

		} catch (IOException e) {
			System.err.println("Cannot init Server");
			System.exit(1);
		}
	}

	// Start Game Here?
	public void startGame() {
		game = new Game();
		new Thread(new GUIThread(game)).start();
		// clienthandlerslapper = new ClientHandlerSlapper(Game.REFRESH_INTERVAL);
		// clienthandlerslapper.start();
	}

	public void startServer() {
		while (true) {
			try {
				System.out.println("Waiting for connection...");
				Socket conSocket = ss.accept();

				System.out.println("Connection received: " + conSocket.toString());
				GameStateSender ch = new GameStateSender(conSocket);

				// clienthandlerslapper.addClientHandler(ch);

				registerSlayer(ch);
				ch.start();

			} catch (IOException e) {
				System.err.println("Error starting Server");
				break;
			}
		}

		try {
			ss.close();
		} catch (IOException e) {
			System.err.println("Error on server close");
		}
	}

	private void registerSlayer(GameStateSender ch) {
		System.out.println("Slayer ID " + game.getUsableIdentifier());
		Slayer sl = new Slayer(game.getUsableIdentifier(), game, (byte) 5);
		try {
			game.addPlayerToGame(sl);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println("Error adding player");
		}
		onlinePlayers.put(sl.getIdentification(), sl);
	}

	// responsible for updating the clients with the new game state
	private class GameStateSender extends Thread {
		private ObjectOutputStream out;
//		private BufferedReader in;
		private Socket conSocket;

		public GameStateSender(Socket socket) {
			conSocket = socket;
		}

		@Override
		public void run() {
			System.out.println("starting to serve client");
			try {
				out = new ObjectOutputStream(conSocket.getOutputStream());
				serve();
			} catch (IOException e) {
				System.err.println("Error in server-client connection");
			} finally {
				try {
//					in.close(); 
					out.close();
					conSocket.close();
				} catch (IOException e) {
					System.err.println("Error while closing server-client connection... Abort");
				}
			}
		}

//		void setup() throws IOException {
//			out = new ObjectOutputStream(conSocket.getOutputStream());
//			in = new BufferedReader(new InputStreamReader(conSocket.getInputStream()));
//		}

		private void serve() throws IOException {

			// Implementar as merditxas todas
			try {
				while (true) {
					System.out.println("Sending contestants...");
					ArrayList<Contestant> contestants = game.getContestants();

					List<PlayerDetails> playerDetailsList = new ArrayList<>();
					for (Contestant c : contestants) {
						if (c.getCurrentCell() == null) {
							continue;
						}
						PlayerDetails playerDetails = new PlayerDetails(c.getIdentification(), c.getCurrentStrength(),
								c.getCurrentCell().getPosition(), c.isHumanPlayer());
						playerDetailsList.add(playerDetails);
					}

					GameState gameState = new GameState(playerDetailsList);
					out.writeObject(gameState);
					Thread.sleep(Game.REFRESH_INTERVAL);
				}
			} catch (InterruptedException e) {
				System.err.println("Could Not Send Board.");
			}

		}
	}

	public class ClientInputHandler extends Thread {
		private BufferedReader in;
		private Socket socket;

		public ClientInputHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			System.out.println("starting to serve client");
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				serve();
			} catch (IOException e) {
				System.err.println("Error in server-client connection");
			} finally {
				try {
					in.close(); 
					socket.close();
				} catch (IOException e) {
					System.err.println("Error while closing server-client connection... Abort");
				}
			}
		}
		
		private void serve() throws IOException {
			while (true) {
				String clientInput = in.readLine();
				System.out.println("client input: " + clientInput);
				//parse
			}
		}
	}
}