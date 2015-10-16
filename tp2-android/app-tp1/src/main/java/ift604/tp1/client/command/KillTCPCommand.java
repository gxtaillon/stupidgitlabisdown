package ift604.tp1.client.command;

import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;
import gxt.common.lispite.Command;
import ift604.tp1.client.State;

public class KillTCPCommand implements Command {
	State state;

	public KillTCPCommand(State state) {
		this.state = state;
	}

	public Maybe<Object> func() {
		state.getTcpMarshallGeneral().stop();
		try {
			state.getTcpThread().join(25000);
			return Maybe.<Object>Just(this, "stopped tcp thread");
		} catch (InterruptedException e) {
			return Maybe.<Object>Nothing(ExceptionExtension.stringnify(e));
		}
	}

}
