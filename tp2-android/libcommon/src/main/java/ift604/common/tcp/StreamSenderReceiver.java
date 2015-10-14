package ift604.common.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;
import ift604.common.transport.Marshall;
import ift604.common.transport.Receipt;
import ift604.common.transport.Receiver;
import ift604.common.transport.StreamSender;

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
    protected Func1<Serializable, Challenge> applySend = new Func1<Serializable, Challenge>() {
        @Override
        public Challenge func(Serializable a) {
            Maybe<byte[]> mbuf = Marshall.toBytes(a);
            return Challenge.Maybe(mbuf, new Func1<byte[], Challenge>() {
                @Override
                public Challenge func(byte[] a) {
                    try {
                        OutputStream os = socket.getOutputStream();
                        os.write(a);
                        os.flush();
                        return Challenge.Success("socket packet sent");
                    } catch (IOException e) {
                        return Challenge.Failure(ExceptionExtension.stringnify(e));
                    }
                }
            });
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
        if (socket.isConnected() && socket.isBound()) {
            return Maybe.<StreamSenderReceiver>Just(this, "init");
        } else if (socket.isClosed()) {
            return Maybe.<StreamSenderReceiver>Nothing("socket is closed, can't start again");
        } else {
            return Maybe.<StreamSenderReceiver>Nothing("something is wrong with that socket");
        }
    }

    protected Maybe<StreamSenderReceiver> init() {
        try {
            socket = new Socket();
            socket.bind(null);
            socket.connect(new InetSocketAddress(sendAddr, sendPort));
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
        return applySend.func(a);
    }

    @Override
    public <Ta extends Serializable> Maybe<Receipt<Ta>> receive(Class<Ta> ac) {
        try {
            InputStream is = socket.getInputStream();
            byte[] buf = new byte[1024];
            is.read(buf);
            return Marshall.fromBytes(buf, ac).bind(new Func1<Ta, Maybe<Receipt<Ta>>>() {
                @Override
                public Maybe<Receipt<Ta>> func(Ta a) {
                    Receipt<Ta> receipt = new Receipt<Ta>(a, socket.getInetAddress(), socket.getPort(), (Func1<Ta,Challenge>)applySend);
                    return Maybe.Just(receipt, "received");
                }
            });
        } catch (IOException e) {
            return Maybe.<Receipt<Ta>>Nothing(ExceptionExtension.stringnify(e));
        }
    }
}
