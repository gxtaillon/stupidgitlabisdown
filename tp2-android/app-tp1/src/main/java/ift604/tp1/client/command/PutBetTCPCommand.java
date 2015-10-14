package ift604.tp1.client.command;

import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.lispite.Command;
import ift604.common.cargo.GetBoat;
import ift604.common.cargo.PutBet;
import ift604.common.transport.Cargo;
import ift604.tp1.client.State;

public class PutBetTCPCommand implements Command {
	State state;
	Integer matchId;
	Integer userId;
	Double amount;

	public PutBetTCPCommand(State state, Integer matchId, Integer userId, Double amount) {
		this.state = state;
		this.matchId = matchId;
		this.userId = userId;
		this.amount = amount;
	}

	public Maybe<Object> func() {
		return Maybe.Challenge(
				state.getTcpSenderReceiver().send(new Cargo(0L, PutBet.class, new PutBet(matchId, userId, amount))),
				new Func1<String, Maybe<Object>>() {
					@Override
					public Maybe<Object> func(String a) {
						return Maybe.<Object>Just(this, a);
					}
				});
	}

}
