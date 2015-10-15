package ift604.common.transport;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import gxt.common.Act1;
import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Maybe;
import gxt.common.MaybeBase;
import gxt.common.Monad;
import gxt.common.Tup0;
import ift604.common.cargo.*;
import ift604.common.dispatch.ContainerContainer;
import ift604.common.dispatch.Dispatcher;

/**
 * The MarshallGeneral class, like its name suggest, gives orders and makes everything work neatly.
 * @param <Tc> The type of container that will be used by the Marshallgeneral to sendClose and receive messages
 */
public class MarshallGeneral <Tc extends ContainerContainer & Serializable> {
	protected Dispatcher<Tc> dispatcher;
	protected Receiver receiver;
	protected Class<Tc> containerContainerClass;
	protected ExecutorService pool;
    protected Tc shutdownContainer;
	protected Challenge active;
    protected Semaphore tasks;
    protected AtomicInteger taskCount;
    protected Semaphore handlers;
    protected AtomicInteger handlerCount;
	public MarshallGeneral(Class<Tc> c, Dispatcher<Tc> dispatcher, final Receiver receiver, Tc shutdownContainer) {
		this.dispatcher = dispatcher; 
		this.receiver = receiver;
		this.containerContainerClass = c;
		this.pool = Executors.newCachedThreadPool();
        this.shutdownContainer = shutdownContainer;
		this.active = Challenge.Failure("not started yet");
        this.tasks = new Semaphore(0);
        this.taskCount = new AtomicInteger(0);
        this.handlers = new Semaphore(0);
        this.handlerCount = new AtomicInteger(0);


        this.dispatcher.addReceiver(shutdownContainer.getContainerClass(), new Act1<Receipt<Tc>>() {
            @Override
            public void func(Receipt<Tc> receipt) {
                System.out.println(receipt.close());
            }
        });

        final MarshallGeneral<Tc> mgThis = this;
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                mgThis.stop();
            }
        }));
	}

    /**
     * Starts the procedure to stop all communications currently operated by the MarshallGeneral.
     * Blocks until the MarshallGeneral has stopped.
     * @return Success if communications stopped normally, Failure otherwise.
     */
	public Challenge stop() {
        receiver.sendClose(shutdownContainer);
		active = Challenge.Failure("asked to stop");
		return receiver.stop();
	}

    /**
     * Starts the MarshallGeneral messaging operations
     * @return
     */
	public Challenge start() {
        // start the receiver
        return Challenge.Maybe(receiver.start(), new Func1<Receiver, Challenge>() {
			public Challenge func(Receiver sr) {
                // start looping to continuously accept new connections
				active = Challenge.Success("started");
				while (active.isSuccess() && receiver.canAccept()) {
                    // start accepting, block until new connection is received
                    //  also provide a Func to handle each request from this client
					System.out.println("debug: mg accepting...");
					Challenge m = Challenge.Maybe(sr.accept(new Act1<Maybe<Receipt<Tc>>>() {
                        @Override
                        public void func(Maybe<Receipt<Tc>> receiptMaybe) {
                            // submit a new "task" to handle the request only if it was understood
                            // correctly
                            Challenge.Maybe(receiptMaybe, new Func1<Receipt<Tc>, Challenge>() {
                                @Override
                                public Challenge func(final Receipt<Tc> receipt) {
                                    // keeptrack of how many tasks were started
                                    handlerCount.incrementAndGet();
                                    pool.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            // raise the event to notify that something
                                            // was received
                                            dispatcher.dispatch(receipt);
                                            // increment the semaphore counter once we're done
                                            handlers.release();
                                        }
                                    });
                                    // always succeed
                                    return Challenge.Success("done");
                                }
                            });
                        }
                    }), new Func1<Act1<Class<Tc>>, Challenge>() {
                        @Override
                        public Challenge func(final Act1<Class<Tc>> doReceive) {
                            taskCount.incrementAndGet();
                            // once the client was accepted, start listening to its requests
                            pool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    doReceive.func(containerContainerClass);
                                    tasks.release();
                                }
                            });
                            return Challenge.Success("accepted new client");
                        }
                    });
				}
				try {
                    tasks.acquire(taskCount.get());
                    handlers.acquire(handlerCount.get());
                    pool.shutdown();
					if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
						pool.shutdownNow();
						if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
							return Challenge.Failure("pool did not terminate properly");
						}
					}
					return Challenge.Success("stopped gracefully");
				} catch (InterruptedException ie) {
					pool.shutdownNow();
					Thread.currentThread().interrupt();
					return Challenge.Failure("pool did not terminate properly");
				}
			}
		});
	}
}
