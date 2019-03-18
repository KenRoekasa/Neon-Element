package engine.ai.calculations;

public class TimeCalculations {

	protected float startTime;
	protected float wanderingTime;
	protected float pausingTime;
	protected boolean paused;
	
	public TimeCalculations() {
		startTime = currentTime();
		wanderingTime = 0;
	}

	public boolean hasBeenWanderingFor(int time) {
		if(paused) {
			resetStartingTimes();
		}
		if (wanderingTime == 0)
			wanderingTime = currentTime();
		float endTime = currentTime();
		if(endTime-wanderingTime>=time) {
			wanderingTime = endTime;
			return true;
		}
		return false;
	}

	public float secondsElapsed() {
		if(paused) {
			resetStartingTimes();
		}
		float endTime = currentTime();
		float elapsedTime = endTime - startTime;
		return elapsedTime;
	}
	
	public void setStartTime(float time) {
		startTime = time;
	}

	public void setPaused(boolean b) {
		if(b)
			pausingTime = currentTime();
		paused = b;
	}
	/**
	 * @return current time in seconds
	 */
	private float currentTime() {
		return  System.nanoTime()/1000000000;
	}

	/**
	 * deducts pausing time from starting time and wandering time, so the pausing does not affect calculations
	 */
	private void resetStartingTimes() {
		setPaused(false);
		wanderingTime -= pausingTime;
		startTime -=pausingTime;
	}


}
