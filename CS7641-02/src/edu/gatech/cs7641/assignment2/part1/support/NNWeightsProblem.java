package edu.gatech.cs7641.assignment2.part1.support;

import java.util.Random;

import opt.EvaluationFunction;
import opt.HillClimbingProblem;
import opt.ga.GeneticAlgorithmProblem;
import shared.Instance;
import util.linalg.Vector;
import func.nn.feedfwd.FeedForwardNetwork;
import func.nn.feedfwd.FeedForwardNeuralNetworkFactory;

public class NNWeightsProblem implements HillClimbingProblem, GeneticAlgorithmProblem {

	private static final double INIT_SCALE = 0.1d;
	private static final double STEP_SCALE = 0.05d;
	private Random random;
	private int[] nodeCounts = { 3, 2, 1 };
	private FeedForwardNetwork net;
	private EvaluationFunction trainer;

	public NNWeightsProblem(Random random) {
		this.random = random==null?new Random():random;
		this.trainer = new NNWeightsTrainingEvaluationFunction(random);
		FeedForwardNeuralNetworkFactory factory = new FeedForwardNeuralNetworkFactory();
		net = factory.createClassificationNetwork(nodeCounts);
	}

	@Override
	public double value(Instance d) {
		return this.trainer.value(d);
	}

	@Override
	public Instance random() {
		double[] weights = net.getWeights();
		for (int i = 0; i < weights.length; i++) {
			weights[i] = (random.nextBoolean() ? 1 : -1) * random.nextDouble()
					* INIT_SCALE;
		}
		return new Instance(weights);
	}

	@Override
	public Instance neighbor(Instance d) {
		Instance neighbor = new Instance(d.getData().plus(random().getData()));
		int i = random.nextInt(d.getData().size());
		boolean sign = random.nextBoolean();
		neighbor.getData().set(i,
				neighbor.getData().get(i) + (sign ? STEP_SCALE : -STEP_SCALE));
		return neighbor;
	}

	@Override
	public Instance mate(Instance a, Instance b) {
		Vector geneSequence = a.getData().plus(b.getData());
		geneSequence.timesEquals(0.5);
		return new Instance(geneSequence);
	}

	@Override
	public void mutate(Instance d) {
		d.getData().plusEquals(this.random().getData());
	}

}
