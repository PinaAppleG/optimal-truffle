package edu.gatech.cs7641.assignment2.part1;

import java.util.Random;

import edu.gatech.cs7641.assignment2.util.Timer;
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

	private static final int RESTARTS = 100;
	private static Random random;

	public static void main(String[] args) {
		random = new Random(1L);
		flipFlopRHC(RESTARTS);
		flipFlopSA(RESTARTS);
		flipFlopGA(RESTARTS);
		flipFlopMIMICConvergence(RESTARTS);
		// flipFlopMIMICFixed(RESTARTS,10);
	}

	private static void flipFlopRHC(int maxIterations) {
		Timer t = new Timer();
		int size = 100;
		double[] fitness = new double[maxIterations];
		double[] iterations = new double[maxIterations];
		EvaluationFunction eval = new FlipFlopEvaluationFunction();
		CrossoverFunction cross = null;
		MutationFunction mutation = booleanChangeOneMutation(size);
		t.start();
		for (int i = 0; i < maxIterations; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation, null);
			Trainer trainer = new BitStringRHCTrainer(problem);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, 0.0001, 100000);
			fitness[i] = convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("FlipFlopRHC, %1.0f, %s, %1.2f, %1.2f, %1.2f\n",
				i.sum(), t.display(), f.get(f.times(-1).argMax()), f.sum()
						/ size, f.get(f.argMax()));
	}

	private static void flipFlopSA(int maxIterations) {
		Timer t = new Timer();
		int size = 100;
		double temp = Double.MAX_VALUE / 2;
		double cooling = 0.1;
		double[] fitness = new double[maxIterations];
		double[] iterations = new double[maxIterations];
		EvaluationFunction eval = new FlipFlopEvaluationFunction();
		CrossoverFunction cross = null;
		MutationFunction mutation = booleanChangeOneMutation(size);
		t.start();
		for (int i = 0; i < maxIterations; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation, null);
			AnnealingTrainer trainer = new AnnealingTrainer(problem, temp,
					cooling, random);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, 0.0001, 100000);
			fitness[i] = convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("FlipFlopSA, %1.0f, %s, %1.2f, %1.2f, %1.2f\n",
				i.sum(), t.display(), f.get(f.times(-1).argMax()), f.sum()
						/ size, f.get(f.argMax()));
	}

	private static void flipFlopGA(int maxIterations) {
		Timer t = new Timer();
		int size = 100;
		double[] fitness = new double[maxIterations];
		double[] iterations = new double[maxIterations];
		EvaluationFunction eval = new FlipFlopEvaluationFunction();
		CrossoverFunction cross = new SingleCrossOver();
		MutationFunction mutation = booleanChangeOneMutation(size);
		t.start();
		for (int i = 0; i < maxIterations; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation, null);
			OptimizationAlgorithm trainer = new StandardGeneticAlgorithm(size,
					80, 25, problem);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, 0.0001, 100000);
			convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
			fitness[i] = eval.value(trainer.getOptimal());
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("FlipFlopGA, %1.0f, %s, %1.2f, %1.2f, %1.2f\n",
				i.sum(), t.display(), f.get(f.times(-1).argMax()), f.sum()
						/ size, f.get(f.argMax()));
	}

	private static void flipFlopMIMICConvergence(int maxIterations) {
		Timer t = new Timer();
		int size = 100;
		double[] fitness = new double[maxIterations];
		double[] iterations = new double[maxIterations];
		EvaluationFunction eval = new FlipFlopEvaluationFunction();
		CrossoverFunction cross = null;
		MutationFunction mutation = null;
		t.start();
		for (int i = 0; i < maxIterations; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation, null);
			MIMIC trainer = new MIMIC(10000, 15, problem);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, 0.0001, 100000);
			convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
			fitness[i] = eval.value(trainer.getOptimal());
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("FlipFlopMIMIC, %1.0f, %s, %1.2f, %1.2f, %1.2f\n",
				i.sum(), t.display(), f.get(f.times(-1).argMax()), f.sum()
						/ size, f.get(f.argMax()));
	}

	@SuppressWarnings("unused")
	private static void flipFlopMIMICFixed(int restarts, int iterationLimit) {
		Timer t = new Timer();
		int size = 100;
		double[] fitness = new double[restarts];
		double[] iterations = new double[restarts];
		EvaluationFunction eval = new FlipFlopEvaluationFunction();
		CrossoverFunction cross = null;
		MutationFunction mutation = null;
		t.start();
		for (int i = 0; i < restarts; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation, null);
			MIMIC trainer = new MIMIC(10000, 15, problem);
			for (int j = 0; j < iterationLimit; j++)
				trainer.train();
			iterations[i] = iterationLimit;
			fitness[i] = eval.value(trainer.getOptimal());
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("FlipFlopMIMIC, %1.0f, %s, %1.2f, %1.2f, %1.2f\n",
				i.sum(), t.display(), f.get(f.times(-1).argMax()), f.sum()
						/ size, f.get(f.argMax()));
	}

	private static MutationFunction booleanChangeOneMutation(int size) {
		int[] ranges = new int[size];
		for (int i = 0; i < size; i++)
			ranges[i] = 1;
		MutationFunction mutation = new DiscreteChangeOneMutation(ranges);
		return mutation;
	}

}
