package edu.gatech.cs7641.assignment2.part1.support;

import java.util.Random;

import opt.EvaluationFunction;
import opt.NeighborFunction;
import opt.OptimizationAlgorithm;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.MutationFunction;
import opt.ga.SingleCrossOver;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.MIMIC;
import shared.ConvergenceTrainer;
import shared.Trainer;
import util.linalg.DenseVector;
import edu.gatech.cs7641.assignment2.util.Timer;

public class BitStringOptimizationProcedure {

	private static final double TOLERANCE = 0.0001;
	private static final int MIMIC_SAMPLE_SIZE = 1000;
	private static final int MAX_ITERATIONS = 10000;
	private static final double TEMP = 1000;
	private static final double COOLING = 0.1;
	private static final int GA_POPULATION_SIZE = 100;

	private Random random;

	public void run(EvaluationFunction evaluationFunction, int bitLength,
			int sampleSize, int mimicIterations) {
		optimizeWithRHC(evaluationFunction, bitLength, sampleSize);
		optimizeWithSA(evaluationFunction, bitLength, sampleSize);
		optimizeWithGA(evaluationFunction, bitLength, sampleSize);
		if (mimicIterations < 0)
			optimizeWithMIMICConvergence(evaluationFunction, bitLength,
					sampleSize);
		else if (mimicIterations > 0)
			optimizeWithMIMICFixed(evaluationFunction, bitLength, sampleSize,
					mimicIterations);
	}

	public void seedRandom(long seed) {
		this.random = new Random(seed);
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public BitStringOptimizationProcedure() {
		this.random = new Random();
	}

	private void optimizeWithRHC(EvaluationFunction evaluationFunction,
			int bitLength, int sampleSize) {
		Timer t = new Timer();
		double[] fitness = new double[sampleSize];
		double[] iterations = new double[sampleSize];
		CrossoverFunction cross = null;
		MutationFunction mutation = null;
		NeighborFunction neighbor = new BitFlipNextNeighborFunction();
		t.start();
		for (int i = 0; i < sampleSize; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					bitLength, random, evaluationFunction, cross, mutation,
					neighbor);
			Trainer trainer = new BitStringRHCTrainer(problem);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, 0.0001, 100000);
			fitness[i] = convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf(
				"%s, RHC, %1.0f, %s, %1.2f, %1.2f, %1.2f\n",
				evaluationFunction.getClass().getName()
						.replace("EvaluationFunction", ""), i.sum(),
				t.display(), f.get(f.times(-1).argMax()), f.sum() / sampleSize,
				f.get(f.argMax()));
	}

	private void optimizeWithSA(EvaluationFunction evaluationFunction,
			int bitLength, int sampleSize) {
		Timer t = new Timer();
		double[] sampleFitness = new double[sampleSize];
		double[] sampleIterations = new double[sampleSize];
		CrossoverFunction cross = null;
		MutationFunction mutation = null;
		NeighborFunction neighbor = new RandomBitFlipNeighborFunction(random);
		t.start();
		for (int i = 0; i < sampleSize; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					bitLength, random, evaluationFunction, cross, mutation,
					neighbor);
			AnnealingTrainer trainer = new AnnealingTrainer(problem, TEMP, COOLING, random);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, TOLERANCE, 100000);
			sampleFitness[i] = convergenceTrainer.train();
			sampleIterations[i] = convergenceTrainer.getIterations();
		}
		t.stop();
		DenseVector f = new DenseVector(sampleFitness);
		DenseVector i = new DenseVector(sampleIterations);
		System.out.printf(
				"%s, SA, %1.0f, %s, %1.2f, %1.2f, %1.2f\n",
				evaluationFunction.getClass().getName()
						.replace("EvaluationFunction", ""), i.sum(),
				t.display(), f.get(f.times(-1).argMax()), f.sum() / sampleSize,
				f.get(f.argMax()));
	}

	private void optimizeWithGA(EvaluationFunction evaluationFunction,
			int bitLength, int sampleSize) {
		Timer t = new Timer();
		double[] fitness = new double[sampleSize];
		double[] iterations = new double[sampleSize];
		CrossoverFunction cross = new SingleCrossOver();
		MutationFunction mutation = booleanChangeOneMutation(bitLength);
		NeighborFunction neighbor = new RandomBitFlipNeighborFunction(random);
		t.start();
		for (int i = 0; i < sampleSize; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					bitLength, random, evaluationFunction, cross, mutation,
					neighbor);
			OptimizationAlgorithm trainer = new StandardGeneticAlgorithm(
					GA_POPULATION_SIZE, 80, 25, problem);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, 0.0001, 100000);
			convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
			fitness[i] = evaluationFunction.value(trainer.getOptimal());
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf(
				"%s, GA, %1.0f, %s, %1.2f, %1.2f, %1.2f\n",
				evaluationFunction.getClass().getName()
						.replace("EvaluationFunction", ""), i.sum(),
				t.display(), f.get(f.times(-1).argMax()), f.sum() / sampleSize,
				f.get(f.argMax()));
	}

	private void optimizeWithMIMICConvergence(
			EvaluationFunction evaluationFunction, int bitLength, int sampleSize) {
		Timer t = new Timer();
		double[] fitness = new double[sampleSize];
		double[] iterations = new double[sampleSize];
		CrossoverFunction cross = null;
		MutationFunction mutation = null;
		NeighborFunction neighbor = null;
		t.start();
		for (int i = 0; i < sampleSize; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					bitLength, random, evaluationFunction, cross, mutation,
					neighbor);
			MIMIC trainer = new MIMIC(MIMIC_SAMPLE_SIZE,
					MIMIC_SAMPLE_SIZE / 10, problem);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, TOLERANCE, MAX_ITERATIONS);
			convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
			fitness[i] = evaluationFunction.value(trainer.getOptimal());
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf(
				"%s, MIMIC, %1.0f, %s, %1.2f, %1.2f, %1.2f\n",
				evaluationFunction.getClass().getName()
						.replace("EvaluationFunction", ""), i.sum(),
				t.display(), f.get(f.times(-1).argMax()), f.sum() / sampleSize,
				f.get(f.argMax()));
	}

	private void optimizeWithMIMICFixed(EvaluationFunction evaluationFunction,
			int bitLength, int sampleSize, int mimicIterations) {
		Timer t = new Timer();
		double[] fitness = new double[sampleSize];
		double[] iterations = new double[sampleSize];
		CrossoverFunction cross = null;
		MutationFunction mutation = null;
		NeighborFunction neighbor = null;
		t.start();
		for (int i = 0; i < sampleSize; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					bitLength, random, evaluationFunction, cross, mutation,
					neighbor);
			MIMIC trainer = new MIMIC(MIMIC_SAMPLE_SIZE,
					MIMIC_SAMPLE_SIZE / 100, problem);
			for (int j = 0; j < mimicIterations; j++)
				trainer.train();
			iterations[i] = mimicIterations;
			fitness[i] = evaluationFunction.value(trainer.getOptimal());
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf(
				"%s, MIMIC, %1.0f, %s, %1.2f, %1.2f, %1.2f\n",
				evaluationFunction.getClass().getName()
						.replace("EvaluationFunction", ""), i.sum(),
				t.display(), f.get(f.times(-1).argMax()), f.sum() / sampleSize,
				f.get(f.argMax()));
	}

	private MutationFunction booleanChangeOneMutation(int size) {
		int[] ranges = new int[size];
		for (int i = 0; i < size; i++)
			ranges[i] = 1;
		MutationFunction mutation = new DiscreteChangeOneMutation(ranges);
		return mutation;
	}

}
