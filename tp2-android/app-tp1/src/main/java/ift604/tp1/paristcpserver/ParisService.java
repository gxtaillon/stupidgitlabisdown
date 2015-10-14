package ift604.tp1.paristcpserver;

import ift604.common.models.ListeDesMatchs;
import ift604.common.models.Match;

public class ParisService {
	
	public ParisService() {
		
	}
	
	public void parier(Integer matchId, Integer userId, Double amount) {
		Match m = ListeDesMatchs.getInstance().getMatch(matchId);
	}
	
	public Boolean matchTermine(Integer parisId) {
		// TODO
		return false;
	}
	
	public Double getGain(Integer parisId) {
		// TODO Retourner le gain dun paris
		return 0.0;
	}

}
