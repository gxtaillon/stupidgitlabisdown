package ift604.common.models;

import java.io.Serializable;

public class Boat implements Serializable {
	
	private static final long serialVersionUID = 4089657874246736898L;
	private boolean isBig;

	public boolean isBig() {
		return isBig;
	}

	public void setBig(boolean isBig) {
		this.isBig = isBig;
	}
	
	public String toString() {
		return "Boat {boatIsBig = "+isBig+"}";
	}
}
