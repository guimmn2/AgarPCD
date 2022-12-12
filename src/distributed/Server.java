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

import environment.Direction;
import game.Contestant;
import game.Game;
import game.Slayer;
import gui.ClientGUI;

public class Server {

	private ServerSocket ss;
	public static final int PORT = 8080;
	HashMap<Integer, Slayer> clientMap = new HashMap<>();
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
	}

	public void startServer() {
		while (true) {
			try {
				System.out.println("Waiting for connection...");
				Socket conSocket = ss.accept();

				System.out.println("Connection received: " + conSocket.toString());
				GameStateSender sender = new GameStateSender(conSocket);

				registerSlayer(conSocket);
				sender.start();

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

	private void registerSlayer(Socket socket) {
		Slayer sl = new Slayer(game.getUsableIdentifier(), game, (byte) 5);
		try {
			game.addPlayerToGame(sl);
			new ClientInputHandler(socket, sl.getIdentification()).start();;
			clientMap.put(sl.getIdentification(), sl);
		} catch (InterruptedException e) {
			System.err.println("Error adding player");
		}
	}

	// responsible for updating the clients with the new game state
	private class GameStateSender extends Thread {
		private ObjectOutputStream out;
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
					out.close();
					conSocket.close();
				} catch (IOException e) {
					System.err.println("Error while closing server-client connection... Abort");
				}
			}
		}

		private void serve() throws IOException {

			try {
				while (true) {
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
		private int id;

		public ClientInputHandler(Socket socket, int id) {
			this.socket = socket;
			this.id = id;
		}

		@Override
		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				serve();
			} catch (IOException e) {
				game.removePlayerFromCell(clientMap.get(id).getCurrentCell().getPosition());
				clientMap.remove(id);
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
				System.out.println("waiting for client input...");
				String clientInput = in.readLine();
				System.out.println("client input: " + clientInput);
				
				Slayer sl = clientMap.get(id);
				
				switch (clientInput) {
				case "UP":
					sl.moveKeys(Direction.UP);
					break;
				case "LEFT":
					sl.moveKeys(Direction.LEFT);
					break;
				case "RIGHT":
					sl.moveKeys(Direction.RIGHT);
					break;
					
				case "DOWN":
					sl.moveKeys(Direction.DOWN);
					break;
					
				case "W":
					sl.moveKeys(Direction.UP);
					break;
				case "A":
					sl.moveKeys(Direction.LEFT);
					break;
				case "D":
					sl.moveKeys(Direction.RIGHT);
					break;
					
				case "S":
					sl.moveKeys(Direction.DOWN);
					break;
				}
			}
		}
	}
}