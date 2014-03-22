package edu.gatech.cs7641.assignment2.util;

public class Timer {

	@SuppressWarnings("unused")
	private static final int DAY_IN_MILLIS = 86400000;
	private static final long HOUR_IN_MILLIS = 3600000;
	private static final long MINUTE_IN_MILLIS = 60000;
	private static final int SECOND_IN_MILLIS = 1000;
	private long elapsedTime;
	private long startPressed;
	private boolean running;

	public void reset() {
		this.elapsedTime=0L;
		if(running)this.startPressed=System.currentTimeMillis();
	}

	public void start() {
		this.startPressed=System.currentTimeMillis();
		running=true;
	}

	public void stop() {
		if(running)elapsedTime+=System.currentTimeMillis()-startPressed;
		running=false;
	}
	
	public String display() {
		long millis=running?System.currentTimeMillis()-startPressed:elapsedTime;
		long hours=millis/HOUR_IN_MILLIS;
		millis=millis%HOUR_IN_MILLIS;
		long minutes=millis/MINUTE_IN_MILLIS;
		millis=millis%MINUTE_IN_MILLIS;
		long seconds=millis/SECOND_IN_MILLIS;
		millis=millis%SECOND_IN_MILLIS;
		return String.format("%02d:%02d:%02d.%03d",hours,minutes,seconds,millis);
	}

}
