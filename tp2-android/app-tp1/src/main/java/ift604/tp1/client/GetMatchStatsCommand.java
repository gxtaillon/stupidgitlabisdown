package ift604.tp1.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;
import gxt.common.lispite.Command;
import ift604.common.cargo.MatchList;
import ift604.common.cargo.MatchStats;
import ift604.common.models.ListeDesMatchs;
import ift604.common.models.Match;
import ift604.common.transport.Cargo;
import ift604.common.transport.SenderReceiver;

public class GetMatchStatsCommand implements Command {
	private SenderReceiver sr;
	private String host;
	private int port;

	public GetMatchStatsCommand(SenderReceiver sr, String host, int port) {
		this.sr = sr;
		this.host = host;
		this.port = port;
	}

	public Maybe<Object> func() {
		try {
			Match requestedMatch = ListeDesMatchs.getInstance().getMatch(0);
			Cargo c = new Cargo(0L, MatchStats.class, new MatchStats(requestedMatch));
			System.out.println("debug: " + host + ":" + port);
			Challenge sendResult = sr.send(c, InetAddress.getByName(host), port);
			return Maybe.Challenge(sendResult, new Func1<String, Maybe<Object>>() {
				@Override
				public Maybe<Object> func(String a) {
					return Maybe.<Object> Just(this, "sent GetMatchList");
				}
			});
		} catch (UnknownHostException e) {
			return Maybe.<Object>Nothing(ExceptionExtension.stringnify(e));
		}
	}

}
