package ift604.tp1.client.command;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;
import gxt.common.lispite.Command;
import ift604.common.cargo.Cargo;
import ift604.common.cargo.GetMatchList;
import ift604.common.transport.DatagramSender;
import ift604.tp1.client.State;

public class StartAutoGetMatchListCommand implements Command {
	private State state;
	private String host;
	private int port;

	public StartAutoGetMatchListCommand(State state, String host, int port) {
		this.state = state;
		this.host = host;
		this.port = port;
	}

	public Maybe<Object> func() {
		state.setAutoUpdateTimer(new Timer());
		state.getAutoUpdateTimer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new GetMatchListCommand(state.getUdpSenderReceiver(), host, port).func();
            }
        }, 0, 2);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (state.getAutoUpdateTimer() != null) {
                    state.getAutoUpdateTimer().cancel();
                }
            }
        }));
		return Maybe.<Object>Just(this, "started auto update");
	}

}
