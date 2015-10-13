package ift604.common.transport;

import gxt.common.Challenge;
import gxt.common.Maybe;

import java.io.Serializable;
import java.net.InetAddress;

public interface SenderReceiver {

	public <Ta extends Serializable> Challenge send(Ta a,
			InetAddress sendAddr, int sendPort);

	public <Ta extends Serializable> Maybe<Receipt<Ta>> receive(Class<Ta> ac);

	public Maybe<SenderReceiver> start();

}