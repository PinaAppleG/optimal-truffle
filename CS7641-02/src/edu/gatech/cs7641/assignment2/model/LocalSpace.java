package edu.gatech.cs7641.assignment2.model;

public interface LocalSpace {

	public Location getRandomLocation();

	public double valueOf(Location start);

	public Location[] neighborhoodOf(Location currentLocation);

	public double trainingAccuracyOf(Location optimum);

	double fullAccuracyOf(Location optimum);

}
