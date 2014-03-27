package edu.gatech.cs7641.assignment2.algorithms;

import java.util.Random;

import edu.gatech.cs7641.assignment2.model.LocalSpace;
import edu.gatech.cs7641.assignment2.model.Location;

public class MySimulatedAnnealing {

	/*
	 * A cooling rate of 0.9 would give about 6,730 iterations. A cooling rate
	 * of 0.5 would give about 1,024 iterations
	 */
	public Location search(LocalSpace localSpace, double coolingRate,
			int maxRestarts) {
		Random random = new Random(1L);
		if (coolingRate >= 1 || coolingRate <= 0)
			throw new RuntimeException("Cooling rate out of bounds");
		double fitness = Double.MAX_VALUE;
		double maxFitness = -Double.MAX_VALUE;
		Location optimum = null;
		for (int restarts = 0; restarts < maxRestarts + 1; restarts++) {
			Location localOptimum = anneal(localSpace, coolingRate, random);
			fitness = localSpace.valueOf(localOptimum);
			System.out.println("Iteration " + restarts + " fitness " + fitness);
			if (fitness > maxFitness) {
				optimum = localOptimum;
				maxFitness = fitness;
			}
		}
		return optimum;
	}

	private Location anneal(LocalSpace localSpace, double coolingRate,
			Random random) {
		Location currentLocation = localSpace.getRandomLocation(random);
		Location nextLocation;
		final double maxTemperature = Double.MAX_VALUE / 2;
		double temperature = maxTemperature;
		final double ambient = 76; // The perfect temperature
		while (temperature > ambient) {
			if (random.nextDouble() > temperature / maxTemperature) {
				// Hill climb
				nextLocation = stepUp(localSpace, currentLocation, random);
				if (nextLocation != null)
					currentLocation = nextLocation;
			} else {
				// Random walk
				nextLocation = randomStep(localSpace, currentLocation, random);
			}
			temperature = temperature * coolingRate;
		}
		return currentLocation;
	}

	private Location randomStep(LocalSpace localSpace, Location location,
			Random random) {
		Location[] neighborhood = localSpace.neighborhoodOf(location, random);
		if (neighborhood.length == 0)
			throw new RuntimeException("No neighbors of " + location);
		return neighborhood[random.nextInt(neighborhood.length)];
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
