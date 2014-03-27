package edu.gatech.cs7641.assignment2.part1.debris;

import java.util.Random;

import opt.HillClimbingProblem;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import edu.gatech.cs7641.assignment2.util.Timer;

public class FourPeaksHillClimb {

	private static final int NEIGHBORHOOD_SIZE = 10000;
	private static final int RESTARTS = 100;

	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.start();
		Random random = new Random();
		OptimizationAlgorithm optimizer=null, bestOptimizer = null;
		double bestFitness, newFitness;
		for (int t = 4; t < 100; t++) {
			HillClimbingProblem problem = new FourPeaksProblem(random, t);
			bestFitness = -Double.MAX_VALUE;
			System.out.print(t + ", ");
			for (int r = 0; r < RESTARTS; r++) {
				optimizer = climbSomeHill(random, problem, NEIGHBORHOOD_SIZE);
				newFitness = problem.value(optimizer.getOptimal());
				if (newFitness > bestFitness) {
					bestOptimizer = optimizer;
					bestFitness = newFitness;
				}
			}
			System.out.printf("%1.0f\n", bestFitness);
		}
		timer.stop();
		System.out.println(timer.display());
	}

	private static OptimizationAlgorithm climbSomeHill(Random random,
			HillClimbingProblem problem, int neighborhoodSize) {
		RandomizedHillClimbing optimizer = new RandomizedHillClimbing(problem);
		double fitness = -Double.MAX_VALUE, newFitness, epsilon = 0.01;
		int neighborsChecked = 0;
		while (neighborsChecked < neighborhoodSize) {
			neighborsChecked++;
			newFitness = optimizer.train();
			if (newFitness - fitness > epsilon) {
				neighborsChecked = 0;
				fitness = newFitness;
			}
		}
		return optimizer;
	}

}
