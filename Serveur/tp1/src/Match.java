public class Match {

	private Equipe[] equipes  = new Equipe[2];
	private int[] pointage =  new int[2];
	private int[] buts =  new int[2];
	private int[] penalites =  new int[2];
	private Chronometre chronometre= new Chronometre();
	
	public Equipe[] getEquipes() {
		return equipes;
	}
	public void setEquipe1(Equipe e1) {
		equipes[0] =e1;
	}
	public void setEquipe2(Equipe e2) {
		equipes[1] =e2;
	}
	public void setEquipes(Equipe[] equipes) {
		this.equipes = equipes;
	}
	public int[] getPointage() {
		return pointage;
	}
	public void setPointage(int[] pointage) {
		this.pointage = pointage;
	}
	public int[] getButs() {
		return buts;
	}
	public int getButsEquipe1() {
		return buts[0];
	}
	public int getPenalitesEquipe1() {
		return penalites[0];
	}
	public int getPenalitesEquipe2() {
		return penalites[1];
	}
	public int[] getPenalites() {
		return penalites;
	}
	public Chronometre getChronometre() {
		return chronometre;
	}
	public Match(Equipe e1, Equipe e2) {
		equipes[0] =e1;
		equipes[1] =e2;
		chronometre.demarer();
	}

	
	
}
