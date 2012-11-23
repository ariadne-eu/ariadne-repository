package org.ariadne_eu.utils;
/**
 * A class to help benchmark code
 * It simulates a real stop watch
 */
public class Stopwatch {

	private long elapsedTime = 0;
	private long startTime = -1;
	private long stopTime = -1;
	private boolean running = false;

	public void start() {
		if(!running) {
			if(startTime > 0) {
				elapsedTime = elapsedTime + (stopTime - startTime);
			}
			startTime = System.currentTimeMillis();
			running = true;
		}
		else {
			System.out.println("Stopwatch already running !");
		}
	}
	public void restart() {
		reset();
		start();
	}
	public long stop() {
		if(running) {
		stopTime = System.currentTimeMillis();
		running = false;
		}
		else {
			System.out.println("Stopwatch not started !");
		}
		return getElapsedTime();
	}
	public void stopWPrint() {
		long difference = stop();
		int mins = (int)Math.floor(difference/60000.0);
		float d = (float) (difference/1000.0);
		float e = (float) (mins*60.0);
		float secs = d - e; 
		System.out.println(mins + " m " + secs + " s");
	}
	/** returns elapsed time in milliseconds
	 * if the watch has never been started then
	 * return zero
	 */
	public long getElapsedTime() {
		if (startTime == -1) {
			return 0;
		}
		if (running){
			return System.currentTimeMillis() - startTime;
		} else {
			return stopTime-startTime;
		} 
	}

	public void reset() {
		startTime = -1;
		stopTime = -1;
		elapsedTime = -1;
		running = false;
	}
}