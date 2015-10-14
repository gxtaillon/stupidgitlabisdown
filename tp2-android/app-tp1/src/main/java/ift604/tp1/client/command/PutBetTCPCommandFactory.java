package ift604.tp1.client.command;

import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;
import gxt.common.lispite.Command;
import gxt.common.lispite.CommandFactory;
import gxt.common.lispite.TokenGroup;
import ift604.tp1.client.State;

public class PutBetTCPCommandFactory implements CommandFactory {
	private State state;

	public PutBetTCPCommandFactory(State state) {
		this.state = state;
	}

	@Override
	public Maybe<Command> make(TokenGroup group) {
		if (group.getGroup().length == 3) {
			try {
				return Maybe.<Command>Just(
						new PutBetTCPCommand(
								state,
								Integer.parseInt(group.getGroup()[0].getName()),
								Integer.parseInt(group.getGroup()[1].getName()),
								Double.parseDouble(group.getGroup()[2].getName())),
						"made PutBetTCP command");
			} catch (NumberFormatException e) {
				return Maybe.Nothing(ExceptionExtension.stringnify(e));
			}
		} else {
			return Maybe.Nothing("PutBetTCP command expected arguments MatchId, UserId, Amount :: Double");
		}
	}
}
