package x.udp.server;

import gxt.common.Func1;
import gxt.common.Maybe;
import x.transport.Boat;
import x.udp.DatagramSenderReceiver;

public class Main {

	public static void main(String[] args) {
		Maybe<Boat> mboat = DatagramSenderReceiver.Start(13370).bind(
			new Func1<DatagramSenderReceiver, Maybe<Boat>>() {
				public Maybe<Boat> func(DatagramSenderReceiver dsr) {
					return dsr.receive(Boat.class);
				}
		});
		System.out.println(mboat);

	}

}
