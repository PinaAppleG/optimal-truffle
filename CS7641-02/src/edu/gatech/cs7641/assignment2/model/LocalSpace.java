package edu.gatech.cs7641.assignment2.model;

import java.util.Random;

public interface LocalSpace {

	public Location getRandomLocation(Random random);

	public double valueOf(Location start);

	public Location[] neighborhoodOf(Location currentLocation, Random random);

	public double trainingAccuracyOf(Location optimum);

	double fullAccuracyOf(Location optimum);

}
