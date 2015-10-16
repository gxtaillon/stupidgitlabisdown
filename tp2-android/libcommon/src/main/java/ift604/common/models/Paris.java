package ift604.common.models;

import java.io.Serializable;

public class Paris implements Serializable {
	static Integer UNIQUE_ID = 0;
	int id = ++UNIQUE_ID;

	private User user;
	private Team team;
	private Double amount;

	public Paris(User u, Team t, Double amount) {
		this.user = u;
		this.team = t;
		this.amount = amount;
	}

	public Double getAmount() {
		return amount;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public Integer id() {
		return id;
	}

}
