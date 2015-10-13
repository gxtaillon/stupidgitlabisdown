package ift604.tp1;

import java.io.IOException;

import ift604.tp1.models.InvalidParisException;
import ift604.tp1.models.ListeDesMatchs;
import ift604.tp1.models.Match;
import ift604.tp1.models.ParisManager;
import ift604.tp1.models.Team;
import ift604.tp1.models.User;
import ift604.tp1.models.Player;
import ift604.tp1.paristcpserver.SoireeHockeyServer;

public class Main {

	public static void main(String[] args) throws IOException {
		// Init all da things
		
		User u1 = new User();

		// Liste des Équipes
		Team Canadiens = new Team("Montreal Canadiens");
		Team Bruins = new Team("Boston Bruins");

		// Liste des Joueurs du Canadien
		Player playerC1 = new Player(41,"PAUL BYRON");
		Player playerC2 = new Player(51,"DAVID DESHARNAIS");
		Player playerC3 = new Player(81,"LARS ELLER");
		Player playerC4 = new Player(15,"TOMAS FLEISCHMANN");
		Player playerC5 = new Player(32,"BRIAN FLYNN");
		Player playerC6 = new Player(27,"ALEX GALCHENYUK");
		Player playerC7 = new Player(11,"BRENDAN GALLAGHER");
		Player playerC8 = new Player(8,"ZACK KASSIAN");
		Player playerC9 = new Player(17,"TORREY MITCHELL");
		Player playerC10 = new Player(67,"MAX PACIORETTY");
		Player playerC11 = new Player(14,"TOMAS PLEKANEC");
		Player playerC12 = new Player(13,"ALEXANDER SEMIN");
		Player playerC13 = new Player(21,"DEVANTE SMITH-PELLY");
		Player playerC14 = new Player(22,"DALE WEISE");
		Player playerC15 = new Player(28,"NATHAN BEAULIEU");
		Player playerC16 = new Player(74,"ALEXEI EMELIN");
		Player playerC17 = new Player(77,"TOM GILBERT");
		Player playerC18 = new Player(28,"ANDREI MARKOV");
		Player playerC19 = new Player(6,"GREG PATERYN");
		Player playerC20 = new Player(26,"JEFF PETRY");
		Player playerC21 = new Player(76,"P.K. SUBBAN");
		Player playerC22 = new Player(24,"JARRED TINORDI");
		Player playerC23 = new Player(39,"MIKE CONDON");
		Player playerC24 = new Player(31,"CAREY PRICE");

		// Liste des Joueurs de Boston
		Player playerB1 = new Player(39,"MATT BELESKEY");
		Player playerB2 = new Player(37,"PATRICE BERGERON");
		Player playerB3 = new Player(14,"BRETT CONNOLLY");
		Player playerB4 = new Player(21,"LOUI ERIKSSON ");
		Player playerB5 = new Player(11,"JIMMY HAYES");
		Player playerB6 = new Player(23,"CHRIS KELLY");
		Player playerB7 = new Player(41,"JOONAS KEMPPAINEN");
		Player playerB8 = new Player(46,"DAVID KREJCI");
		Player playerB9 = new Player(63,"BRAD MARCHAND");
		Player playerB10 = new Player(88,"DAVID PASTRNAK");
		Player playerB11 = new Player(64,"TYLER RANDELL");
		Player playerB12 = new Player(36,"ZAC RINALDO");
		Player playerB13 = new Player(51,"RYAN SPOONER");
		Player playerB14 = new Player(25,"MAX TALBOT");
		Player playerB15 = new Player(33,"ZDENO CHARA");
		Player playerB16 = new Player(56,"TOMMY CROSS");
		Player playerB17 = new Player(47,"TOREY KRUG");
		Player playerB18 = new Player(54,"ADAM MCQUAID");
		Player playerB19 = new Player(48,"COLIN MILLER");
		Player playerB20 = new Player(86,"KEVAN MILLER");
		Player playerB21 = new Player(45,"JOE MORROW");
		Player playerB22 = new Player(62,"ZACH TROTMAN");
		Player playerB23 = new Player(50,"JONAS GUSTAVSSON");
		Player playerB24 = new Player(40,"TUUKKA RASK");


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
