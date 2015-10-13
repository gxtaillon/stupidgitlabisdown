package ift604.tp1.paristcpserver;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler implements Runnable {
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	public RequestHandler(Socket socket) {
		this.socket = socket;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader( new InputStreamReader( socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
	  try {
		  String inputLine;
		  while ((inputLine = in.readLine()) != null) { 
			  // Do something with input
			  out.println(inputLine); 
			  
			  // Break
			  if (inputLine.equals("Bye.")) 
				  break; 
		  }
	  } catch(EOFException e) {
		  System.out.println("EOF:"+e.getMessage());
	  } catch(IOException e) {
		  System.out.println("IO:"+e.getMessage());
	  } finally {
		  try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	  }
	}

}
