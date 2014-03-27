package edu.gatech.cs7641.assignment2.part1.debris;

import java.util.Random;

import shared.Instance;
import util.linalg.DenseVector;

public class AnnealingTrainer extends BitStringRHCTrainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7389103273380661837L;
	private double t;
	private Random random;
	private double cooling;
	private int iterations;
	private double fitness;
	private Instance optimal;

	public AnnealingTrainer(BitStringProblemRepresentation problem,
			double temp, double cooling, Random random) {
		super(problem);
		this.optimal = problem.random();
		this.t = temp;
		this.cooling = cooling;
		this.random = random;
		iterations = 0;
		this.fitness = Double.NEGATIVE_INFINITY;
	}

	@Override
	public double train() {
		Instance[] neighbors = new Instance[optimal.size()];
		double[] values = new double[optimal.size()];
		for (int i = 0; i < neighbors.length; i++) {
			neighbors[i] = flipBit((Instance) optimal.copy(), i);
			values[i] = getOptimizationProblem().value(neighbors[i]);
		}
		DenseVector nVal = new DenseVector(values);
		int bestNeighbor = nVal.argMax();
		int randomNeighbor = random.nextInt(optimal.size());
		if (values[bestNeighbor] > fitness) {
			fitness = values[bestNeighbor];
			optimal = neighbors[bestNeighbor];
		} else if (random.nextDouble() < Math
				.exp((values[bestNeighbor] - fitness) / t)) {
			fitness = values[randomNeighbor];
			optimal = neighbors[randomNeighbor];
		}
		t *= cooling;
		return fitness;
	}

	public int getIterations() {
		return iterations;
	}

	@Override
	public Instance getOptimal() {
		return optimal;
	}
}
