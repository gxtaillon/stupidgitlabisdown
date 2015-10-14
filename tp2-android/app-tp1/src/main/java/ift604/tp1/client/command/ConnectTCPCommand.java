package ift604.tp1.client.command;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;
import gxt.common.lispite.Command;
import ift604.common.dispatch.Dispatcher;
import ift604.common.tcp.StreamSenderReceiver;
import ift604.common.transport.Cargo;
import ift604.common.transport.MarshallGeneral;
import ift604.common.transport.StreamSender;
import ift604.tp1.client.State;

/**
 * Created by taig1501 on 15-10-14.
 */
public class ConnectTCPCommand implements Command {
    State state;
    int remotePort;
    String remoteHost;

    public ConnectTCPCommand(State state, int remotePort, String remoteHost) {
        this.state = state;
        this.remotePort = remotePort;
        this.remoteHost = remoteHost;
    }

    @Override
    public Maybe<Object> func() {
        try {
            state.setTcpSenderReceiver(
                    new StreamSenderReceiver(InetAddress.getByName(remoteHost),remotePort));

            return state.getTcpSenderReceiver().connect().bind(new Func1<StreamSender, Maybe<Object>>() {
                @Override
                public Maybe<Object> func(StreamSender a) {
                    state.setTcpThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ExecutorService pool = Executors.newCachedThreadPool();
                            state.setTcpMarshallGeneral(new MarshallGeneral<Cargo>(Cargo.class, state.getDispatcher(), state.getTcpSenderReceiver(), pool));
                            System.out.println(state.getTcpMarshallGeneral().start());
                        }
                    }));
                    state.getTcpThread().start();
                    return Maybe.<Object>Just(a, "connected");
                }
            });
        } catch (UnknownHostException e) {
            return Maybe.<Object>Nothing(ExceptionExtension.stringnify(e));
        }
    }
}
