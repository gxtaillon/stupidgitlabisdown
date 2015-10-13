package ift604.common.udp;

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

import ift604.common.transport.Marshall;
import ift604.common.transport.Receipt;
import ift604.common.transport.SenderReceiver;

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
	public <Ta extends Serializable> Maybe<Receipt<Ta>> receive(Class<Ta> ac) {
		byte[] buf = new byte[1024];
		final DatagramPacket dp = new DatagramPacket(buf, buf.length);
		try {
			ds.receive(dp);
			return Marshall.fromBytes(buf, ac).bind(new Func1<Ta, Maybe<Receipt<Ta>>>() {
				@Override
				public Maybe<Receipt<Ta>> func(Ta a) {

					final Receipt<Ta> receipt = new Receipt<Ta>(a, dp.getAddress(), dp.getPort(), new Func1<Ta, Challenge>() {
						@Override
						public Challenge func(Ta response) {
							Maybe<byte[]> mbuf = Marshall.toBytes(response);
							return Challenge.Maybe(mbuf, new Func1<byte[], Challenge>() {
								public Challenge func(byte[] buf) {
									dp.setData(buf);
									try {
										ds.send(dp);
										return Challenge.Success("datagram packet reply sent");
									} catch (IOException e) {
										return Challenge.Failure(ExceptionExtension.stringnify(e));
									}
								}
							});
						}
					});
					return Maybe.Just(receipt, "received");
				}
			});
		} catch (IOException e) {
			return Maybe.<Receipt<Ta>>Nothing(ExceptionExtension.stringnify(e));
		}
	}
}
