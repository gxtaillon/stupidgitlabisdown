package ift604.common.dispatch;

import gxt.common.Act1;
import gxt.common.Challenge;
import gxt.common.Func1;
import gxt.common.Func2;
import gxt.common.Maybe;
import gxt.common.extension.HashMapExtension;
import ift604.common.transport.Receipt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ContainerDispatcher <Tc extends ContainerContainer & Serializable> implements Dispatcher<Tc> {
	HashMap<Class<?>, ArrayList<Act1<Receipt<Tc>>>> receivers;
	
	public ContainerDispatcher() {
		receivers = new HashMap<Class<?>, ArrayList<Act1<Receipt<Tc>>>>();
	}
	
	@Override
	public void addReceiver(Class<?> c, final Act1<Receipt<Tc>> cr) {
		HashMapExtension.put(receivers, c, new Func2<Class<?>, Maybe<ArrayList<Act1<Receipt<Tc>>>>, ArrayList<Act1<Receipt<Tc>>>>() {
			public ArrayList<Act1<Receipt<Tc>>> func(Class<?> c, Maybe<ArrayList<Act1<Receipt<Tc>>>> malcr) {
				ArrayList<Act1<Receipt<Tc>>> alcr = (malcr.isJust())
						? malcr.just()
						: new ArrayList<Act1<Receipt<Tc>>>();
				alcr.add(cr);
				return alcr;
			}
		});
	}

	@Override
	public Challenge dispatch(final Receipt<Tc> r) {
		Tc c = r.getPayload();
		return Challenge.Maybe(HashMapExtension.get(receivers, c.getContainerClass()), new Func1<ArrayList<Act1<Receipt<Tc>>>, Challenge>() {
			public Challenge func(ArrayList<Act1<Receipt<Tc>>> alcr) {
				for (Act1<Receipt<Tc>> cr : alcr) {
					cr.func(r);
				}
				return Challenge.Success("cargo receivers received cargo");
			}
		});
		
	}
}
