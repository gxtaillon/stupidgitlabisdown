package models;

import java.util.HashMap;

public class ParisManager {

	private Match match;
	private HashMap<Integer, Paris> paris;
	private Double totalAmount;

	public ParisManager(Match m) {
		this.match = m;
		this.totalAmount = 0.0;
		this.paris = new HashMap<Integer, Paris>();
	}
	
	public Boolean matchIsFinished() {
		return match.isFinished();
	}
	
	public synchronized void addBet(User u, Team t, Double amount) throws InvalidParisException {
		if (match.getPeriod() > 2)
			throw new InvalidParisException("Impossible de parier apres la deuxieme periode");
		Paris p = new Paris(u, t, amount);
		paris.put(p.id, p);
		this.totalAmount += amount;
	}
	
	public Double getTotalAmount() {
		return totalAmount;
	}

	public boolean parisGagne(Integer parisId) {
		return match.getWinner() == paris.get(parisId).getTeam();
	}
	
	public Double gains(Integer parisId) {
		if (parisGagne(parisId)) {
			// TODO Faire le calcul
			return paris.get(parisId).getAmount() * 0.75;
		}
		return 0.0;
	}
	
}
