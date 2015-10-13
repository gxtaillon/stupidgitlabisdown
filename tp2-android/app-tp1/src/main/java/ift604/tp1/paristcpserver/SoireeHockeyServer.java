package ift604.tp1.paristcpserver;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoireeHockeyServer implements Runnable {
	
	private final ServerSocket serverSocket;
	private final ExecutorService pool;
	
	public SoireeHockeyServer(int port, int poolSize) throws IOException {
		serverSocket = new ServerSocket(port);
		pool = Executors.newFixedThreadPool(poolSize);
	}

	@Override
	public void run() {
		try {
			for (;;) {
				pool.execute(new RequestHandler(serverSocket.accept()));
				System.out.println("Accepted");
			}
		} catch (IOException ex) {
			System.out.println("IO:"+ex.getMessage());
			pool.shutdown();
		}		
	}
}
