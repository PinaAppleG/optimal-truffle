package edu.gatech.cs7641.assignment2.part1;

import opt.HillClimbingProblem;
import opt.RandomizedHillClimbing;
import shared.Trainer;

public class LazyNeighborhoodSearchTrainer implements Trainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6628545401403325373L;
	private HillClimbingProblem hcp;
	private int neighborhoodSize;
	private RandomizedHillClimbing trainer;
	private double fitness;
	
	public LazyNeighborhoodSearchTrainer (HillClimbingProblem hcp, int neighborhoodSize) {
		this.hcp=hcp;
		this.trainer = new RandomizedHillClimbing(hcp);
		this.neighborhoodSize=neighborhoodSize;
		this.fitness=hcp.value(trainer.getOptimal());
	}
	
	/* (non-Javadoc)
	 * @see shared.Trainer#train()
	 * Lazy search that returns the first good neighbor found
	 */
	@Override
	public double train() {
		int i=0;
		double newFitness;
		while (i<neighborhoodSize) {
			newFitness=trainer.train();
			if (newFitness>fitness) {
				fitness=newFitness;
				break;
			}
			i++;
		}
		return fitness;
	}

}
