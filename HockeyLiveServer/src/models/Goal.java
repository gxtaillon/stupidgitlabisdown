package models;
public class Goal extends MatchEvent {

	private Player assist;

	public Goal(Player p, Team t, Player assist) {
		super(p, t);
		this.assist = assist;
	}

}
