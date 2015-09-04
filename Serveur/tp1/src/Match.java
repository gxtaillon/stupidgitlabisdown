import java.util.List;


public class Match {

	private Equipe[] equipes  = new Equipe[2];
	private int[] pointage =  new int[2];
	private int[] buts =  new int[2];
	private int[] penalites =  new int[2];
	private Chronometre chronometre;
	
	public Equipe[] getEquipes() {
		return equipes;
	}
	public void setEquipe1(Equipe a) {
		equipes[0] =a;
	}
	public void setEquipe2(Equipe a) {
		equipes[1] =a;
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
	public int[] getPenalites() {
		return penalites;
	}
	public Chronometre getChronometre() {
		return chronometre;
	}

	
	
}
