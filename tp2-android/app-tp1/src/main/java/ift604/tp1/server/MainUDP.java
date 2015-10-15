package ift604.tp1.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gxt.common.Act1;
import ift604.common.cargo.*;
import ift604.common.dispatch.ContainerDispatcher;
import ift604.common.dispatch.Dispatcher;
import ift604.common.transport.Cargo;
import ift604.common.transport.MarshallGeneral;
import ift604.common.transport.Receipt;
import ift604.common.transport.Receiver;
import ift604.common.transport.DatagramSenderReceiver;

/**
 * Created by taig1501 on 15-10-13.
 */
public class MainUDP {

    public static void main(String[] args) {
        Receiver sr = new DatagramSenderReceiver(13370);
        Dispatcher<Cargo> d = new ContainerDispatcher<Cargo>();
        d.addReceiver(GetBoat.class, new Act1<Receipt<Cargo>>() {
            public void func(Receipt<Cargo> r) {
                System.out.println("received GetBoat from " + r.getOriginAddress() + ":" + r.getOriginPort());
                r.reply(new Cargo(0L, Boat.class, new Boat()));

            }
        });
        ExecutorService pool = Executors.newCachedThreadPool();
        MarshallGeneral<Cargo> mg = new MarshallGeneral<Cargo>(Cargo.class, d, sr, pool, Shutdown.getShutdownCargo());
        System.out.println("Starting UDP server...");
        System.out.println(mg.start());
    }
}
