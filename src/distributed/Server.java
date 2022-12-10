package distributed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	public static final int PORT = 8080;

	private ArrayList<Integer> playerIds = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		new Server().startServer();
	}

	public void startServer() throws IOException {
		System.out.println("server starting...");
		ServerSocket ss = new ServerSocket(PORT);
		try {
			while (true) {
				Socket socket = ss.accept();
				System.out.println("connection received: " + socket.toString());
				new ClientHandler(socket).start();
			}
		} finally {
			ss.close();
		}
	}

	public class ClientHandler extends Thread {
		private ObjectOutputStream out;
		private BufferedReader in;

		public ClientHandler(Socket socket) throws IOException {
			setup(socket);
		}

		@Override
		public void run() {
			System.out.println("starting to serve client");
			try {
				while (true) {
					serve();
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		void setup(Socket socket) throws IOException {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			in = new ObjectInputStream(socket.getInputStream());
		}

		private void serve() throws IOException, ClassNotFoundException {
			System.out.println("waiting for request...");
//			ClientRequest req = ((ClientRequest) in.readObject());
			System.out.println("request received");
			// spawn request
//			if (req.getId() == null) {
//				int id = playerIds.size() + 1;
//				System.out.println("spawn request, assign id: " + id);
//				out.writeObject(new ServerResponse(id));
//			}
			out.writeObject(new ServerResponse(playerIds.size() + 1));
		}
	}

}
