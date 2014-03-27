package edu.gatech.cs7641.assignment2.part1.support;

import opt.OptimizationAlgorithm;
import opt.OptimizationProblem;
import shared.Copyable;
import shared.Instance;
import util.linalg.DenseVector;

public class BitStringRHCTrainer extends OptimizationAlgorithm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6628545401403325373L;
	private Instance optimal;
	private double fitness;

	private BitStringRHCTrainer(OptimizationProblem op) {
		super(op);
	}

	public BitStringRHCTrainer(BitStringProblemRepresentation problem) {
		super(problem);
		this.optimal = problem.random();
		this.fitness = problem.value(optimal);
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
		if (values[bestNeighbor] > fitness) {
			fitness = values[bestNeighbor];
			optimal = neighbors[bestNeighbor];
		}
		return fitness;
	}

	protected Instance flipBit(Instance d, int i) {
		d.getData().set(i, d.getData().get(i)==0?1:0);
		return d;
	}

	@Override
	public Instance getOptimal() {
		return optimal;
	}

}
