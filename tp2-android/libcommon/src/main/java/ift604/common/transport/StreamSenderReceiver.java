package ift604.common.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import gxt.common.Act1;
import gxt.common.Challenge;
import gxt.common.Func0;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;

/**
 * Created by taig1501 on 15-10-14.
 */
public class StreamSenderReceiver implements StreamSender, Receiver {
    protected static final Func1<StreamSenderReceiver, Maybe<StreamSender>> bindSender = new Func1<StreamSenderReceiver, Maybe<StreamSender>>() {
        @Override
        public Maybe<StreamSender> func(StreamSenderReceiver a) {
            return Maybe.<StreamSender>Just(a, "init");
        }
    };
    protected static final Func1<StreamSenderReceiver, Maybe<Receiver>> bindReceiver = new Func1<StreamSenderReceiver, Maybe<Receiver>>() {
        @Override
        public Maybe<Receiver> func(StreamSenderReceiver a) {
            return Maybe.<Receiver>Just(a, "init");
        }
    };

    protected final Func1<Serializable, Challenge> sendHandler = new Func1<Serializable, Challenge>() {
        @Override
        public Challenge func(Serializable a) {
            System.out.println("debug: ssr sending...");
            Maybe<byte[]> mbuf = Marshall.toBytes(a);
            return Challenge.Maybe(mbuf, new Func1<byte[], Challenge>() {
                @Override
                public Challenge func(byte[] a) {
                    try {
                        System.out.println("debug: ssr writing...");

                        OutputStream os = socket.getOutputStream();
                        os.write(a);
                        os.flush();

                        System.out.println("debug: ssr sent");
                        return Challenge.Success("socket packet sent");
                    } catch (IOException e) {
                        return Challenge.Failure(ExceptionExtension.stringnify(e));
                    }
                }
            });
        }
    };

    protected final Func0<Challenge> closeHandler = new Func0<Challenge>() {
        @Override
        public Challenge func() {
            try {
                socket.close();
                return Challenge.Success("socket closed");
            } catch (IOException e) {
                return Challenge.Failure(ExceptionExtension.stringnify(e));
            }
        }
    };

    protected Socket socket;
    protected InetAddress sendAddr;
    protected int sendPort;

    public StreamSenderReceiver(InetAddress sendAddr, int sendPort) {
        this.sendAddr = sendAddr;
        this.sendPort = sendPort;
    }

    @Override
    public Maybe<StreamSender> connect() {
        if (socket != null) {
            return initCheck().bind(bindSender);
        } else {
            return init().bind(bindSender);
        }
    }

    @Override
    public Maybe<Receiver> start() {
        if (socket != null) {
            return initCheck().bind(bindReceiver);
        } else {
            return init().bind(bindReceiver);
        }
    }

    protected Maybe<StreamSenderReceiver> initCheck() {
        //if (socket.isConnected() && socket.isBound()) {
            return Maybe.<StreamSenderReceiver>Just(this, "init");
      /*  } else if (socket.isClosed()) {
            return Maybe.<StreamSenderReceiver>Nothing("socket is closed, can't start again");
        } else {
            return Maybe.<StreamSenderReceiver>Nothing("something is wrong with that socket");
        }
        //*/
    }

    protected Maybe<StreamSenderReceiver> init() {
        try {
            socket = new Socket();
            socket.bind(null);
            socket.connect(new InetSocketAddress(sendAddr, sendPort));
            socket.setKeepAlive(true);
            return Maybe.<StreamSenderReceiver>Just(this, "StreamSenderReceiver ready");
        } catch (IOException e) {
            return Maybe.<StreamSenderReceiver>Nothing(ExceptionExtension.stringnify(e));
        }
    }

    @Override
    public Challenge stop() {
        try {
            socket.close();
            return Challenge.Success("stopped");
        } catch (IOException e) {
            return Challenge.Failure(ExceptionExtension.stringnify(e));
        }
    }

    @Override
    public <Ta extends Serializable> Challenge send(Ta a) {
        return sendHandler.func(a);
    }

    @Override
    public <Ta extends Serializable> Maybe<Act1<Class<Ta>>> accept(final Act1<Maybe<Receipt<Ta>>> onReceive) {
        return Maybe.<Act1<Class<Ta>>>Just(new Act1<Class<Ta>>() {
            @Override
            public void func(final Class<Ta> ac) {
                while (!socket.isClosed()) {
                    Maybe<Receipt<Ta>> receiptMaybe = new Func0<Maybe<byte[]>>() {
                        @Override
                        public Maybe<byte[]> func() {
                            try {
                                System.out.println("debug: ssr reading...");
                                InputStream is = socket.getInputStream();
                                byte[] buf = new byte[1024];
                                is.read(buf);
                                return Maybe.<byte[]>Just(buf, "read incoming");
                            } catch (IOException e) {
                                return Maybe.Nothing(ExceptionExtension.stringnify(e));
                            }
                        }
                    }.func().bind(new Func1<byte[], Maybe<Receipt<Ta>>>() {
                        @Override
                        public Maybe<Receipt<Ta>> func(byte[] buf) {
                            System.out.println("debug: ssr mashalling...");
                            return Marshall.fromBytes(buf, ac).bind(new Func1<Ta, Maybe<Receipt<Ta>>>() {
                                @Override
                                public Maybe<Receipt<Ta>> func(Ta a) {
                                    Receipt<Ta> receipt = new Receipt<Ta>(a, socket.getInetAddress(), socket.getPort(), (Func1<Ta, Challenge>) sendHandler, closeHandler);
                                    System.out.println("debug: ssr received.");
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
