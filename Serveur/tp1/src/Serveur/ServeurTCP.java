package Serveur;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurTCP {
	   public static void main(String args[]) {
		   System.out.println("server is running");
		   String data = "test";
	      try {
	         ServerSocket srvr = new ServerSocket(1234);
	         Socket skt = srvr.accept();
	         System.out.print("Server has connected!\n");
	         PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
	         System.out.print("Sending string: '" + data + "'\n");
	         out.print(data);
	         out.close();
	         skt.close();
	         srvr.close();
	      }
	      catch(Exception e) {
	         System.out.print("Whoops! It didn't work!\n");
	      }
	   }
	}
	