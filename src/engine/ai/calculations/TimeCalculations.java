package engine.ai.calculations;

public class TimeCalculations {

	protected float startTime;
	protected float wanderingTime;
	
	public TimeCalculations() {
		startTime = System.nanoTime()/1000000000;
		wanderingTime = 0;
	}

	public boolean hasBeenWanderingFor(int time) {
		if (wanderingTime == 0)
			wanderingTime = System.nanoTime()/1000000000;
		float endTime = System.nanoTime()/1000000000;
		if(endTime-wanderingTime>=time) {
			wanderingTime = endTime;
			return true;
		}
		return false;
	}


	public float secondsElapsed() {
		float endTime = System.nanoTime()/1000000000;
		float elapsedTime = endTime - startTime;
		return elapsedTime;
	}
	
	public void setStartTime(float time) {
		startTime = time;
	}

}
