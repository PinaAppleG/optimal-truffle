package edu.gatech.cs7641.assignment2.algorithms;

import java.util.Random;

import edu.gatech.cs7641.assignment2.model.LocalSpace;
import edu.gatech.cs7641.assignment2.model.Location;

public class SimulatedAnnealing {

	/* A cooling rate of 0.9 would give about 6,730 iterations.
	 * A cooling rate of 0.5 would give about 1,024 iterations */
	public Location search(LocalSpace localSpace, double coolingRate,
			int maxRestarts) {
		if (coolingRate>=1||coolingRate<=0) throw new RuntimeException("Cooling rate out of bounds");
		double fitness = Double.MAX_VALUE;
		double maxFitness = -Double.MAX_VALUE;
		Location optimum = null;
		for (int restarts=0;restarts<maxRestarts+1; restarts++) {
			Location localOptimum=anneal(localSpace, coolingRate);
			fitness = localSpace.valueOf(localOptimum);
			System.out.println("Iteration "+restarts+" fitness "+fitness);
			if (fitness > maxFitness) {
				optimum = localOptimum;
				maxFitness = fitness;
			}
		}
		return optimum;
	}

	private Location anneal(LocalSpace localSpace, double coolingRate) {
		Location currentLocation = localSpace.getRandomLocation();
		Location nextLocation;
		final double maxTemperature=Double.MAX_VALUE/2;
		double temperature = maxTemperature;
		final double ambient=76; // The perfect temperature
		Random random = new Random(1L);
		while (temperature>ambient) {
			if(random.nextDouble()>temperature/maxTemperature) {
				// Hill climb
				nextLocation = stepUp(localSpace, currentLocation);
				if (nextLocation != null)
					currentLocation = nextLocation;
			} else {
				// Random walk
				nextLocation = randomStep(localSpace,currentLocation,random);
			}
			temperature=temperature*coolingRate;
		}
		return currentLocation;
	}
	
	private Location randomStep(LocalSpace localSpace,
			Location location, Random random) {
		Location[] neighborhood = localSpace.neighborhoodOf(location);
		if (neighborhood.length==0) throw new RuntimeException("No neighbors of "+location);
		return neighborhood[random.nextInt(neighborhood.length)];
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
