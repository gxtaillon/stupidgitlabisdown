package ift604.common.models;
public abstract class MatchEvent {
	private Player player;
	private Team team;

	public MatchEvent(Player player, Team team) {
		this.player = player;
		this.team = team;
	}

}
