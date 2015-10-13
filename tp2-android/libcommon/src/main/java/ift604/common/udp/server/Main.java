package ift604.common.udp.server;

import ift604.common.dispatch.ContainerDispatcher;
import ift604.common.dispatch.Dispatcher;
import ift604.common.dispatch.Receiver;
import ift604.common.transport.Boat;
import ift604.common.transport.Cargo;
import ift604.common.transport.MarshallGeneral;
import ift604.common.transport.SenderReceiver;
import ift604.common.udp.DatagramSenderReceiver;

public class Main {

	public static void main(String[] args) {
		Dispatcher<Cargo> d = new ContainerDispatcher<Cargo>();
		d.addReceiver(Boat.class, new Receiver<Cargo>() {
			public void receive(Cargo c) {
				Boat b = (Boat) c.getContainer();
				System.out.println(b); // how ugly is this?! no IO! bad! (I don't want to write Monad IO...)
			}
		});
		SenderReceiver sr = new DatagramSenderReceiver(13370);
		MarshallGeneral<Cargo> mg = new MarshallGeneral<Cargo>(Cargo.class, d, sr);
		System.out.println(mg.start());
	}

}
