package ift604.common.transport;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

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
    protected int taskCount;
	public MarshallGeneral(Class<Tc> c, Dispatcher<Tc> dispatcher, final Receiver receiver, ExecutorService pool, Tc shutdownContainer) {
		this.dispatcher = dispatcher; 
		this.receiver = receiver;
		this.containerContainerClass = c;
		this.pool = pool;
        this.shutdownContainer = shutdownContainer;
		this.active = Challenge.Failure("not started yet");
        this.tasks = new Semaphore(0);
        this.taskCount = 0;

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
                // start looping until the end times to continuously accept new connections
				active = Challenge.Success("started");
				while (active.isSuccess() && receiver.canAccept()) {
                    tasks.release();
                    // start accepting, block until new connection is received
                    //  also provide a Func to handle each request from this client
					System.out.println("debug: mg accepting...");
					Challenge m = Challenge.Maybe(sr.accept(new Act1<Maybe<Receipt<Tc>>>() {
                        @Override
                        public void func(Maybe<Receipt<Tc>> receiptMaybe) {
                            Challenge.Maybe(receiptMaybe, new Func1<Receipt<Tc>, Challenge>() {
                                @Override
                                public Challenge func(final Receipt<Tc> receipt) {
                                    pool.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            dispatcher.dispatch(receipt);
                                            tasks.release();
                                        }
                                    });
                                    ++taskCount;
                                    return Challenge.Success("done");
                                }
                            });
                        }
                    }), new Func1<Act1<Class<Tc>>, Challenge>() {
                        @Override
                        public Challenge func(Act1<Class<Tc>> doReceive) {
                            doReceive.func(containerContainerClass);
                            return Challenge.Success("accepted new client");
                        }
                    });
				}
				try {
                    tasks.acquire(taskCount);
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
