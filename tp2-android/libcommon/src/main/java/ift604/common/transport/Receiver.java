package ift604.common.transport;

import gxt.common.Act1;
import gxt.common.Act2;
import gxt.common.Challenge;
import gxt.common.Func0;
import gxt.common.Func1;
import gxt.common.Maybe;

import java.io.Serializable;
import java.net.InetAddress;

public interface Receiver {

	public <Ta extends Serializable> Challenge sendClose(Ta a);
	public boolean canAccept();
	public <Ta extends Serializable> Maybe<Act1<Class<Ta>>> accept(final Act1<Maybe<Receipt<Ta>>> onReceive);
	public Maybe<Receiver> start();
	public Challenge stop();
}