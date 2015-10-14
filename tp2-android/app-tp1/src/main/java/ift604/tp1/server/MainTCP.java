package ift604.tp1.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gxt.common.Act1;
import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.MaybeBase;
import gxt.common.Monad;
import ift604.common.cargo.Boat;
import ift604.common.cargo.GetBoat;
import ift604.common.dispatch.ContainerDispatcher;
import ift604.common.dispatch.Dispatcher;
import ift604.common.tcp.StreamReceiver;
import ift604.common.transport.Cargo;
import ift604.common.transport.MarshallGeneral;
import ift604.common.transport.Receipt;
import ift604.common.transport.Receiver;
import ift604.common.udp.DatagramSenderReceiver;

/**
 * Created by taig1501 on 15-10-13.
 */
public class MainTCP {

    public static void main(String[] args) {
        Challenge result = Challenge.Maybe(StreamReceiver.newStreamSenderReceiver(13380),
                new Func1<StreamReceiver, Challenge>() {
                    @Override
                    public Challenge func(StreamReceiver a) {
                        Dispatcher<Cargo> d = new ContainerDispatcher<Cargo>();
                        d.addReceiver(GetBoat.class, new Act1<Receipt<Cargo>>() {
                            public void func(Receipt<Cargo> r) {
                                System.out.println("received GetBoat from " + r.getOriginAddress() + ":" + r.getOriginPort());
                                r.reply(new Cargo(0L, Boat.class, new Boat()));

                            }
                        });
                        ExecutorService pool = Executors.newCachedThreadPool();
                        MarshallGeneral<Cargo> mg = new MarshallGeneral<Cargo>(Cargo.class, d, a, pool);
                        System.out.println("Starting TCP server...");
                        return mg.start();
                    }
                });
        System.out.println(result);
    }
}
