package ift604.tp1.models;

public class Penalty extends MatchEvent {
	private String type;

	public Penalty(Player p, Team t, String type) {
		super(p, t);
		this.type = type;
	}

}
