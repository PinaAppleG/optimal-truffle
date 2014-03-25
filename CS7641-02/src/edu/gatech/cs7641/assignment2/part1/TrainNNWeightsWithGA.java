package edu.gatech.cs7641.assignment2.part1;

import java.util.Random;

import opt.OptimizationAlgorithm;
import opt.ga.StandardGeneticAlgorithm;
import shared.Instance;
import edu.gatech.cs7641.assignment2.util.Timer;

public class TrainNNWeightsWithGA {

	private static final int RESTARTS = 100;
	private static final long SEED = 1L;
	private static final int MAX_GENERATIONS = 1000;
	
	private static NNWeightsValidationEvaluationFunction validator;

	public static void main(String[] args) {
		Random random = new Random(SEED);
		/* 
		 * By selecting the training set first after the randomness is seeded,
		 * we ensure the training set is identical given the same seed 
		 * */
		validator = new NNWeightsValidationEvaluationFunction();
		OptimizationAlgorithm optimizer, bestOptimizer;
		System.out.print("0, ");
		bestOptimizer = evolveSolution(random, MAX_GENERATIONS);
		Timer timer = new Timer();
		timer.start();
		for (int i = 1; i < RESTARTS; i++) {
			System.out.print(i + ", ");
			optimizer = evolveSolution(random, MAX_GENERATIONS);
			if (optimizer.getOptimizationProblem()
					.value(optimizer.getOptimal()) > bestOptimizer
					.getOptimizationProblem().value(bestOptimizer.getOptimal()))
				bestOptimizer = optimizer;
		}
		timer.stop();
		System.out.println(timer.display());
	}

	private static OptimizationAlgorithm evolveSolution(Random random,
			int maxGenerations) {
		NNWeightsProblem problem = new NNWeightsProblem(random);
		OptimizationAlgorithm optimizer = new StandardGeneticAlgorithm(100, 10,
				50, problem);
		Timer timer = new Timer();
		timer.reset();
		timer.start();
		for (int i = 0; i < maxGenerations; i++) {
			optimizer.train();
		}
		timer.stop();
		Instance optimal = optimizer.getOptimal();
		double fitness = problem.value(optimal);
		double validation = validator.value(optimal);
		System.out.printf("%02f, %02f, %s, %s\n", fitness, validation,
				timer.display(), optimal.getData().toString());
		return optimizer;
	}

}
