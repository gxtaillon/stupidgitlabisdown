package ift604.tp1.client;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.Tup0;
import gxt.common.lispite.Command;
import gxt.common.lispite.InputDispatcher;
import gxt.common.lispite.wip.EchoCommandFactory;
import gxt.common.lispite.wip.ExitCommandFactory;
import ift604.common.dispatch.ContainerDispatcher;
import ift604.common.dispatch.Dispatcher;
import ift604.common.dispatch.Receiver;
import ift604.common.cargo.Boat;
import ift604.common.transport.Cargo;
import ift604.common.transport.MarshallGeneral;
import ift604.common.transport.Receipt;
import ift604.common.transport.SenderReceiver;
import ift604.common.udp.DatagramSenderReceiver;

/**
 * Created by taig1501 on 15-10-13.
 */
public class Main {
    public static void main(String[] args) {
        final State currentState = new State();
        final SenderReceiver sr = new DatagramSenderReceiver(13371);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Dispatcher<Cargo> d = new ContainerDispatcher<Cargo>();
                d.addReceiver(Boat.class, new Receiver<Cargo>() {
                    public void receive(Receipt<Cargo> r) {
                        System.out.println("received Boat");
                        Cargo c = r.getPayload();
                        Boat b = (Boat) c.getContainer();
                        ++currentState.boatsReceived;
                    }
                });
                ExecutorService pool = Executors.newCachedThreadPool();
                MarshallGeneral<Cargo> mg = new MarshallGeneral<Cargo>(Cargo.class, d, sr, pool);
                System.out.println(mg.start());
            }
        }).start();

        InputDispatcher id = new InputDispatcher();
        id.addFactory("exit", new ExitCommandFactory());
        id.addFactory("getBoat", new GetBoatCommandFactory(sr));
        Maybe<Command> c;
        Scanner sc = new Scanner(System.in);
        try {
            do {
                String line = sc.nextLine();
                c = id.dispatch(line);
                System.out.println(c.toString());
                c.fmap(new Func1<Command, Tup0>() {
                    public Tup0 func(Command cmd) {
                        System.out.println(cmd.func().toString());
                        return Tup0.Tup();
                    }
                });
            } while (true);
        } finally {
            sc.close();
            System.out.println("bye");
        }
    }
}
