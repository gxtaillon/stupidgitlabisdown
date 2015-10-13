package ift604.common.dispatch;

import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Func2;
import gxt.common.Maybe;
import gxt.common.extension.HashMapExtension;
import ift604.common.transport.Cargo;
import ift604.common.transport.Receipt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ContainerDispatcher <Tc extends ContainerContainer & Serializable> implements Dispatcher<Tc> {
	HashMap<Class<?>, ArrayList<Receiver<Tc>>> receivers;
	
	public ContainerDispatcher() {
		receivers = new HashMap<Class<?>, ArrayList<Receiver<Tc>>>();
	}
	
	@Override
	public void addReceiver(Class<?> c, final Receiver<Tc> cr) {
		HashMapExtension.put(receivers, c, new Func2<Class<?>, Maybe<ArrayList<Receiver<Tc>>>, ArrayList<Receiver<Tc>>>() {
			public ArrayList<Receiver<Tc>> func(Class<?> c, Maybe<ArrayList<Receiver<Tc>>> malcr) {
				ArrayList<Receiver<Tc>> alcr = (malcr.isJust()) 
						? malcr.just()
						: new ArrayList<Receiver<Tc>>();
				alcr.add(cr);
				return alcr;
			}
		});
	}

	@Override
	public Challenge receive(final Receipt<Tc> r) {
		Tc c = r.getPayload();
		return Challenge.Maybe(HashMapExtension.get(receivers, c.getContainerClass()), new Func1<ArrayList<Receiver<Tc>>, Challenge>() {
			public Challenge func(ArrayList<Receiver<Tc>> alcr) {
				for (Receiver<Tc> cr : alcr) {
					cr.receive(r);
				}
				return Challenge.Success("cargo receivers received cargo");
			}
		});
		
	}
}
