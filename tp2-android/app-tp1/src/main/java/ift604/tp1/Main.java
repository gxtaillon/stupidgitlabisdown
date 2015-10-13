package ift604.tp1;

import java.io.IOException;

import ift604.tp1.models.InvalidParisException;
import ift604.tp1.models.ListeDesMatchs;
import ift604.tp1.models.Match;
import ift604.tp1.models.ParisManager;
import ift604.tp1.models.Team;
import ift604.tp1.models.User;
import ift604.tp1.paristcpserver.SoireeHockeyServer;

public class Main {

	public static void main(String[] args) throws IOException {
		// Init all da things
		
		User u1 = new User();
		Team Canadiens = new Team("Montreal Canadiens");
		Team Bruins = new Team("Boston Bruins");
		
		ListeDesMatchs liste = ListeDesMatchs.getInstance();
		
		Match match1 = new Match(Canadiens, Bruins);
		Match match2 = new Match(new Team("Toronto MapleLeafs"), new Team("Vancouver Canucks"));
		Match match3 = new Match(new Team("Edmonton Oilers"), new Team("NY Rangers"));
		
		liste.add(match1);
		liste.add(match2);
		liste.add(match3);

		ParisManager p1 = new ParisManager(match1);

		try {
			p1.addBet(u1, Canadiens, 100.0);
		} catch (InvalidParisException e) {
			e.printStackTrace();
		}
		
		// Start TCP And UDP Servers
		SoireeHockeyServer tcpServer = new SoireeHockeyServer(3000, 8);
		tcpServer.run();
		
		// telnet localhost:3000 -> just an echo server for now
		return;
	}

}
