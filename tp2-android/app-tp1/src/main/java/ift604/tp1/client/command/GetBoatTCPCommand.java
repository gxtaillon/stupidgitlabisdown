package ift604.tp1.client.command;

import java.net.InetAddress;
import java.net.UnknownHostException;

import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;
import gxt.common.lispite.Command;
import ift604.common.cargo.GetBoat;
import ift604.common.transport.Cargo;
import ift604.common.transport.DatagramSender;
import ift604.tp1.client.State;

public class GetBoatTCPCommand implements Command {
	State state;

	public GetBoatTCPCommand(State state) {
		this.state = state;
	}

	public Maybe<Object> func() {
		return Maybe.Challenge(
				state.getTcpSenderReceiver().send(new Cargo(0L, GetBoat.class, new GetBoat())),
				new Func1<String, Maybe<Object>>() {
					@Override
					public Maybe<Object> func(String a) {
						return Maybe.<Object>Just(this, a);
					}
				});
	}

}
