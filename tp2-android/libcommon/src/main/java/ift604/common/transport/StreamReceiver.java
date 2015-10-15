package ift604.common.transport;

import gxt.common.Act0;
import gxt.common.Act1;
import gxt.common.Challenge;
import gxt.common.Func0;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.MaybeBase;
import gxt.common.Monad;
import gxt.common.extension.ExceptionExtension;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import ift604.common.transport.Marshall;
import ift604.common.transport.Receipt;
import ift604.common.transport.Receiver;

public class StreamReceiver implements Receiver {
    ServerSocket ss;
    int listenPort;

    public StreamReceiver(int listenPort) {
        this.listenPort = listenPort;
    }

    @Override
    public Maybe<Receiver> start() {
        try {
            ss = new ServerSocket(listenPort);
            return Maybe.<Receiver>Just(this, "StreamReceiver ready");
        } catch (UnknownHostException e) {
            return Maybe.<Receiver>Nothing(ExceptionExtension.stringnify(e));
        } catch (SocketException e) {
            return Maybe.<Receiver>Nothing(ExceptionExtension.stringnify(e));
        } catch (IOException e) {
            return Maybe.<Receiver>Nothing(ExceptionExtension.stringnify(e));
        }
    }

    @Override
    public Challenge stop() {
        try {
            ss.close();
            return Challenge.Success("stopped");
        } catch (IOException e) {
            return Challenge.Failure(ExceptionExtension.stringnify(e));
        }
    }

    private <Ta extends Serializable> Func1<Ta, Challenge> getSendHandler(final Socket s) {
        return new Func1<Ta, Challenge>() {
            @Override
            public Challenge func(Ta response) {
                Maybe<byte[]> mbuf = Marshall.toBytes(response);
                return Challenge.Maybe(mbuf, new Func1<byte[], Challenge>() {
                    public Challenge func(byte[] buf) {
                        try {
                            OutputStream os = s.getOutputStream();
                            os.write(buf);
                            os.flush();
                            return Challenge.Success("socket reply sent");
                        } catch (IOException e) {
                            return Challenge.Failure(ExceptionExtension.stringnify(e));
                        }
                    }
                });
            }
        };
    }

    private Func0<Challenge> getCloseHandler(final Socket s) {
        return new Func0<Challenge>() {
            @Override
            public Challenge func() {
                try {
                    s.close();
                    return Challenge.Success("socket closed");
                } catch (IOException e) {
                    return Challenge.Failure(ExceptionExtension.stringnify(e));
                }
            }
        };
    }

    @Override
    public <Ta extends Serializable> Maybe<Act1<Class<Ta>>> accept(final Act1<Maybe<Receipt<Ta>>> onReceive) {
        final StreamReceiver srThis = this;
        try {
            System.out.println("debug: sr accepting...");
            final Socket s = ss.accept();
            return Maybe.<Act1<Class<Ta>>>Just(new Act1<Class<Ta>>() {
                @Override
                public void func(final Class<Ta> ac) {
                    while (!s.isClosed()) {
                        Maybe<Receipt<Ta>> receiptMaybe = new Func0<Maybe<byte[]>>() {
                            @Override
                            public Maybe<byte[]> func() {
                                try {
                                    byte[] buf = new byte[1024];
                                    System.out.println("debug: sr reading...");
                                    s.getInputStream().read(buf);
                                    return Maybe.<byte[]>Just(buf, "read incoming");
                                } catch (IOException e) {
                                    return Maybe.Nothing(ExceptionExtension.stringnify(e));
                                }
                            }
                        }.func().bind(new Func1<byte[], Maybe<Receipt<Ta>>>() {
                            @Override
                            public Maybe<Receipt<Ta>> func(byte[] buf) {
                                System.out.println("debug: sr marhsalling...");
                                final Func1<Ta, Challenge> sendHandler = srThis.<Ta>getSendHandler(s);
                                final Func0<Challenge> closeHandler = srThis.<Ta>getCloseHandler(s);
                                return Marshall.fromBytes(buf, ac).bind(new Func1<Ta, Maybe<Receipt<Ta>>>() {
                                    @Override
                                    public Maybe<Receipt<Ta>> func(Ta a) {
                                        final Receipt<Ta> receipt = new Receipt<Ta>(a, s.getInetAddress(), s.getPort(), sendHandler, closeHandler);

                                        System.out.println("debug: sr received");
                                        return Maybe.Just(receipt, "received");
                                    }
                                });
                            }
                        });

                        onReceive.func(receiptMaybe);
                    }
                }
            }, "accepted");
        } catch (IOException e) {
            return Maybe.<Act1<Class<Ta>>>Nothing(ExceptionExtension.stringnify(e));
        }
    }
}
