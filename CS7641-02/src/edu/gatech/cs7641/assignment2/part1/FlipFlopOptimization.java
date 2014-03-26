package edu.gatech.cs7641.assignment2.part1;

import java.util.Random;

import opt.EvaluationFunction;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.FlipFlopEvaluationFunction;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.MutationFunction;
import opt.ga.SingleCrossOver;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.MIMIC;
import shared.ConvergenceTrainer;
import shared.Trainer;
import util.linalg.DenseVector;
import util.linalg.Vector;

public class FlipFlopOptimization {

	private static final int ITER = 100;
	private static Random random;

	public static void main(String[] args) {
		random = new Random(1L);
		flipFlopRHC(ITER);
		flipFlopSA(ITER);
		flipFlopGA(ITER);
		flipFlopMIMICConvergence(ITER);
		//flipFlopMIMICFixed(ITER);
	}

	private static void flipFlopRHC(int maxIterations) {
		int size = 100;
		double[] fitness = new double[maxIterations];
		double[] iterations = new double[maxIterations];
		EvaluationFunction eval = new FlipFlopEvaluationFunction();
		CrossoverFunction cross = null;
		MutationFunction mutation = booleanChangeOneMutation(size);
		for (int i = 0; i < maxIterations; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation);
			Trainer trainer = new RandomizedHillClimbing(problem);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, 0.01, 10000);
			fitness[i] = convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
		}
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("FlipFlopRHC, %1.0f, %1.2f, %1.2f, %1.2f\n", i.sum(),
				f.get(f.times(-1).argMax()), f.sum() / size, f.get(f.argMax()));
	}

	private static void flipFlopSA(int maxIterations) {
		int size = 100;
		double[] fitness = new double[maxIterations];
		double[] iterations = new double[maxIterations];
		EvaluationFunction eval = new FlipFlopEvaluationFunction();
		CrossoverFunction cross = null;
		MutationFunction mutation = booleanChangeOneMutation(size);
		for (int i = 0; i < maxIterations; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation);
			Trainer trainer = new SimulatedAnnealing(Double.MAX_VALUE / 2, 0.1,
					problem);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, 0.01, 10000);
			fitness[i] = convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
		}
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("FlipFlopSA, %1.0f, %1.2f, %1.2f, %1.2f\n", i.sum(),
				f.get(f.times(-1).argMax()), f.sum() / size, f.get(f.argMax()));
	}

	private static void flipFlopGA(int maxIterations) {
		int size = 100;
		double[] fitness = new double[maxIterations];
		double[] iterations = new double[maxIterations];
		EvaluationFunction eval = new FlipFlopEvaluationFunction();
		CrossoverFunction cross = new SingleCrossOver();
		MutationFunction mutation = booleanChangeOneMutation(size);
		for (int i = 0; i < maxIterations; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation);
			OptimizationAlgorithm trainer = new StandardGeneticAlgorithm(size,
					80, 25, problem);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, 0.01, 10000);
			convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
			fitness[i] = eval.value(trainer.getOptimal());
		}
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("FlipFlopGA, %1.0f, %1.2f, %1.2f, %1.2f\n", i.sum(),
				f.get(f.times(-1).argMax()), f.sum() / size, f.get(f.argMax()));
	}

	private static void flipFlopMIMICConvergence(int maxIterations) {
		int size = 100;
		double[] fitness = new double[maxIterations];
		double[] iterations = new double[maxIterations];
		EvaluationFunction eval = new FlipFlopEvaluationFunction();
		CrossoverFunction cross = null;
		MutationFunction mutation = null;
		for (int i = 0; i < maxIterations; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation);
			MIMIC trainer = new MIMIC(1000, 15, problem);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, 0.01, 10000);
			convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
			fitness[i] = eval.value(trainer.getOptimal());
		}
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("FlipFlopMIMIC, %1.0f, %1.2f, %1.2f, %1.2f\n", i.sum(),
				f.get(f.times(-1).argMax()), f.sum() / size, f.get(f.argMax()));
	}

	@SuppressWarnings("unused")
	private static void flipFlopMIMICFixed(int maxIterations) {
		int size = 100;
		double[] fitness = new double[maxIterations];
		double[] iterations = new double[maxIterations];
		EvaluationFunction eval = new FlipFlopEvaluationFunction();
		CrossoverFunction cross = null;
		MutationFunction mutation = null;
		for (int i = 0; i < maxIterations; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation);
			MIMIC trainer = new MIMIC(1000, 15, problem);
			for (int j=0; j<maxIterations; j++) trainer.train();
			iterations[i] = maxIterations;
			fitness[i] = eval.value(trainer.getOptimal());
		}
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("FlipFlopMIMIC, %1.0f, %1.2f, %1.2f, %1.2f\n", i.sum(),
				f.get(f.times(-1).argMax()), f.sum() / size, f.get(f.argMax()));
	}
	
	private static MutationFunction booleanChangeOneMutation(int size) {
		int[] ranges = new int[size];
		for (int i = 0; i < size; i++)
			ranges[i] = 1;
		MutationFunction mutation = new DiscreteChangeOneMutation(ranges);
		return mutation;
	}

}
