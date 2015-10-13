package ift604.common.dispatch;

import java.io.Serializable;

import gxt.common.Challenge;
import ift604.common.transport.Receipt;

public interface Dispatcher<Tc extends ContainerContainer & Serializable> {

	public abstract void addReceiver(Class<?> c, Receiver<Tc> cr);

	public abstract Challenge receive(Receipt<Tc> c);

}