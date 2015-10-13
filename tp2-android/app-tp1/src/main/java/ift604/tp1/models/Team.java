package ift604.tp1.models;

import java.util.ArrayList;

public class Team {
	private String name;
	private static ArrayList<Player> teamPlayers;

	public Team(String name) {
		this.teamPlayers = new ArrayList<Player>();
		this.name = name;
	}
	public void addPlayer(Player p) {
		this.teamPlayers.add(p);
	}
}
