import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerThread implements Runnable {
	
	/** 
	 * Class manages one HTTP Get Request
	 * Receives request from client, forwards request to the remote host, receives response from remote host, and sends response to client
	 *  */
	
	private Socket clientSocket;
	private InputStream inFromClient;
	private OutputStream outToClient;
	
	private Socket serverSocket;
	private InputStream inFromServer;
	private OutputStream outToServer;
	
	private String remoteHost;
	private int remotePort;
	
	public ServerThread(Socket clientSocket){
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		try {
			/* byte buffers to store request and reply */
			byte[] request = new byte[4096];
			byte[] reply = new byte[20000];
			
			/* stores client input and output streams*/
			this.inFromClient = clientSocket.getInputStream();
			this.outToClient = clientSocket.getOutputStream();
			
			/*reads the client request into the buffer and stores the size in bytes of the request */
			int bytes_read = inFromClient.read(request);

			/*gets client request in string form, and changes the connection from persistant to closed */
			String decoded = new String(request, "UTF-8");
			decoded = decoded.replace("Connection: keep-alive", "Connection: close");
			request = decoded.getBytes(StandardCharsets.UTF_8);
			
			/*ensures the request is a HTTP GET */
			if(decoded.startsWith("GET")){
				
				/*parses the remote host from the HTTP request */
				String[] lines = decoded.split("\n");
				this.remoteHost = lines[1].substring(6, lines[1].length()).trim();
				
				this.remotePort = 80;
			
				/*opens a connection to the remote host*/
				this.serverSocket = new Socket(remoteHost,remotePort);
				this.outToServer = serverSocket.getOutputStream();
				this.inFromServer = serverSocket.getInputStream();
				
				/*writes request to remote host */
				outToServer.write(request,0,bytes_read);
				outToServer.flush();
				
				/*receives response from remote host and writes it to client in one step */
				int br;
				while((br = inFromServer.read(reply)) != -1){
					outToClient.write(reply,0,br);
					outToClient.flush();
				}
			
				/* closes the connection to the remote host*/
				serverSocket.close();
			}
			/* closes the client connection */
			clientSocket.close();
			

		} catch(Exception e){
        	e.printStackTrace();
		}
		
	}
}
