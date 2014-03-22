package edu.gatech.cs7641.assignment2.part1;

import java.util.Random;

import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import edu.gatech.cs7641.assignment2.util.Timer;

public class TrainNNWeightsWithRHC {

	private static final int RESTARTS = 100;
	private static final long SEED = 1L;
	private static final int NEIGHBORHOOD_SIZE = 1000;

	public static void main(String[] args) {
		Random random = new Random(SEED);
		NNWeightsValidationEvaluationFunction validator = new NNWeightsValidationEvaluationFunction();
		NNWeightsHillClimbingProblem problem = new NNWeightsHillClimbingProblem(
				random);
		OptimizationAlgorithm optimizer, bestOptimizer;
		System.out.print("0, ");
		bestOptimizer = climbSomeHill(random, problem, validator);
		Timer timer = new Timer();
		timer.start();
		for (int i = 1; i < RESTARTS; i++) {
			System.out.print(i+", ");
			optimizer = climbSomeHill(random, problem, validator);
			if(optimizer.getOptimizationProblem().value(optimizer.getOptimal())
				>bestOptimizer.getOptimizationProblem().value(bestOptimizer.getOptimal())) bestOptimizer=optimizer;
		}
		timer.stop();
	}

	private static OptimizationAlgorithm climbSomeHill(Random random,
			NNWeightsHillClimbingProblem problem,
			NNWeightsValidationEvaluationFunction validator) {
		RandomizedHillClimbing optimizer = new RandomizedHillClimbing(problem);
		double fitness = -Double.MAX_VALUE, newFitness, epsilon = 0.01;
		int neighborsChecked = 0;
		Timer timer = new Timer();
		timer.reset();
		timer.start();
		while (neighborsChecked < NEIGHBORHOOD_SIZE) {
			neighborsChecked++;
			newFitness = optimizer.train();
			if (newFitness - fitness > epsilon) {
				neighborsChecked = 0;
				fitness = newFitness;
			}
		}
		timer.stop();
		double validation=validator.value(optimizer.getOptimal());
		System.out.printf("%02f, %02f, %s, %s\n", fitness, validation, timer.display(), optimizer.getOptimal().getData().toString());
		return optimizer;
	}

}
