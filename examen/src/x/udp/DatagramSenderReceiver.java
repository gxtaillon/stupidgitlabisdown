package x.udp;

import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.Tup0;
import gxt.common.extension.ExceptionExtension;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import x.transport.Marshall;

public class DatagramSenderReceiver {
	DatagramSocket ds;
	
	protected DatagramSenderReceiver() {}
	
	public static Maybe<DatagramSenderReceiver> Start(int listenPort) {
		try {
			DatagramSenderReceiver r = new DatagramSenderReceiver();
			r.ds = new DatagramSocket(listenPort);
			return Maybe.<DatagramSenderReceiver>Just(r, "sender ready");
		} catch (SocketException e) {
			return Maybe.<DatagramSenderReceiver>Nothing(ExceptionExtension.stringnify(e));
		}
	}
	
	public <Ta extends Serializable> Challenge send(Ta a, final InetAddress sendAddr, final int sendPort) {
		Maybe<byte[]> mbuf = Marshall.toBytes(a);
		return Challenge.Maybe(mbuf, new Func1<byte[], Challenge>() {
			public Challenge func(byte[] buf) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length, sendAddr, sendPort);
				try {
					ds.send(dp);
					return Challenge.Success("datagram packet sent");
				} catch (IOException e) {
					return Challenge.Failure(ExceptionExtension.stringnify(e));
				}
			}
		});
	}
	
	public <Ta extends Serializable> Maybe<Ta> receive(Class<Ta> ac) {
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		try {
			ds.receive(dp);
			return Marshall.fromBytes(buf, ac);
		} catch (IOException e) {
			return Maybe.<Ta>Nothing(ExceptionExtension.stringnify(e));
		}
	}
}
