package ift604.common.dispatch;

import java.io.Serializable;

import ift604.common.transport.Receipt;

public interface Receiver <Ta extends Serializable> {

	public abstract void receive(Receipt<Ta> c);

}