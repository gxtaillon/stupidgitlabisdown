package x.udp.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.extension.ExceptionExtension;
import x.transport.Boat;
import x.transport.Cargo;
import x.transport.SenderReceiver;
import x.udp.DatagramSenderReceiver;

public class Main {

	public static void main(String[] args) {
		SenderReceiver sr = new DatagramSenderReceiver(13371);
		Challenge mboat = Challenge.Maybe(sr.start(), 
			new Func1<SenderReceiver, Challenge>() {
				public Challenge func(SenderReceiver dsr) {
					try {
						Cargo c = new Cargo(0L, Boat.class, new Boat());
						return dsr.send(c, InetAddress.getLocalHost(), 13370);
					} catch (UnknownHostException e) {
						return Challenge.Failure(ExceptionExtension.stringnify(e));
					}
				}
		});
		System.out.println(mboat);

	}

}
