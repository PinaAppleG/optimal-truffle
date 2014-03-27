package edu.gatech.cs7641.assignment2.part1.debris;

import java.util.Random;

import opt.EvaluationFunction;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.ga.CrossoverFunction;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.Instance;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

public class BitStringProblemRepresentation implements HillClimbingProblem,
		GeneticAlgorithmProblem, ProbabilisticOptimizationProblem {

	private int size;
	private Random random;
	private EvaluationFunction eval;
	private CrossoverFunction cross;
	private MutationFunction mutation;
	private Distribution dist;
	private NeighborFunction neigh;

	public BitStringProblemRepresentation(int size, Random random,
			EvaluationFunction eval, CrossoverFunction cross,
			MutationFunction mutation, NeighborFunction neigh) {
		super();
		this.size = size;
		this.random = random;
		this.eval = eval;
		this.cross = cross;
		this.mutation = mutation;
		this.neigh = neigh;
		int[] range = new int[size];
		for (int i = 0; i < size; i++)
			range[i] = 2;
		this.dist = new DiscreteUniformDistribution(range);
	}

	@Override
	public double value(Instance d) {
		if (d.size() != this.size)
			throw new IllegalArgumentException("Instance size mismatch");
		return eval.value(d);
	}

	@Override
	public Instance random() {
		double[] bits = new double[size];
		for (int i = 0; i < bits.length; i++)
			bits[i] = random.nextBoolean() ? 1 : 0;
		return new Instance(bits);
	}

	@Override
	public Instance mate(Instance a, Instance b) {
		if (a.size() != this.size)
			throw new IllegalArgumentException("Instance size mismatch");
		if (b.size() != this.size)
			throw new IllegalArgumentException("Instance size mismatch");
		return cross.mate(a, b);
	}

	@Override
	public void mutate(Instance d) {
		if (d.size() != this.size)
			throw new IllegalArgumentException("Instance size mismatch");
		mutation.mutate(d);
		;
	}

	@Override
	public Instance neighbor(Instance d) {
		if (d.size() != this.size)
			throw new IllegalArgumentException("Instance size mismatch");
		return neigh.neighbor(d);
	}

	@Override
	public Distribution getDistribution() {
		return dist;
	}

	public int size() {
		return size;
	}

}
