package x.transport;

import java.io.Serializable;

public class Boat implements Serializable {
	private boolean isBig;

	public boolean isBig() {
		return isBig;
	}

	public void setBig(boolean isBig) {
		this.isBig = isBig;
	}
}
