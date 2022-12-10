package distributed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
		ServerSocket ss = new ServerSocket(PORT);
		try {
			while(true) {
				Socket socket = ss.accept();
				new ClientHandler(socket).start();
			}
		} finally {
			ss.close();
		}
	}
	
	public class ClientHandler extends Thread {
//		private ObjectOutputStream out;
//		private ObjectInputStream in;

		private BufferedReader in;
		private PrintWriter out;

		public ClientHandler(Socket socket) throws IOException {
			setup(socket);
		}
		@Override
		public void run() {
			try {
				serve();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		void setup(Socket socket) throws IOException {
//			in = new ObjectInputStream(socket.getInputStream());
//			out = new ObjectOutputStream(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())),
					true);
		}

		private void serve() throws IOException, ClassNotFoundException {
//			while (true) {
//				ClientRequest req = ((ClientRequest) in.readObject());
				System.out.println("request received");
				String resp = String.valueOf(playerIds.size() + 1);
				System.out.println("sending assigned id: " + resp);
				out.println(resp);
				//spawn request
//				if (req.getId() == null) {
//					int id = playerIds.size() + 1;
//					System.out.println("spawn request, assign id: " + id);
//					out.writeObject(new ServerResponse(id));
//				}
//			}
		}
	}

}
