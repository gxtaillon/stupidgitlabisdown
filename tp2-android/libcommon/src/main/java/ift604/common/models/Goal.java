package ift604.common.models;

import java.io.Serializable;

public class Goal extends MatchEvent implements Serializable {

	private Player assist;
	private Player owner;
	private Team OwnerTeam;

	public Goal(Player p, Team t, Player assist) {
		super(p, t);
		this.assist = assist;
	}

}
