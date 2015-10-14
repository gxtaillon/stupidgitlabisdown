package ift604.common.models;

public class Score {
	private int home;
	private int away;
	
	public Score() {
		home = 0;
		away = 0;
	}
	
	public int homeScore() {
		return home;
	}
	
	public int awayScore() {
		return away;
	}
	
	public void goalForHome() {
		home += 1;
	}
	
	public void goalForAway() {
		away += 1;
	}
}
