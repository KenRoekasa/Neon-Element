package engine.ai.calculations;

public class TimeCalculations {

	//Start time, used to calculate time passed
	protected float startTime;
	//wandering time, used to calculate time AI has been wandering for
	protected float wanderingTime;
	//pausing time, used to calculate time game has been paused for
	protected float pausingTime;
	//paused boolean, used to know if game was paused
	protected boolean paused;
	//counts number of time game ticked to time shielding and un-shielding
	protected int tickCtr;

	/**
	 * Constructor, sets start time to current time, and wandering time to 0
	 */
	public TimeCalculations() {
		startTime = currentTime();
		wanderingTime = 0;
        tickCtr = 0;
	}

	/**
	 * Calculates time this AI has been wandering for
	 * @param time Threshold given in seconds
	 * @return True if AI has been wandering for time given or more, false otherwise
	 */
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

	/**
	 * Calculates seconds elapsed since start time
	 * @return current time - start time
	 */
	public float secondsElapsed() {
		if(paused) {
			resetStartingTimes();
		}
		float endTime = currentTime();
		float elapsedTime = endTime - startTime;
		return elapsedTime;
	}
	
	/**
	 * Sets start time
	 * @param time Time to set start time to
	 */
	public void setStartTime(float time) {
		startTime = time;
	}
	
	/**
	 * Sets paused boolean to given parameter
	 * @param b boolean to set paused to
	 */
	public void setPaused(boolean b) {
		if(b)
			pausingTime = currentTime();
		paused = b;
	}
	
	/**
	 * Ticks one time, i.e. adds one tick to ticking counter
	 */
	public void tick() {
		tickCtr++;
	}
	
	/**
	 * Compares the number of times game ticked since last time this method returned true or since beginning of the game with given argument.
	 * @param times Number of ticks to compare
	 * @return True if ticking counter is more than or equals to given argument, false otherwise
	 */
	public boolean gameTicked(int times) {
		if(tickCtr >= times) {
			resetTickCounter();
			return true;
		}
		return false;
	}

	public int getTickCtr()
	{
		return tickCtr;
	}
	/**
	 * Sets tick counter to 0
	 */
	private void resetTickCounter() {
		tickCtr = 0;
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
