package ift604.tp1.server;

import gxt.common.Act1;
import ift604.common.cargo.*;
import ift604.common.dispatch.ContainerDispatcher;
import ift604.common.dispatch.Dispatcher;
import ift604.common.models.*;
import ift604.common.models.Boat;
import ift604.common.service.ActService;
import ift604.common.cargo.Cargo;
import ift604.common.transport.DatagramSenderReceiver;
import ift604.common.transport.MarshallGeneral;
import ift604.common.transport.Receipt;
import ift604.common.transport.Receiver;
import ift604.common.transport.StreamReceiver;

/**
 * Created by taig1501 on 15-10-15.
 */
public class Main {
    public static void tcpServer(final State state) {
        Receiver sr = new StreamReceiver(13380);
        Dispatcher<Cargo> d = new ContainerDispatcher<Cargo>();
        final ActService<ParisManager> parisActService = new ActService<>(state.getParisManager());

        d.addReceiver(GetBoat.class, new Act1<Receipt<Cargo>>() {
            public void func(Receipt<Cargo> receipt) {
                System.out.println("received GetBoat over TCP from " + receipt.getOriginAddress() + ":" + receipt.getOriginPort());
                receipt.reply(new Cargo(0L, Boat.class, new Boat()));
            }
        });
        d.addReceiver(PutBet.class, new Act1<Receipt<Cargo>>() {
            @Override
            public void func(final Receipt<Cargo> receipt) {
                System.out.println("received PutBet over TCP from " + receipt.getOriginAddress() + ":" + receipt.getOriginPort());
                parisActService.addAct(new Act1<ParisManager>() {
                    @Override
                    public void func(ParisManager parisManager) {
                        PutBet bet = (PutBet)receipt.getPayload().getContainer();
                        //parisManager.addBet(bet.getMatchId(), bet.getUserId(), bet.getAmount());
                    }
                });
            }
        });
        MarshallGeneral<Cargo> mg = new MarshallGeneral<Cargo>(Cargo.class, d, sr, Shutdown.getShutdownCargo());
        System.out.println("Starting TCP server...");
        System.out.println(mg.start());
    }

    public static void udpServer(final State state) {
        Receiver sr = new DatagramSenderReceiver(13370);
        Dispatcher<Cargo> d = new ContainerDispatcher<Cargo>();
        d.addReceiver(GetBoat.class, new Act1<Receipt<Cargo>>() {
            public void func(Receipt<Cargo> r) {
                System.out.println("received GetBoat over UDP from " + r.getOriginAddress() + ":" + r.getOriginPort());
                r.reply(new Cargo(0L, Boat.class, new Boat()));

            }
        });
        d.addReceiver(GetMatchList.class, new Act1<Receipt<Cargo>>() {
            @Override
            public void func(Receipt<Cargo> r) {
                System.out.println("received GetMatchList over UDP from " + r.getOriginAddress() + ":" + r.getOriginPort());
                r.reply(new Cargo(0L, ListeDesMatchs.class, state.getListeDesMatchs()));
            }
        });
        MarshallGeneral<Cargo> mg = new MarshallGeneral<Cargo>(Cargo.class, d, sr, Shutdown.getShutdownCargo());
        System.out.println("Starting UDP server...");
        System.out.println(mg.start());
    }

    public static void main(String[] args) {
        // Init all da things

        User u1 = new User();

        // Liste des Équipes
        Team Canadiens = new Team("Montreal Canadiens");
        Team Bruins = new Team("Boston Bruins");

        // Liste des Joueurs du Canadien
        Canadiens.addPlayer(new Player(41,"PAUL BYRON"));
        Canadiens.addPlayer(new Player(51,"DAVID DESHARNAIS"));
        Canadiens.addPlayer(new Player(81,"LARS ELLER"));
        Canadiens.addPlayer(new Player(15,"TOMAS FLEISCHMANN"));
        Canadiens.addPlayer(new Player(32,"BRIAN FLYNN"));
        Canadiens.addPlayer(new Player(27,"ALEX GALCHENYUK"));
        Canadiens.addPlayer(new Player(11,"BRENDAN GALLAGHER"));
        Canadiens.addPlayer(new Player(8,"ZACK KASSIAN"));
        Canadiens.addPlayer(new Player(17,"TORREY MITCHELL"));
        Canadiens.addPlayer(new Player(67,"MAX PACIORETTY"));
        Canadiens.addPlayer(new Player(14,"TOMAS PLEKANEC"));
        Canadiens.addPlayer(new Player(13,"ALEXANDER SEMIN"));
        Canadiens.addPlayer(new Player(21,"DEVANTE SMITH-PELLY"));
        Canadiens.addPlayer(new Player(22,"DALE WEISE"));
        Canadiens.addPlayer(new Player(28,"NATHAN BEAULIEU"));
        Canadiens.addPlayer(new Player(74,"ALEXEI EMELIN"));
        Canadiens.addPlayer(new Player(77,"TOM GILBERT"));
        Canadiens.addPlayer(new Player(28,"ANDREI MARKOV"));
        Canadiens.addPlayer(new Player(6,"GREG PATERYN"));
        Canadiens.addPlayer(new Player(26,"JEFF PETRY"));
        Canadiens.addPlayer(new Player(76,"P.K. SUBBAN"));
        Canadiens.addPlayer(new Player(24,"JARRED TINORDI"));
        Canadiens.addPlayer(new Player(39,"MIKE CONDON"));
        Canadiens.addPlayer(new Player(31,"CAREY PRICE"));

        // Liste des Joueurs de Boston
        Bruins.addPlayer(new Player(39,"MATT BELESKEY"));
        Bruins.addPlayer(new Player(37,"PATRICE BERGERON"));
        Bruins.addPlayer(new Player(14,"BRETT CONNOLLY"));
        Bruins.addPlayer(new Player(21,"LOUI ERIKSSON "));
        Bruins.addPlayer(new Player(11,"JIMMY HAYES"));
        Bruins.addPlayer(new Player(23,"CHRIS KELLY"));
        Bruins.addPlayer(new Player(41,"JOONAS KEMPPAINEN"));
        Bruins.addPlayer(new Player(46,"DAVID KREJCI"));
        Bruins.addPlayer(new Player(63,"BRAD MARCHAND"));
        Bruins.addPlayer(new Player(88,"DAVID PASTRNAK"));
        Bruins.addPlayer(new Player(64,"TYLER RANDELL"));
        Bruins.addPlayer(new Player(36,"ZAC RINALDO"));
        Bruins.addPlayer(new Player(51,"RYAN SPOONER"));
        Bruins.addPlayer(new Player(25,"MAX TALBOT"));
        Bruins.addPlayer(new Player(33,"ZDENO CHARA"));
        Bruins.addPlayer(new Player(56,"TOMMY CROSS"));
        Bruins.addPlayer(new Player(47,"TOREY KRUG"));
        Bruins.addPlayer(new Player(54,"ADAM MCQUAID"));
        Bruins.addPlayer(new Player(48,"COLIN MILLER"));
        Bruins.addPlayer(new Player(86,"KEVAN MILLER"));
        Bruins.addPlayer(new Player(45,"JOE MORROW"));
        Bruins.addPlayer(new Player(62,"ZACH TROTMAN"));
        Bruins.addPlayer(new Player(50,"JONAS GUSTAVSSON"));
        Bruins.addPlayer(new Player(40,"TUUKKA RASK"));

        ListeDesMatchs liste = ListeDesMatchs.getInstance();

        Match match1 = new Match(Canadiens, Bruins);
        Match match2 = new Match(new Team("Toronto MapleLeafs"), new Team("Vancouver Canucks"));
        Match match3 = new Match(new Team("Edmonton Oilers"), new Team("NY Rangers"));

        liste.add(match1);
        liste.add(match2);
        liste.add(match3);

        ParisManager parisManager = new ParisManager(match1);

        final State currentState = new State();
        currentState.setListeDesMatchs(liste);
        currentState.setParisManager(parisManager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                tcpServer(currentState);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                udpServer(currentState);
            }
        }).start();
    }
}
