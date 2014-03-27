package edu.gatech.cs7641.assignment2.part1.support;

import java.util.Random;

import opt.EvaluationFunction;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.example.FourPeaksEvaluationFunction;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.MutationFunction;
import opt.ga.SingleCrossOver;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.MIMIC;
import shared.ConvergenceTrainer;
import shared.Instance;
import shared.Trainer;
import util.linalg.DenseVector;
import edu.gatech.cs7641.assignment2.util.Timer;

public class FourPeaksOptimization {

	private static final int RESTARTS = 100;
	private static Random random;

	public static void main(String[] args) {
		random = new Random(1L);
		fourPeaksRHC(RESTARTS);
		fourPeaksSA(RESTARTS);
		fourPeaksGA(RESTARTS);
		fourPeaksMIMICConvergence(RESTARTS);
		fourPeaksMIMICFixed(RESTARTS,100);
	}

	private static void fourPeaksRHC(int maxIterations) {
		Timer t = new Timer();
		int size = 100;
		double[] fitness = new double[maxIterations];
		double[] iterations = new double[maxIterations];
		EvaluationFunction eval = new FourPeaksEvaluationFunction(size/10);
		CrossoverFunction cross = null;
		MutationFunction mutation = booleanChangeOneMutation(size);
		double bestFit=Double.NEGATIVE_INFINITY;
		Instance best=null, temp=null;
		t.start();
		for (int i = 0; i < maxIterations; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation, null);
			BitStringRHCTrainer trainer = new BitStringRHCTrainer(problem);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, 0.0001, 100000);
			fitness[i] = convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
			if(fitness[i]>bestFit) {
				bestFit=fitness[i];
				best=trainer.getOptimal();
			}
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("fourPeaksRHC, %1.0f, %s, %1.2f, %1.2f, %1.2f, %s\n", i.sum(), t.display(),
				f.get(f.times(-1).argMax()), f.sum() / size, f.get(f.argMax()), best.getData().toString());
	}

	private static void fourPeaksSA(int maxIterations) {
		Timer t = new Timer();
		int size = 100;
		double temp = Double.MAX_VALUE / 2;
		double cooling = 0.1;
		double[] fitness = new double[maxIterations];
		double[] iterations = new double[maxIterations];
		EvaluationFunction eval = new FourPeaksEvaluationFunction(size/10);
		CrossoverFunction cross = null;
		MutationFunction mutation = booleanChangeOneMutation(size);
		double bestFit=Double.NEGATIVE_INFINITY;
		Instance best=null;
		t.start();
		for (int i = 0; i < maxIterations; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation, null);
			AnnealingTrainer trainer = new AnnealingTrainer(
					problem, temp, cooling, random);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, 0.0001, 100000);
			fitness[i] = convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
			if(fitness[i]>bestFit) {
				bestFit=fitness[i];
				best=trainer.getOptimal();
			}
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("fourPeaksSA, %1.0f, %s, %1.2f, %1.2f, %1.2f, %s\n", i.sum(), t.display(),
				f.get(f.times(-1).argMax()), f.sum() / size, f.get(f.argMax()), best.getData().toString());
	}

	private static void fourPeaksGA(int maxIterations) {
		Timer t = new Timer();
		int size = 100;
		double[] fitness = new double[maxIterations];
		double[] iterations = new double[maxIterations];
		EvaluationFunction eval = new FourPeaksEvaluationFunction(size/10);
		CrossoverFunction cross = new SingleCrossOver();
		MutationFunction mutation = booleanChangeOneMutation(size);
		double bestFit=Double.NEGATIVE_INFINITY;
		Instance best=null, tmp=null;
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
			fitness[i] = eval.value(tmp=trainer.getOptimal());
			if(fitness[i]>bestFit) {
				bestFit=fitness[i];
				best=tmp;
			}
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("fourPeaksGA, %1.0f, %s, %1.2f, %1.2f, %1.2f, %s\n", i.sum(), t.display(),
				f.get(f.times(-1).argMax()), f.sum() / size, f.get(f.argMax()), best.getData().toString());
	}

	private static void fourPeaksMIMICConvergence(int maxIterations) {
		Timer t = new Timer();
		int size = 100;
		int sampleSize=10000;
		double[] fitness = new double[maxIterations];
		double[] iterations = new double[maxIterations];
		EvaluationFunction eval = new FourPeaksEvaluationFunction(size/10);
		CrossoverFunction cross = null;
		MutationFunction mutation = null;
		double bestFit=Double.NEGATIVE_INFINITY;
		Instance best=null, tmp=null;
		t.start();
		for (int i = 0; i < maxIterations; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation, null);
			MIMIC trainer = new MIMIC(sampleSize, sampleSize/100, problem);
			ConvergenceTrainer convergenceTrainer = new ConvergenceTrainer(
					trainer, 0.0001, 100000);
			convergenceTrainer.train();
			iterations[i] = convergenceTrainer.getIterations();
			fitness[i] = eval.value(tmp=trainer.getOptimal());
			if(fitness[i]>bestFit) {
				bestFit=fitness[i];
				best=tmp;
			}
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("fourPeaksMIMIC, %1.0f, %s, %1.2f, %1.2f, %1.2f, %s\n", i.sum(), t.display(),
				f.get(f.times(-1).argMax()), f.sum() / size, f.get(f.argMax()), best.getData().toString());
	}

	@SuppressWarnings("unused")
	private static void fourPeaksMIMICFixed(int restarts, int iterationLimit) {
		Timer t = new Timer();
		int size = 100;
		int sampleSize=10000;
		double[] fitness = new double[restarts];
		double[] iterations = new double[restarts];
		EvaluationFunction eval = new FourPeaksEvaluationFunction(size/10);
		CrossoverFunction cross = null;
		MutationFunction mutation = null;
		double bestFit=Double.NEGATIVE_INFINITY;
		Instance best=null, tmp=null;
		t.start();
		for (int i = 0; i < restarts; i++) {
			BitStringProblemRepresentation problem = new BitStringProblemRepresentation(
					size, random, eval, cross, mutation, null);
			MIMIC trainer = new MIMIC(sampleSize, sampleSize/10, problem);
			for (int j=0; j<iterationLimit; j++) trainer.train();
			iterations[i] = iterationLimit;
			fitness[i] = eval.value(tmp=trainer.getOptimal());
			if(fitness[i]>bestFit) {
				bestFit=fitness[i];
				best=tmp;
			}
		}
		t.stop();
		DenseVector f = new DenseVector(fitness);
		DenseVector i = new DenseVector(iterations);
		System.out.printf("fourPeaksMIMIC, %1.0f, %s, %1.2f, %1.2f, %1.2f, %s\n", i.sum(), t.display(),
				f.get(f.times(-1).argMax()), f.sum() / size, f.get(f.argMax()), best.getData().toString());
	}
	
	private static MutationFunction booleanChangeOneMutation(int size) {
		int[] ranges = new int[size];
		for (int i = 0; i < size; i++)
			ranges[i] = 1;
		MutationFunction mutation = new DiscreteChangeOneMutation(ranges);
		return mutation;
	}

}
