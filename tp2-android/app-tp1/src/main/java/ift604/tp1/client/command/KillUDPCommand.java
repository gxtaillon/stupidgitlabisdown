package ift604.tp1.client.command;

import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;
import gxt.common.lispite.Command;
import ift604.tp1.client.State;

public class KillUDPCommand implements Command {
	State state;

	public KillUDPCommand(State state) {
		this.state = state;
	}

	public Maybe<Object> func() {
		state.getUdpMarshallGeneral().stop();
		try {
			state.getUdpThread().join(25000);
			return Maybe.<Object>Just(this, "stopped udp thread");
		} catch (InterruptedException e) {
			return Maybe.<Object>Nothing(ExceptionExtension.stringnify(e));
		}
	}

}
