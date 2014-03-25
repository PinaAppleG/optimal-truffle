package edu.gatech.cs7641.assignment2.part1;

import java.util.Random;

import opt.HillClimbingProblem;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import edu.gatech.cs7641.assignment2.util.Timer;

public class FourPeaksHillClimb {

	private static final int NEIGHBORHOOD_SIZE = 100;
	private static final int RESTARTS = 100;

	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.start();
		Random random = new Random();
		for (int t = 4; t < 100; t++) {
			HillClimbingProblem problem = new FourPeaksProblem(random, t);
			System.out.print(t + ", ");
			climbSomeHill(random, problem, NEIGHBORHOOD_SIZE);
		}
		timer.stop();
		System.out.println(timer.display());
	}

	private static OptimizationAlgorithm climbSomeHill(Random random,
			HillClimbingProblem problem, int neighborhoodSize) {
		RandomizedHillClimbing optimizer = new RandomizedHillClimbing(problem);
		double fitness = -Double.MAX_VALUE, newFitness, epsilon = 0.01;
		int neighborsChecked = 0;
		int i=0;
		while (neighborsChecked < neighborhoodSize) {
			i++;
			neighborsChecked++;
			newFitness = optimizer.train();
			if (newFitness - fitness > epsilon) {
				neighborsChecked = 0;
				fitness = newFitness;
			}
		}
		System.out.printf("%02f, %02f, %d, %s\n", fitness, fitness,
				i, optimizer.getOptimal().getData().toString());
		return optimizer;
	}

}
