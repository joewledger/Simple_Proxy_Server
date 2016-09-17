import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class proxyd{
	
	/**
	 * Class implements a multithreaded proxy server that processes HTTP GET requests
	 * @param args[0] is the port to listen on
	 * @throws IOException when another process is already listening on the given port
	 */
	
	
	public static void main(String[] args) throws IOException{
		
		/* parses port number from args */
		int port_num = Integer.parseInt(args[0]);
		
		/* opens a server socket to receive connections from clients */
		ServerSocket serverSocket = new ServerSocket(port_num);
		
		try{
			/* listens on given port, when request is received a new thread is spawned to process it */
			while (true) {
				Socket clientSocket = serverSocket.accept();
				new Thread(new ServerThread(clientSocket)).start();
			}
		} finally {
			/* closes the server socket */
			serverSocket.close();
		}
		
    }
}
