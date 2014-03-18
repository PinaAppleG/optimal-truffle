package edu.gatech.cs7641.assignment2.algorithms;

import edu.gatech.cs7641.assignment2.model.LocalSpace;
import edu.gatech.cs7641.assignment2.model.Location;

public class RandomizedHillClimbing {

	public Location search(LocalSpace localSpace, double epsilon,
			int maxRestarts) {
		double fitness = Double.MAX_VALUE;
		double maxFitness = -Double.MAX_VALUE;
		Location optimum = null;
		for (int restarts=0;restarts<maxRestarts+1; restarts++) {
			Location localOptimum=climbSomeHill(localSpace, epsilon);
			fitness = localSpace.valueOf(localOptimum);
			System.out.println("Iteration "+restarts+" fitness "+fitness);
			if (fitness > maxFitness) {
				optimum = localOptimum;
				maxFitness = fitness;
			}
		}
		return optimum;
	}

	public Location climbSomeHill(LocalSpace localSpace, double epsilon) {
		Location currentLocation = localSpace.getRandomLocation();
		Location nextLocation;
		double delta = Double.MAX_VALUE;
		while (delta > epsilon) {
			double current = localSpace.valueOf(currentLocation);
			nextLocation = stepUp(localSpace, currentLocation);
			if (nextLocation == null)
				return currentLocation;
			double next = localSpace.valueOf(nextLocation);
			delta = Math.abs(current-next);
			//System.out.println(delta + " # " + current + " -> " + next);
			currentLocation = nextLocation;
		}
		return currentLocation;
	}

	private Location stepUp(LocalSpace localSpace, Location location) {
		double fitness = -Double.MAX_VALUE;
		double currentFitness = fitness, peakFitness = fitness;
		Location fittestNeighbor = null;
		for (Location neighbor : localSpace.neighborhoodOf(location)) {
			currentFitness = localSpace.valueOf(neighbor);
			if (currentFitness > peakFitness) {
				fittestNeighbor = neighbor;
				peakFitness = currentFitness;
			}
		}
		return fittestNeighbor;
	}

}
