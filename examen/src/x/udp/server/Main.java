package x.udp.server;

import x.dispatch.ContainerDispatcher;
import x.dispatch.Dispatcher;
import x.dispatch.Receiver;
import x.transport.Boat;
import x.transport.Cargo;
import x.transport.MarshallGeneral;
import x.transport.SenderReceiver;
import x.udp.DatagramSenderReceiver;

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
