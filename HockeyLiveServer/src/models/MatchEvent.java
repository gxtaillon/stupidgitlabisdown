package models;
public abstract class MatchEvent {
	private Player player;
	private Team team;

	public MatchEvent(Player player, Team team) {
		this.player = player;
		this.team = team;
	}

}
