package edu.gatech.cs7641.assignment2.part1;

import java.util.Random;

import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import edu.gatech.cs7641.assignment2.part1.support.NNWeightsProblem;
import edu.gatech.cs7641.assignment2.part1.support.NNWeightsValidationEvaluationFunction;
import edu.gatech.cs7641.assignment2.util.Timer;

public class TrainNNWeightsWithSA {

	private static final int RESTARTS = 100;
	private static final long SEED = 1L;
	private static final int NEIGHBORHOOD_SIZE = 10;
	private static final double COOLING = 0.1;
	private static final double STARTING_TEMP = Double.MAX_VALUE/2;

	// Too Hot: Double.MAX_VALUE / 2;

	public static void main(String[] args) {
		Random random = new Random(SEED);
		NNWeightsValidationEvaluationFunction validator = new NNWeightsValidationEvaluationFunction();
		NNWeightsProblem problem = new NNWeightsProblem(
				random);
		OptimizationAlgorithm optimizer, bestOptimizer;
		System.out.print("0, ");
		bestOptimizer = simulateAnnealing(random, problem, validator);
		Timer timer = new Timer();
		timer.start();
		for (int i = 1; i < RESTARTS; i++) {
			System.out.print(i + ", ");
			optimizer = simulateAnnealing(random, problem, validator);
			if (optimizer.getOptimizationProblem()
					.value(optimizer.getOptimal()) > bestOptimizer
					.getOptimizationProblem().value(bestOptimizer.getOptimal()))
				bestOptimizer = optimizer;
		}
		timer.stop();
		System.out.println(timer.display());
	}

	private static OptimizationAlgorithm simulateAnnealing(Random random,
			NNWeightsProblem problem,
			NNWeightsValidationEvaluationFunction validator) {
		RandomizedHillClimbing optimizer = new RandomizedHillClimbing(problem);
		double fitness = -Double.MAX_VALUE, newFitness, epsilon = 0.01;
		int neighborsChecked = 0;
		Timer timer = new Timer();
		timer.reset();
		timer.start();
		double t = STARTING_TEMP;
		while (t > 1 || neighborsChecked < NEIGHBORHOOD_SIZE) {
			t *= COOLING;
			neighborsChecked++;
			newFitness = optimizer.train();
			if (newFitness - fitness > epsilon || random.nextDouble() < 
	                Math.exp((fitness - newFitness) / t)) {
				neighborsChecked = 0;
				fitness = newFitness;
			}
		}
		timer.stop();
		double validation = validator.value(optimizer.getOptimal());
		System.out.printf("%02f, %02f, %s, %s\n", fitness, validation,
				timer.display(), optimizer.getOptimal().getData().toString());
		return optimizer;
	}

}
