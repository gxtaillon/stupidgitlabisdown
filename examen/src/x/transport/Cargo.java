package x.transport;

import java.io.Serializable;

import x.dispatch.ContainerContainer;

public class Cargo implements Serializable, ContainerContainer {
	
	private static final long serialVersionUID = -1954647204344856020L;
	private long serial;
	private Class<?> payloadClass;
	private Serializable payload;
	public Cargo(long serial, Class<?> payloadClass, Serializable payload) {
		super();
		this.serial = serial;
		this.payloadClass = payloadClass;
		this.payload = payload;
	}
	public long getSerial() {
		return serial;
	}
	@Override
	public Class<?> getContainerClass() {
		return payloadClass;
	}
	public Serializable getContainer() {
		return payload;
	}
}
