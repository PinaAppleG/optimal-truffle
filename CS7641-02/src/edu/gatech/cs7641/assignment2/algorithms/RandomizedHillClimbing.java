package edu.gatech.cs7641.assignment2.algorithms;

import java.util.Random;

import edu.gatech.cs7641.assignment2.model.LocalSpace;
import edu.gatech.cs7641.assignment2.model.Location;

public class RandomizedHillClimbing {

	public Location climbFrom(Location currentLocation, LocalSpace localSpace, double epsilon, Random random) {
		Location nextLocation;
		double delta = Double.MAX_VALUE;
		while (delta > epsilon) {
			double current = localSpace.valueOf(currentLocation);
			nextLocation = stepUp(localSpace, currentLocation, random);
			if (nextLocation == null)
				return currentLocation;
			double next = localSpace.valueOf(nextLocation);
			delta = Math.abs(current - next);
			currentLocation = nextLocation;
		}
		return currentLocation;
	}
	
	public Location randomSearch(LocalSpace localSpace, double epsilon,
			int maxRestarts) {
		Random random = new Random(1L);
		double fitness = Double.MAX_VALUE;
		double maxFitness = -Double.MAX_VALUE;
		Location optimum = null;
		for (int restarts = 0; restarts < maxRestarts + 1; restarts++) {
			Location localOptimum = climbSomeHill(localSpace, epsilon, random);
			fitness = localSpace.valueOf(localOptimum);
			System.out.println("Iteration " + restarts + " fitness " + fitness);
			if (fitness > maxFitness) {
				optimum = localOptimum;
				maxFitness = fitness;
			}
		}
		return optimum;
	}

	public Location climbSomeHill(LocalSpace localSpace, double epsilon,
			Random random) {
		Location currentLocation = localSpace.getRandomLocation(random);
		return climbFrom(currentLocation,localSpace,epsilon,random);
	}

	private Location stepUp(LocalSpace localSpace, Location location,
			Random random) {
		double fitness = -Double.MAX_VALUE;
		double currentFitness = fitness, peakFitness = fitness;
		Location fittestNeighbor = null;
		for (Location neighbor : localSpace.neighborhoodOf(location, random)) {
			currentFitness = localSpace.valueOf(neighbor);
			if (currentFitness > peakFitness) {
				fittestNeighbor = neighbor;
				peakFitness = currentFitness;
			}
		}
		return fittestNeighbor;
	}

}
