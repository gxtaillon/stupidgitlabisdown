package ift604.common.models;

import java.io.Serializable;

public class Player implements Serializable {
	
	private int number;
	private String name;

	public Player(int num, String n) {
		number = num;
		name = n;
	}

}
