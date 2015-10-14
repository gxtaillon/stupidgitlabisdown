package ift604.common.udp.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gxt.common.Act1;
import ift604.common.dispatch.ContainerDispatcher;
import ift604.common.dispatch.Dispatcher;
import ift604.common.cargo.Boat;
import ift604.common.transport.Cargo;
import ift604.common.transport.MarshallGeneral;
import ift604.common.transport.Receipt;
import ift604.common.transport.Receiver;
import ift604.common.udp.DatagramSenderReceiver;

public class Main {

	public static void main(String[] args) {
		Dispatcher<Cargo> d = new ContainerDispatcher<Cargo>();
		d.addReceiver(Boat.class, new Act1<Receipt<Cargo>>() {
			public void func(Receipt<Cargo> r) {
				Cargo c = r.getPayload();
				Boat b = (Boat) c.getContainer();
				System.out.println(b); // how ugly is this?! no IO! bad! (I don't want to write Monad IO...)
			}
		});
		Receiver sr = new DatagramSenderReceiver(13370);
		ExecutorService pool = Executors.newCachedThreadPool();
		MarshallGeneral<Cargo> mg = new MarshallGeneral<Cargo>(Cargo.class, d, sr, pool);
		System.out.println(mg.start());
	}

}
