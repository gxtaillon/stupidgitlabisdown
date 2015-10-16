package ift604.common.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Team implements Serializable {
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
