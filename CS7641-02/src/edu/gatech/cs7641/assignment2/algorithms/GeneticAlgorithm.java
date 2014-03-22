package edu.gatech.cs7641.assignment2.algorithms;

import java.util.Random;

import edu.gatech.cs7641.assignment2.model.LocalSpace;
import edu.gatech.cs7641.assignment2.model.Location;

public class GeneticAlgorithm {
	private static final int BREEDING_INTERVAL = 10;

	public Location search(final LocalSpace localSpace,
			final int populationSize, final int generations, double epsilon) {
		if (populationSize < 1)
			throw new RuntimeException("Illegal populationSize ("
					+ populationSize + ")");
		Random random = new Random(1L);
		Location[] population = new Location[populationSize];
		double[] fitness = new double[populationSize];
		double peakFitness = -Double.MAX_VALUE;
		double leastFitness = Double.MAX_VALUE;
		double lastFitness = -Double.MAX_VALUE;
		double delta = Double.MAX_VALUE;
		int fittestMember = -1;
		for (int i = 0; i < population.length; i++)
			population[i] = localSpace.getRandomLocation(random);
		for (int g = 0; g < generations; g++) {
			// Measure current fitness
			for (int i = 0; i < population.length; i++) {
				fitness[i] = localSpace.valueOf(population[i]);
				if (fitness[i] > peakFitness) {
					peakFitness = fitness[i];
					fittestMember = i;
				}
				if (fitness[i] < leastFitness) {
					leastFitness = fitness[i];
				}
			}
			System.out.printf("Generation %d peak fitness %02f\n", g,
					peakFitness);
			delta = peakFitness - lastFitness;
			if (delta < epsilon)
				break;
			lastFitness = peakFitness;
			// Cull the population
			for (int i = 0; i < population.length; i++) {
				if (random.nextDouble() > (fitness[i] - leastFitness)
						/ (peakFitness - leastFitness)) {
					// death
					int a = -1, b = -1;
					// pick a suitor
					for (int j = 0; j < population.length; j++) {
						if (j == i)
							continue;
						if (random.nextDouble() < (fitness[i] - leastFitness)
								/ (peakFitness - leastFitness)) {
							a = j;
							break;
						}

					}
					// do the mating dance
					for (int j = 0; j < population.length; j++) {
						if (j == i || j == a)
							continue;
						if (random.nextDouble() < (fitness[i] - leastFitness)
								/ (peakFitness - leastFitness)) {
							b = j;
							break;
						}
					}
					// breed
					if ((a > -1 && b > -1)) {
						population[i] = population[a].mateWith(population[b]).mateWith(
								localSpace.getRandomLocation(random));
					} else if (a > -1) {
						// System.out.println("Virgin birth...");
						population[i] = population[a].mateWith(localSpace
								.getRandomLocation(random));
					} else if (b > -1) {
						// System.out.println("Virgin birth...");
						population[i] = population[b].mateWith(localSpace
								.getRandomLocation(random));
					} else {
						// System.out.println("Creation...");
						population[i] = localSpace.getRandomLocation(random);
					}
				}
			}
			for (int i = 0; i < population.length; i++) {
				population[i] = age(localSpace, population[i],
						BREEDING_INTERVAL, random);
			}
		}
		// Measure current fitness
		for (int i = 0; i < population.length; i++) {
			fitness[i] = localSpace.valueOf(population[i]);
			if (fitness[i] > peakFitness) {
				peakFitness = fitness[i];
				fittestMember = i;
			}
		}
		System.out.printf("Generation %d peak fitness %02f\n", generations,
				peakFitness);
		return population[fittestMember];
	}

	private Location age(LocalSpace localSpace, Location location, int years,
			Random random) {
		for (int i = 0; i < years; i++) {
			double fitness = -Double.MAX_VALUE;
			double currentFitness = fitness, peakFitness = fitness;
			Location fittestNeighbor = null;
			for (Location neighbor : localSpace
					.neighborhoodOf(location, random)) {
				currentFitness = localSpace.valueOf(neighbor);
				if (currentFitness > peakFitness) {
					fittestNeighbor = neighbor;
					peakFitness = currentFitness;
				}
			}
			if (fittestNeighbor == null)
				return location;
			else
				location = fittestNeighbor;
		}
		return location;
	}

}
