package ift604.common.models;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Match implements Serializable {
	protected static final Integer PERIOD_TIME = 5000; //20 * 60 * 1000;
	protected static final Integer INTERVAL = 1000; //30 * 1000;
	protected static Integer ID_COUNT = 0;
	
	private int id;
	
	private Integer period;
	private Timer timer;
	private Integer clock;
	private Boolean started;
	private Boolean finished;
	
	private Team home;
	private Team away;
	private ArrayList<Goal> goals;
	private ArrayList<Penalty> penalties;
	private Score score;
	private Team winner;

	public Match(Team home, Team away) {
		this.id = ID_COUNT;
		ID_COUNT++;
		
		this.period = 1;
		this.home = home;
		this.away = away;
		this.timer = new Timer();
		this.started = false;
		this.finished = false;
		this.winner = null;
		this.clock = PERIOD_TIME; // 20 minutes
		this.score = new Score();
		this.goals = new ArrayList<Goal>();
		this.penalties = new ArrayList<Penalty>();
	}

	public void start() {
		this.started = true;
		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Game Time is " + clock.toString() + " in period " + period.toString());
				clock -= INTERVAL;
				if (clock < 0) {
					if (period == 3) {
						started = false;
						finished = true;
						timer.cancel();
						System.out.println("Game Ended");
					} else {
						period += 1;
						clock = PERIOD_TIME; // Reinitialize timer
					}
				}
			}
		}, INTERVAL, INTERVAL);
	}
	
	public Boolean isStarted() {
		return started;
	}
	
	public Boolean isFinished() {
		return finished;
	}
	
	public void homeScores(Player p, Player a) {
		goals.add(new Goal(p, home, a));
		score.goalForHome();
	}
	
	public void awayScores(Player p, Player a) {
		goals.add(new Goal(p, away, a));
		score.goalForAway();
	}
	
	public void penaltyForHome(Player p, String type) {
		penalties.add(new Penalty(p, home, type));
	}
	
	public void penaltyForAway(Player p, String type) {
		penalties.add(new Penalty(p, away, type));
	}
	
	public Team getWinner() {
		return winner;
	}
	
	public Integer getPeriod() {
		return period;
	}
	
	public Integer getId() {
		return id;
	}
	
}
