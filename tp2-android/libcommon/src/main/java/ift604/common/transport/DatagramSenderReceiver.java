package ift604.common.transport;

import gxt.common.Act1;
import gxt.common.Challenge;
import gxt.common.Func0;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import ift604.common.transport.*;
import ift604.common.transport.DatagramSender;

public class DatagramSenderReceiver implements Receiver, DatagramSender {
	DatagramSocket ds;
	int listenPort;
	
	public DatagramSenderReceiver(int listenPort) {
		this.listenPort = listenPort;
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
	public Maybe<Receiver> start() {
		try {
			ds = new DatagramSocket(listenPort);
			return Maybe.<Receiver>Just(this, "DatagramSenderReceiver ready");
		} catch (SocketException e) {
			return Maybe.<Receiver>Nothing(ExceptionExtension.stringnify(e));
		}
	}

	@Override
	public Challenge stop() {
		ds.close();
		return Challenge.Success("stopped");
	}

    @Override
    public <Ta extends Serializable> Challenge sendClose(Ta a) {
        return Challenge.Success("\"closed\"");
    }

    // requires new handler for each packet
    private <Ta extends Serializable> Func1<Ta, Challenge> getReplyHandler(final DatagramPacket dp) {
        return new Func1<Ta, Challenge>() {
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
        };
    }

    private Func0<Challenge> getCloseHandler(final DatagramPacket dp) {
        return new Func0<Challenge>() {
            @Override
            public Challenge func() {
                return Challenge.Success("datagram packet \"closed\"");
            }
        };
    }

    @Override
    public boolean canAccept() {
        return !ds.isClosed() && ds.isConnected();
    }

    @Override
	public <Ta extends Serializable> Maybe<Act1<Class<Ta>>> accept(final Act1<Maybe<Receipt<Ta>>> onReceive) {
        final DatagramSenderReceiver dsrThis = this;
		return Maybe.<Act1<Class<Ta>>>Just(new Act1<Class<Ta>>() {
			@Override
			public void func(final Class<Ta> ac) {
				while (!ds.isClosed()) {
					Maybe<Receipt<Ta>> receiptMaybe = new Func0<Maybe<DatagramPacket>>() {
						@Override
						public Maybe<DatagramPacket> func() {
							try {
								System.out.println("debug: dsr reading...");
                                final byte[] buf = new byte[1024];
                                final DatagramPacket dp = new DatagramPacket(buf, buf.length);
								ds.receive(dp);
								return Maybe.<DatagramPacket>Just(dp, "read incoming");
							} catch (IOException e) {
								return Maybe.Nothing(ExceptionExtension.stringnify(e));
							}
						}
					}.func().bind(new Func1<DatagramPacket, Maybe<Receipt<Ta>>>() {
						@Override
						public Maybe<Receipt<Ta>> func(final DatagramPacket dp) {
							System.out.println("debug: dsr mashalling...");
                            final Func1<Ta,Challenge> sendHandler = dsrThis.<Ta>getReplyHandler(dp);
                            final Func0<Challenge> closeHandler = dsrThis.getCloseHandler(dp);
							return Marshall.fromBytes(dp.getData(), ac).bind(new Func1<Ta, Maybe<Receipt<Ta>>>() {
								@Override
								public Maybe<Receipt<Ta>> func(Ta a) {
									final Receipt<Ta> receipt = new Receipt<Ta>(a, dp.getAddress(), dp.getPort(), sendHandler, closeHandler);
									System.out.println("debug: dsr received.");
									return Maybe.Just(receipt, "received");
								}
							});
						}
					});
					onReceive.func(receiptMaybe);
				}
			}


        }, "no accept to do");

	}
}
