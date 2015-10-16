package ift604.common.models;
import java.io.Serializable;
import java.util.HashMap;

public class ListeDesMatchs implements Serializable {
	private static ListeDesMatchs instance = null;
	private HashMap<Integer, Match> matchs;

	private ListeDesMatchs() {
		this.matchs = new HashMap<Integer, Match>();
	}
	public static ListeDesMatchs getInstance() {
		if(instance == null) {
			instance = new ListeDesMatchs();
		}
		return instance;
	}

	public synchronized void add(Match m) {
		if (matchs.size() >= 10) {
			return;
		}
		matchs.put(m.getId(), m);
	}
	
	public HashMap<Integer, Match> matchsDuJour() {
		return matchs;
	}
	
	public Match getMatch(Integer matchId) {
		return matchs.get(matchId);
	}
}
