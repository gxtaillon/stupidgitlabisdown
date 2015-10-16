package ift604.common.models;

import java.io.Serializable;

public class Penalty extends MatchEvent implements Serializable {
	private String type;

	public Penalty(Player p, Team t, String type) {
		super(p, t);
		this.type = type;
	}

}
