package ift604.tp1.client;

import java.util.Scanner;

import gxt.common.Act1;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.Tup0;
import gxt.common.lispite.Command;
import gxt.common.lispite.CommandFactory;
import gxt.common.lispite.InputDispatcher;
import gxt.common.lispite.TokenGroup;
import gxt.common.lispite.wip.ExitCommandFactory;
import ift604.common.dispatch.ContainerDispatcher;
import ift604.common.models.Boat;
import ift604.common.cargo.Cargo;
import ift604.common.models.ListeDesMatchs;
import ift604.common.transport.Receipt;
import ift604.tp1.client.command.ConnectTCPCommandFactory;
import ift604.tp1.client.command.GetBoatCommandFactory;
import ift604.tp1.client.command.GetBoatTCPCommandFactory;
import ift604.tp1.client.command.GetMatchListCommandFactory;
import ift604.tp1.client.command.KillTCPCommandFactory;
import ift604.tp1.client.command.KillUDPCommandFactory;
import ift604.tp1.client.command.ListenUDPCommandFactory;
import ift604.tp1.client.command.PutBetTCPCommandFactory;
import ift604.tp1.client.command.StartAutoGetMatchListCommandFactory;

/**
 * Created by taig1501 on 15-10-13.
 */
public class Main {
    public static void main(String[] args) {
        final State currentState = new State();
        currentState.setDispatcher(new ContainerDispatcher<Cargo>());
        currentState.getDispatcher().addReceiver(Boat.class, new Act1<Receipt<Cargo>>() {
            public void func(Receipt<Cargo> r) {
                System.out.println("received Boat");
                Cargo c = r.getPayload();
                Boat b = (Boat) c.getContainer();
                currentState.setBoatsReceived(currentState.getBoatsReceived() + 1);
            }
        });
        currentState.getDispatcher().addReceiver(ListeDesMatchs.class, new Act1<Receipt<Cargo>>() {
            @Override
            public void func(Receipt<Cargo> a) {
                System.out.println("received ListeDesMatchs");
                Cargo c = a.getPayload();
                currentState.setListeDesMatchs((ListeDesMatchs) c.getContainer());
            }
        });

        InputDispatcher id = new InputDispatcher();
        id.addFactory("exit", new ExitCommandFactory());
        id.addFactory("listenUDP", new ListenUDPCommandFactory(currentState));
        id.addFactory("killUDP", new KillUDPCommandFactory(currentState));
        id.addFactory("getBoatUDP", new GetBoatCommandFactory(currentState));
        id.addFactory("getMatchList", new GetMatchListCommandFactory(currentState));
        id.addFactory("countBoats", new CommandFactory() {
            @Override
            public Maybe<Command> make(TokenGroup group) {
                if (group.getGroup().length == 0) {
                    return Maybe.<Command>Just(new Command() {
                        @Override
                        public Maybe<Object> func() {
                            return Maybe.<Object>Just(currentState.getBoatsReceived(), "got this many boats");
                        }
                    }, "made countBoats command");
                } else {
                    return Maybe.<Command>Nothing("count boats command expects no argument");
                }
            }
        });
        id.addFactory("connectTCP", new ConnectTCPCommandFactory(currentState));
        id.addFactory("killTCP", new KillTCPCommandFactory(currentState));
        id.addFactory("getBoatTCP", new GetBoatTCPCommandFactory(currentState));
        id.addFactory("putBet", new PutBetTCPCommandFactory(currentState));
        id.addFactory("startAutoUpdate", new StartAutoGetMatchListCommandFactory(currentState));
        final Scanner sc = new Scanner(System.in);
        do {
            System.out.print(" >");
            String line = sc.nextLine();
            Maybe<Command> c = id.dispatch(line);
            System.out.println(c.toString());
            c.fmap(new Func1<Command, Tup0>() {
                public Tup0 func(Command cmd) {
                    System.out.println(cmd.func().toString());
                    return Tup0.Tup();
                }
            });
        } while (true);
    }
}
