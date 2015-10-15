package ift604.common.cargo;

import java.io.Serializable;

import ift604.common.transport.Cargo;

public class Shutdown implements Serializable {
	private Shutdown() {}
	private static Shutdown instance = new Shutdown();
	public static Shutdown getInstance() { return instance; }
	private static Cargo shutdown = new Cargo(0L, Shutdown.class, getInstance());
	public static Cargo getShutdownCargo() { return shutdown; }

	private static final long serialVersionUID = 4089997874246736898L;
}
