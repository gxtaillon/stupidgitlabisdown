package ift604.common.tcp;

import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Maybe;
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

    private StreamReceiver() {}

    public static Maybe<StreamReceiver> newStreamSenderReceiver(int listenPort) {
        try {
            StreamReceiver ssr = new StreamReceiver();
            ssr.listenPort = listenPort;
            ssr.ss = new ServerSocket(listenPort);
            return Maybe.<StreamReceiver>Just(ssr, "created new StreamReceiver");
        } catch (IOException e) {
            return Maybe.<StreamReceiver>Nothing(ExceptionExtension.stringnify(e));
        }
    }

    @Override
    public Maybe<Receiver> start() {
        try {
            ss.bind(new InetSocketAddress("localhost", listenPort));
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
    public <Ta extends Serializable> Maybe<Receipt<Ta>> receive(Class<Ta> ac) {
        try {
            byte[] buf = new byte[1024];
            final Socket s = ss.accept();
            s.getInputStream().read(buf);
            return Marshall.fromBytes(buf, ac).bind(new Func1<Ta, Maybe<Receipt<Ta>>>() {
                @Override
                public Maybe<Receipt<Ta>> func(Ta a) {
                    final Receipt<Ta> receipt = new Receipt<Ta>(a, s.getInetAddress(), s.getPort(), new Func1<Ta, Challenge>() {
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
                    });
                    return Maybe.Just(receipt, "received");
                }
            });
        } catch (IOException e) {
            return Maybe.<Receipt<Ta>>Nothing(ExceptionExtension.stringnify(e));
        }
    }
}
