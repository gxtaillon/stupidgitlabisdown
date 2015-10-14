package ift604.common.models;
public class Goal extends MatchEvent {

	private Player assist;
	private Player owner;
	private Team OwnerTeam;

	public Goal(Player p, Team t, Player assist) {
		super(p, t);
		this.assist = assist;
	}

}
