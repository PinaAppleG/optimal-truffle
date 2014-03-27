package edu.gatech.cs7641.assignment2.part1;

import java.util.Random;

import opt.HillClimbingProblem;
import opt.OptimizationAlgorithm;
import opt.ga.GeneticAlgorithmProblem;
import shared.Instance;
import edu.gatech.cs7641.assignment2.util.Timer;

public class TrainNNWeightsWithHybridGARHC {

	private static final int RESTARTS = 100;
	private static final long SEED = 1L;
	private static final int MAX_GENERATIONS = 1000;
	private static final int NEIGHBORHOOD_SIZE = 10;
	
	private static NNWeightsValidationEvaluationFunction validator;

	public static void main(String[] args) {
		Random random = new Random(SEED);
		/* 
		 * By selecting the training set first after the randomness is seeded,
		 * we ensure the training set is identical given the same seed 
		 * */
		validator = new NNWeightsValidationEvaluationFunction();
		NNWeightsProblem problem = new NNWeightsProblem(random);
		Instance[] solutions;
		Timer timer = new Timer();
		timer.start();
		solutions = evolveSolution(random, MAX_GENERATIONS, problem);
		int i=0;
		for (Instance solution: solutions) {
			System.out.print((i++)+", ");
			climbHill(solution, random, problem, validator, NEIGHBORHOOD_SIZE);
		}
		timer.stop();
		System.out.println(timer.display());
	}

	private static Instance[] evolveSolution(Random random,
			int maxGenerations, GeneticAlgorithmProblem problem) {
		ModifiedGeneticAlgorithm optimizer = new ModifiedGeneticAlgorithm(100, 10, 50, problem);
		for (int i = 0; i < maxGenerations; i++) {
			optimizer.train();
		} 
		return optimizer.getPopulation();
	}
	
	private static OptimizationAlgorithm climbHill(Instance start, Random random,
			HillClimbingProblem problem,
			NNWeightsValidationEvaluationFunction validator, int neighborhoodSize) {
		OptimizationAlgorithm optimizer = new HillClimbingAlgorithm(problem,start);
		double fitness = -Double.MAX_VALUE, newFitness, epsilon = 0.01;
		int neighborsChecked = 0;
		Timer timer = new Timer();
		timer.reset();
		timer.start();
		while (neighborsChecked < neighborhoodSize) {
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
