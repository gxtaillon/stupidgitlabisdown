package ift604.tp1.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gxt.common.Act1;
import ift604.common.cargo.Boat;
import ift604.common.cargo.GetBoat;
import ift604.common.cargo.PutBet;
import ift604.common.dispatch.ContainerDispatcher;
import ift604.common.dispatch.Dispatcher;
import ift604.common.service.ActService;
import ift604.common.transport.StreamReceiver;
import ift604.common.transport.Cargo;
import ift604.common.transport.MarshallGeneral;
import ift604.common.transport.Receipt;
import ift604.common.transport.Receiver;
import ift604.tp1.paristcpserver.ParisService;

/**
 * Created by taig1501 on 15-10-13.
 */
public class MainTCP {

    public static void main(String[] args) {
        Receiver sr = new StreamReceiver(13380);
        Dispatcher<Cargo> d = new ContainerDispatcher<Cargo>();
        final ActService<ParisService> parisActService = new ActService<>(new ParisService());

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
                parisActService.addAct(new Act1<ParisService>() {
                    @Override
                    public void func(ParisService parisService) {
                        PutBet bet = (PutBet)receipt.getPayload().getContainer();
                        parisService.parier(bet.getMatchId(), bet.getUserId(), bet.getAmount());
                    }
                });
            }
        });
        ExecutorService pool = Executors.newCachedThreadPool();
        MarshallGeneral<Cargo> mg = new MarshallGeneral<Cargo>(Cargo.class, d, sr, pool);
        System.out.println("Starting TCP server...");
        System.out.println(mg.start());
    }
}
