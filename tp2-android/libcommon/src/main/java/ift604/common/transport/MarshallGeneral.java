package ift604.common.transport;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import gxt.common.Act1;
import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.MaybeBase;
import gxt.common.Monad;
import gxt.common.Tup0;
import ift604.common.dispatch.ContainerContainer;
import ift604.common.dispatch.Dispatcher;

public class MarshallGeneral <Tc extends ContainerContainer & Serializable> {
	protected Dispatcher<Tc> dispatcher;
	protected Receiver receiver;
	protected Class<Tc> containerContainerClass;
	protected ExecutorService pool;
	protected Challenge active;
	public MarshallGeneral(Class<Tc> c, Dispatcher<Tc> dispatcher, Receiver receiver, ExecutorService pool) {
		this.dispatcher = dispatcher; 
		this.receiver = receiver;
		this.containerContainerClass = c;
		this.pool = pool;
		this.active = Challenge.Failure("not started yet");
	}
	
	public Challenge stop() {
		active = Challenge.Failure("asked to stop");
		return receiver.stop();
	}
	
	public Challenge start() {
        return Challenge.Maybe(receiver.start(), new Func1<Receiver, Challenge>() {
			public Challenge func(Receiver sr) {
				active = Challenge.Success("started");
				while (active.isSuccess()) {

					System.out.println("debug: mg receiving...");
					Maybe<Tup0> m = sr.accept(new Act1<Maybe<Receipt<Tc>>>() {
						// onReceive
						@Override
						public void func(Maybe<Receipt<Tc>> receiptMaybe) {
							receiptMaybe.bind(new Func1<Receipt<Tc>, Maybe<Tup0>>() {
								@Override
								public Maybe<Tup0> func(final Receipt<Tc> receipt) {
									pool.execute(new Runnable() {
										@Override
										public void run() {
											dispatcher.dispatch(receipt);
										}
									});
									return Maybe.<Tup0>Just(Tup0.Tup(), "done");
								}
							});
						}
					}).bind(new Func1<Act1<Class<Tc>>, Maybe<Tup0>>() {
						@Override
						public Maybe<Tup0> func(Act1<Class<Tc>> doReceive) {
							doReceive.func(containerContainerClass);
							return Maybe.Just(Tup0.Tup(), "accepted new client");
						}
					});
					if (!m.isJust()) {
						return Challenge.Failure(m.why());
					}
				}
				pool.shutdown();
				try {
					if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
						pool.shutdownNow();
						if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
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
