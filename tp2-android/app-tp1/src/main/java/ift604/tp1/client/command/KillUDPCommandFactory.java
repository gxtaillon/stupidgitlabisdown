package ift604.tp1.client.command;

import gxt.common.Maybe;
import gxt.common.lispite.Command;
import gxt.common.lispite.CommandFactory;
import gxt.common.lispite.TokenGroup;
import ift604.tp1.client.State;

public class KillUDPCommandFactory implements CommandFactory {
	private State state;

	public KillUDPCommandFactory(State state) {
		this.state = state;
	}

	@Override
	public Maybe<Command> make(TokenGroup group) {
		if (group.getGroup().length == 0) {
			return Maybe.<Command> Just(
					new KillUDPCommand(state),
					"made KillUDP command");
		} else {
			return Maybe
					.<Command> Nothing("KillUDP command expected no arguments");
		}
	}
}
