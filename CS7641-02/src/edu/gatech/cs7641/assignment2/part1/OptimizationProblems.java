package edu.gatech.cs7641.assignment2.part1;

import java.util.Arrays;
import java.util.Random;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.ContinuousPeaksEvaluationFunction;
import opt.example.CountOnesEvaluationFunction;
import opt.example.FlipFlopEvaluationFunction;
import opt.example.FourPeaksEvaluationFunction;
import opt.example.KnapsackEvaluationFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.SingleCrossOver;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;
import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import edu.gatech.cs7641.assignment2.part1.support.OptimizerConfig;
import edu.gatech.cs7641.assignment2.util.Timer;

public class OptimizationProblems {
	
    private static final int NUM_ITEMS = 40;
    /** The number of copies each */
    private static final int COPIES_EACH = 4;
    /** The maximum weight for a single element */
    private static final double MAX_WEIGHT = 50;
    /** The maximum volume for a single element */
    private static final double MAX_VOLUME = 50;
    /** The volume of the knapsack */
    private static final double KNAPSACK_VOLUME = 
         MAX_VOLUME * NUM_ITEMS * COPIES_EACH * .4;
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int size = 200;
		int iterations = 10;

		/* Count Ones */
		OptimizerConfig config = setupProblem(size,
				new CountOnesEvaluationFunction());
		optimize(config, iterations);
		
		size=80;
		
		/* Flip Flop */
		config = setupProblem(size,
				new FlipFlopEvaluationFunction());
		optimize(config, iterations);
		
		/* Four Peaks */
		config = setupProblem(size,
				new FourPeaksEvaluationFunction(size/10));
		optimize(config, iterations);
		
		/* Continuous Peaks */
		size=60;
		config = setupProblem(size,
				new ContinuousPeaksEvaluationFunction(size/10));
		optimize(config, iterations);

		/* Knapsack */
		Random random = new Random(1L);
        int[] copies = new int[NUM_ITEMS];
        Arrays.fill(copies, COPIES_EACH);
        double[] weights = new double[NUM_ITEMS];
        double[] volumes = new double[NUM_ITEMS];
        for (int i = 0; i < NUM_ITEMS; i++) {
            weights[i] = random.nextDouble() * MAX_WEIGHT;
            volumes[i] = random.nextDouble() * MAX_VOLUME;
        }
         int[] ranges = new int[NUM_ITEMS];
        Arrays.fill(ranges, COPIES_EACH + 1);
		config = new OptimizerConfig(new KnapsackEvaluationFunction(weights, volumes, KNAPSACK_VOLUME, copies),
				new DiscreteUniformDistribution(ranges),
				new DiscreteChangeOneNeighbor(ranges),
				new DiscreteChangeOneMutation(ranges), new SingleCrossOver(),
				new DiscreteDependencyTree(0.1, ranges));
		optimize(config, iterations);
		
	}

	private static OptimizerConfig setupProblem(int size,
			EvaluationFunction evaluationFunction) {
		int[] ranges = new int[size];
		Arrays.fill(ranges, 2);
		OptimizerConfig config = new OptimizerConfig(evaluationFunction,
				new DiscreteUniformDistribution(ranges),
				new DiscreteChangeOneNeighbor(ranges),
				new DiscreteChangeOneMutation(ranges), new SingleCrossOver(),
				new DiscreteDependencyTree(0.1, ranges));
		return config;
	}

	private static void optimize(OptimizerConfig config, int iterations) {

		Timer timer = new Timer();
		
		FixedIterationTrainer fit;

		HillClimbingProblem hcp = new GenericHillClimbingProblem(config.eval,
				config.prior, config.neigh);

		timer.start();
		System.out.print(config.eval.getClass().getName()+" RHC");
		for (int i = 0; i < iterations; i++) {
			RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
			fit = new FixedIterationTrainer(rhc, 200000);
			fit.train();
			System.out.print(" "+config.eval.value(rhc.getOptimal()));
		}
		System.out.println(" "+timer.display());

		timer.reset();
		System.out.print(config.eval.getClass().getName()+" SA");
		for (int i = 0; i < iterations; i++) {
			SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
			fit = new FixedIterationTrainer(sa, 200000);
			fit.train();
			System.out.print(" "+config.eval.value(sa.getOptimal()));
		}
		System.out.println(" "+timer.display());

		GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(
				config.eval, config.prior, config.mutation, config.crossover);

		timer.reset();
		System.out.print(config.eval.getClass().getName()+" GA");
		for (int i = 0; i < iterations; i++) {
			StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200,
					100, 10, gap);
			fit = new FixedIterationTrainer(ga, 1000);
			fit.train();
			System.out.print(" "+config.eval.value(ga.getOptimal()));
		}
		System.out.println(" "+timer.display());

		ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(
				config.eval, config.prior, config.target);

		timer.reset();
		System.out.print(config.eval.getClass().getName()+" MIMIC");
		for (int i = 0; i < iterations; i++) {
			MIMIC mimic = new MIMIC(200, 20, pop);
			fit = new FixedIterationTrainer(mimic, 1000);
			fit.train();
			System.out.print(" "+config.eval.value(mimic.getOptimal()));
		}
		System.out.println(" "+timer.display());
	}

}
