package ift604.tp1.client;

import gxt.common.Maybe;
import gxt.common.lispite.Command;
import gxt.common.lispite.CommandFactory;
import gxt.common.lispite.TokenGroup;
import ift604.common.transport.DatagramSender;

public class GetMarchStatsCommandFactory implements CommandFactory {
	private DatagramSender sr;

	public GetMarchStatsCommandFactory(DatagramSender sr) {
		this.sr = sr;
	}

	@Override
	public Maybe<Command> make(TokenGroup group) {
		if (group.getGroup().length == 2) {
			return Maybe.<Command> Just(
					new GetMatchListCommand(sr, group.getGroup()[0].getName(), Integer.parseInt(group.getGroup()[1].getName())),
					"made GetMatchList command");
		} else {
			return Maybe
					.<Command> Nothing("GetMatchList command expected first argument to be host and second to be port");
		}
	}
}
