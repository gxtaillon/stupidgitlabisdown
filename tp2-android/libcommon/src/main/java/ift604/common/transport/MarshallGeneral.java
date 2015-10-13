package ift604.common.transport;

import java.io.Serializable;

import gxt.common.Challenge;
import gxt.common.Func1;
import ift604.common.dispatch.ContainerContainer;
import ift604.common.dispatch.Dispatcher;

public class MarshallGeneral <Tc extends ContainerContainer & Serializable> {
	protected Dispatcher<Tc> dispatcher;
	protected SenderReceiver senderReceiver;
	protected Class<Tc> containerContainerClass;
	protected Challenge active;
	public MarshallGeneral(Class<Tc> c, Dispatcher<Tc> dispatcher, SenderReceiver senderReceiver) { 
		this.dispatcher = dispatcher; 
		this.senderReceiver = senderReceiver;
		this.containerContainerClass = c;
		this.active = Challenge.Failure("not started yet");
	}
	
	public Challenge setActive(Challenge active) {
		Challenge previous = this.active;
		this.active = active;
		return previous;
	}
	
	public Challenge start() {
		return Challenge.Maybe(senderReceiver.start(), new Func1<SenderReceiver, Challenge>() {
			public Challenge func(SenderReceiver sr) {
				active = Challenge.Success("Started");
				while (active.isSuccess()) {
					Challenge.Maybe(sr.receive(containerContainerClass), new Func1<Tc, Challenge>() {
						public Challenge func(Tc a) {
							return dispatcher.receive(a);
						}
					});
				}
				return active;
			}
		
		});
	}
}
