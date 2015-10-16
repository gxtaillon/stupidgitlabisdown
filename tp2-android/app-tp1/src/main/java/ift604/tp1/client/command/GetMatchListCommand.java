package ift604.tp1.client.command;

import java.net.InetAddress;
import java.net.UnknownHostException;

import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.extension.ExceptionExtension;
import gxt.common.lispite.Command;
import ift604.common.cargo.Cargo;
import ift604.common.cargo.GetMatchList;
import ift604.common.transport.DatagramSender;

public class GetMatchListCommand implements Command {
	private DatagramSender sr;
	private String host;
	private int port;

	public GetMatchListCommand(DatagramSender sr, String host, int port) {
		this.sr = sr;
		this.host = host;
		this.port = port;
	}

	public Maybe<Object> func() {
		try {
			Cargo c = new Cargo(0L, GetMatchList.class, new GetMatchList());
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
