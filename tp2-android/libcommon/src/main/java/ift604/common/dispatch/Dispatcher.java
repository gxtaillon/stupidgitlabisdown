package ift604.common.dispatch;

import java.io.Serializable;

import gxt.common.Act1;
import gxt.common.Challenge;
import ift604.common.transport.Receipt;

public interface Dispatcher<Tc extends ContainerContainer & Serializable> {

	public abstract void addReceiver(Class<?> c, Act1<Receipt<Tc>> cr);

	public abstract Challenge dispatch(Receipt<Tc> c);

}