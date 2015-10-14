package ift604.tp1.client.command;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gxt.common.Maybe;
import gxt.common.lispite.Command;
import ift604.common.transport.Cargo;
import ift604.common.transport.MarshallGeneral;
import ift604.common.transport.DatagramSenderReceiver;
import ift604.tp1.client.State;

/**
 * Created by taig1501 on 15-10-14.
 */
public class ListenUDPCommand implements Command {
    State state;

    public ListenUDPCommand(State state) {
        this.state = state;
    }

    @Override
    public Maybe<Object> func() {
        state.setUdpSenderReceiver(new DatagramSenderReceiver(0));

        state.setUdpThread(new Thread(new Runnable() {
            @Override
            public void run() {
                ExecutorService pool = Executors.newFixedThreadPool(1);
                MarshallGeneral<Cargo> mg = new MarshallGeneral<Cargo>(
                        Cargo.class, state.getDispatcher(), state.getUdpSenderReceiver(), pool);
                System.out.println(mg.start());
            }
        }));
        state.getUdpThread().start();
        return Maybe.<Object>Just(this, "started to listen");
    }
}
