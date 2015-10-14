package ift604.tp1.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gxt.common.Act1;
import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.Tup0;
import gxt.common.extension.ExceptionExtension;
import gxt.common.lispite.Command;
import gxt.common.lispite.CommandFactory;
import gxt.common.lispite.InputDispatcher;
import gxt.common.lispite.TokenGroup;
import gxt.common.lispite.wip.ExitCommandFactory;
import ift604.common.cargo.GetBoat;
import ift604.common.dispatch.ContainerDispatcher;
import ift604.common.dispatch.Dispatcher;
import ift604.common.cargo.Boat;
import ift604.common.tcp.StreamSenderReceiver;
import ift604.common.transport.Cargo;
import ift604.common.transport.MarshallGeneral;
import ift604.common.transport.Receipt;
import ift604.common.transport.StreamSender;
import ift604.common.udp.DatagramSenderReceiver;

/**
 * Created by taig1501 on 15-10-13.
 */
public class Main {
    public static void main(String[] args) {
        final State currentState = new State();
        final DatagramSenderReceiver dsr = new DatagramSenderReceiver(13371);
        final Dispatcher<Cargo> dispatcher = new ContainerDispatcher<Cargo>();
        dispatcher.addReceiver(Boat.class, new Act1<Receipt<Cargo>>() {
            public void func(Receipt<Cargo> r) {
                System.out.println("received Boat");
                Cargo c = r.getPayload();
                Boat b = (Boat) c.getContainer();
                ++currentState.boatsReceived;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                ExecutorService pool = Executors.newCachedThreadPool();
                MarshallGeneral<Cargo> mg = new MarshallGeneral<Cargo>(Cargo.class, dispatcher, dsr, pool);
                System.out.println(mg.start());
            }
        }).start();

        final MarshallGeneral<Cargo>[] mg = new MarshallGeneral[1];
        final StreamSenderReceiver[] ssr = new StreamSenderReceiver[1];
        final Thread[] tcpThread = new Thread[1];

        InputDispatcher id = new InputDispatcher();
        id.addFactory("exit", new ExitCommandFactory());
        id.addFactory("getBoat", new GetBoatCommandFactory(dsr));
        id.addFactory("getMatchList", new GetMarchListCommandFactory(dsr));
        id.addFactory("countBoats", new CommandFactory() {
            @Override
            public Maybe<Command> make(TokenGroup group) {
                if (group.getGroup().length == 0) {
                    return Maybe.<Command>Just(new Command() {
                        @Override
                        public Maybe<Object> func() {
                            return Maybe.<Object>Just(currentState.boatsReceived, "got this many boats");
                        }
                    }, "made countBoats command");
                } else {
                    return Maybe.<Command>Nothing("count boats command expects no argument");
                }
            }
        });
        id.addFactory("connectTCP", new CommandFactory() {
            @Override
            public Maybe<Command> make(final TokenGroup group) {
                if (group.getGroup().length == 2) {
                    return Maybe.<Command> Just(
                            new Command() {
                                @Override
                                public Maybe<Object> func() {
                                    try {
                                        ssr[0] = new StreamSenderReceiver(
                                                InetAddress.getByName(group.getGroup()[0].getName()),
                                                Integer.parseInt(group.getGroup()[1].getName()));

                                        return ssr[0].connect().bind(new Func1<StreamSender, Maybe<Object>>() {
                                            @Override
                                            public Maybe<Object> func(StreamSender a) {

                                                tcpThread[0] = new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ExecutorService pool = Executors.newCachedThreadPool();
                                                        mg[0] = new MarshallGeneral<Cargo>(Cargo.class, dispatcher, ssr[0], pool);
                                                        System.out.println(mg[0].start());
                                                    }
                                                });
                                                tcpThread[0].start();
                                                return Maybe.<Object>Just(a, "connected");
                                            }
                                        });
                                    } catch (UnknownHostException e) {
                                        return Maybe.<Object>Nothing(ExceptionExtension.stringnify(e));
                                    }
                                }
                            },
                            "made connect command");
                } else {
                    return Maybe
                            .<Command> Nothing("connect command expected first argument to be host and second to be port");
                }
            }
        });
        id.addFactory("getBoatTCP", new CommandFactory() {
            @Override
            public Maybe<Command> make(final TokenGroup group) {
                if (group.getGroup().length == 0) {
                    return Maybe.<Command> Just(
                            new Command() {
                                @Override
                                public Maybe<Object> func() {
                                    return Maybe.<Object>Challenge(
                                            ssr[0].send(new Cargo(0L, GetBoat.class, new GetBoat())),
                                            new Func1<String, Maybe<Object>>() {
                                                @Override
                                                public Maybe<Object> func(String a) {
                                                    return Maybe.<Object>Just(this, a);
                                                }
                                            });
                                }
                            },
                            "made getBoatTCP command");
                } else {
                    return Maybe
                            .<Command> Nothing("getBoatTCP command expects no argument");
                }
            }
        });
        id.addFactory("killTCP", new CommandFactory() {
            @Override
            public Maybe<Command> make(final TokenGroup group) {
                if (group.getGroup().length == 0) {
                    return Maybe.<Command> Just(
                        new Command() {
                            @Override
                            public Maybe<Object> func() {
                                mg[0].setActive(Challenge.Failure("killTCP command engaged"));
                                return Maybe.<Object>Just(this, "asked marshall to stop");
                            }
                        },
                        "made killTCP command");
                } else {
                    return Maybe
                            .<Command> Nothing("killTCP command expects no argument");
                }
            }
        });
        Maybe<Command> c;
        Scanner sc = new Scanner(System.in);
        try {
            do {
                System.out.print(" >");
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
