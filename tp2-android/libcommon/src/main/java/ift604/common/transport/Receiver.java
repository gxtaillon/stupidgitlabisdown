package ift604.common.transport;

import gxt.common.Challenge;
import gxt.common.Maybe;

import java.io.Serializable;
import java.net.InetAddress;

public interface Receiver {

	public <Ta extends Serializable> Maybe<Receipt<Ta>> receive(Class<Ta> ac);

	public Maybe<Receiver> start();

}