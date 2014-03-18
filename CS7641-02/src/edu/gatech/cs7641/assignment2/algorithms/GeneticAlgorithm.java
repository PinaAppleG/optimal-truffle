package edu.gatech.cs7641.assignment2.algorithms;

import edu.gatech.cs7641.assignment2.model.LocalSpace;
import edu.gatech.cs7641.assignment2.model.Location;

public class GeneticAlgorithm {
	private static final int BREEDING_INTERVAL = 10;

	public Location search(final LocalSpace localSpace, 
			final int populationSize, final int generations) {
		Location[] population = new Location[populationSize];
		double[] fitness = new double[populationSize];
		double peakFitness = -Double.MAX_VALUE;
		int fittestMember;
		initialize(population);
		for(int g=0; g<generations; g++) {
			for(int i=0; i<population.length; i++) fitness[i]=localSpace.valueOf(population[i]);
			breed(population);
			for(int y=0; y<BREEDING_INTERVAL; y++) {
				age(population);
			}
		}
		fittestMember=argmax(fitness);
		return population[fittestMember];
	}

	private int argmax(double[] fitness) {
		int argmax=0;
		if(fitness.length==0) throw new RuntimeException("Empty vector");
		if(fitness.length==1) return 0;
		for (int i=1; i<fitness.length; i++) if (fitness[i]>fitness[argmax]) argmax=i;
		return argmax;
	}

	private void initialize(Location[] population) {
		// TODO Auto-generated method stub
		
	}

	private void breed(Location[] population) {
		// TODO Auto-generated method stub
		
	}

	private void age(Location[] population) {
		// TODO Auto-generated method stub
		
	}
}
