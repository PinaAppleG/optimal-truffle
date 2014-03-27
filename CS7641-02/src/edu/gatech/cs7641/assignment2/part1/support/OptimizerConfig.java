package edu.gatech.cs7641.assignment2.part1.support;

import opt.EvaluationFunction;
import opt.NeighborFunction;
import opt.ga.CrossoverFunction;
import opt.ga.MutationFunction;
import dist.Distribution;

public class OptimizerConfig {
	public EvaluationFunction eval;
	public Distribution prior;
	public NeighborFunction neigh;
	public MutationFunction mutation;
	public CrossoverFunction crossover;
	public Distribution target;

	public OptimizerConfig(EvaluationFunction eval, Distribution prior,
			NeighborFunction neigh, MutationFunction mutation,
			CrossoverFunction crossover, Distribution target) {
		this.eval = eval;
		this.prior = prior;
		this.neigh = neigh;
		this.mutation = mutation;
		this.crossover = crossover;
		this.target = target;
	}

}