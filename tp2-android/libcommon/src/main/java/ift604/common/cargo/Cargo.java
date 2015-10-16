package ift604.common.cargo;

import java.io.Serializable;

import ift604.common.dispatch.ContainerContainer;

/**
 * The Cargo class acts as an abstraction layer and standardized message format for the
 * MarshallGeneral class.
 */
public class Cargo implements Serializable, ContainerContainer {
	
	private static final long serialVersionUID = -1954647204344856020L;
	private long serial;
	private Class<?> payloadClass;
	private Serializable payload;

    /**
     * Creates a new Cargo instance.
     * @param serial Unique serial which identifies the cargo
     * @param payloadClass Class of the payload instance
     * @param payload instance to carry
     */
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
