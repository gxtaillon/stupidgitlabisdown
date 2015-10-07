package x.udp.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;
import x.transport.Boat;
import x.udp.DatagramSenderReceiver;

public class Main {

	public static void main(String[] args) {
		Challenge mboat = Challenge.Maybe(DatagramSenderReceiver.Start(13371), 
			new Func1<DatagramSenderReceiver, Challenge>() {
				public Challenge func(DatagramSenderReceiver dsr) {
					try {
						return dsr.send(new Boat(), InetAddress.getLocalHost(), 13370);
					} catch (UnknownHostException e) {
						return Challenge.Failure(ExceptionExtension.stringnify(e));
					}
				}
		});
		System.out.println(mboat);

	}

}
