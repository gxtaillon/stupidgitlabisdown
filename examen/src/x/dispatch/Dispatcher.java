package x.dispatch;

import gxt.common.Challenge;

public interface Dispatcher<Tc extends ContainerContainer> {

	public abstract void addReceiver(Class<?> c, Receiver<Tc> cr);

	public abstract Challenge receive(Tc c);

}