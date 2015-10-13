package models;

public class MatchEventsRunnable implements Runnable {

	private Match match;

	public MatchEventsRunnable(Match m) {
		this.match = m;
	}

	@Override
	public void run() {
		while(!match.isFinished()) {
			// Make events happen
		}
	}

}
