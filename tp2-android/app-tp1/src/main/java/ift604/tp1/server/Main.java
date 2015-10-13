package ift604.tp1.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ift604.common.cargo.Boat;
import ift604.common.cargo.GetBoat;
import ift604.common.dispatch.ContainerDispatcher;
import ift604.common.dispatch.Dispatcher;
import ift604.common.dispatch.Receiver;
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
        Dispatcher<Cargo> d = new ContainerDispatcher<Cargo>();
        final SenderReceiver sr = new DatagramSenderReceiver(13370);
        d.addReceiver(GetBoat.class, new Receiver<Cargo>() {
            public void receive(Receipt<Cargo> r) {
                System.out.println("received GetBoat from " + r.getOriginAddress() + ":" + r.getOriginPort());
                r.reply(new Cargo(0L, Boat.class, new Boat()));

            }
        });
        ExecutorService pool = Executors.newCachedThreadPool();
        MarshallGeneral<Cargo> mg = new MarshallGeneral<Cargo>(Cargo.class, d, sr, pool);
        System.out.println("Starting server...");
        System.out.println(mg.start());
    }
}
