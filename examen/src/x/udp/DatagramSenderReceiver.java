package x.udp;

import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import x.transport.Marshall;
import x.transport.SenderReceiver;

public class DatagramSenderReceiver implements SenderReceiver {
	DatagramSocket ds;
	int listenPort;
	
	public DatagramSenderReceiver(int listenPort) {
		this.listenPort = listenPort;
	}
	
	@Override
	public Maybe<SenderReceiver> start() {
		try {
			ds = new DatagramSocket(listenPort);
			return Maybe.<SenderReceiver>Just(this, "DatagramSenderReceiver ready");
		} catch (SocketException e) {
			return Maybe.<SenderReceiver>Nothing(ExceptionExtension.stringnify(e));
		}
	}
	
	@Override
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
	
	@Override
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
