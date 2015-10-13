package ift604.common.transport;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.Tup0;
import gxt.common.extension.ExceptionExtension;
import ift604.common.dispatch.ContainerContainer;
import ift604.common.dispatch.Dispatcher;

public class MarshallGeneral <Tc extends ContainerContainer & Serializable> {
	protected Dispatcher<Tc> dispatcher;
	protected SenderReceiver senderReceiver;
	protected Class<Tc> containerContainerClass;
	protected ExecutorService pool;
	protected Challenge active;
	public MarshallGeneral(Class<Tc> c, Dispatcher<Tc> dispatcher, SenderReceiver senderReceiver, ExecutorService pool) {
		this.dispatcher = dispatcher; 
		this.senderReceiver = senderReceiver;
		this.containerContainerClass = c;
		this.pool = pool;
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
					sr.receive(containerContainerClass).bind(new Func1<Receipt<Tc>, Maybe<Tup0>>() {
						public Maybe<Tup0> func(final Receipt<Tc> a) {
							pool.execute(new Runnable() {
								@Override
								public void run() {
									dispatcher.receive(a);
								}
							});
							return Maybe.<Tup0>Just(Tup0.Tup(), "done");
						}
					});
				}
				pool.shutdown();
				try {
					if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
						pool.shutdownNow();
						if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
							return Challenge.Failure("pool did not terminate properly");
						}
					}
					return active;
				} catch (InterruptedException ie) {
					pool.shutdownNow();
					Thread.currentThread().interrupt();
					return Challenge.Failure("pool did not terminate properly");
				}
			}
		});
	}
}
