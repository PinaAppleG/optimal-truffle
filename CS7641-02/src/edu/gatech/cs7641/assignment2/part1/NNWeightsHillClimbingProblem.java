package edu.gatech.cs7641.assignment2.part1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import opt.HillClimbingProblem;
import shared.Instance;
import util.linalg.DenseVector;
import util.linalg.Vector;
import func.nn.feedfwd.FeedForwardNetwork;
import func.nn.feedfwd.FeedForwardNeuralNetworkFactory;

public class NNWeightsHillClimbingProblem implements HillClimbingProblem {

	private static final int SIZELIMIT = 1024;
	private static final double INIT_SCALE = 0.1d;
	private static final double STEP_SCALE = 0.05d;
	private Random random;
	private FeedForwardNetwork net;
	int[] nodeCounts = { 3, 2, 1 };
	private double[][] training;

	public NNWeightsHillClimbingProblem(Random random) {
		this.random = random;
		FeedForwardNeuralNetworkFactory factory = new FeedForwardNeuralNetworkFactory();
		net = factory.createClassificationNetwork(nodeCounts);
		training = new double[SIZELIMIT][];
		try {
			BufferedReader in = new BufferedReader(new FileReader("skin.csv"));
			String line;
			String[] vals;
			double[] v = new double[4];
			int i;
			ArrayList<double[]> dataset = new ArrayList<double[]>();
			while ((line = in.readLine()) != null) {
				vals = line.split(",");
				v = new double[4];
				for (i = 0; i < 4; i++)
					v[i] = Double.parseDouble(vals[i]);
				dataset.add(v);
			}
			/* random sampling with replacement */
			for (i = 0; i < SIZELIMIT; i++)
				training[i] = dataset.get(random.nextInt(dataset.size()));
			in.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public double value(Instance d) {
		if (d == null)
			throw new RuntimeException("Value of empty set is undefined");
		net.setWeights(d.getData());
		double correct = 0;
		double error = 0;
		boolean matches;
		Vector output;
		DenseVector input;
		int classification, prediction;
		for (double[] v : training) {
			if (v == null)
				throw new RuntimeException("V is null");
			if (v.length == 0)
				throw new RuntimeException("V has zero length");
			double[] rgb = new double[3];
			System.arraycopy(v, 0, rgb, 0, 3);
			input = new DenseVector(rgb);
			net.setInputValues(input);
			net.run();
			classification = v[3] == 1 ? 0 : 1;
			prediction = net.getBinaryOutputValue() ? 1 : 0;
			matches = prediction == classification;
			/* magnitude of output in the right direction */
			output = net.getOutputValues();
			error += matches ? 0 : (output.get(0)) * (1 - classification)
					- (output.get(0)) * classification;
			/* correctly classified training instances */
			correct += matches ? 1 : 0;
		}
		/* Reward for getting instances right, reward for getting instances less wrong */
		return (correct - error)/SIZELIMIT*100;
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

	public double validate(Instance d) {
		if (d == null)
			throw new RuntimeException("Value of empty set is undefined");
		net.setWeights(d.getData());
		double correct = 0;
		DenseVector input;
		int classification, prediction, size = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader("skin.csv"));
			String line;
			String[] vals;
			double[] v = new double[4];
			int i;
			while ((line = in.readLine()) != null) {
				size++;
				vals = line.split(",");
				v = new double[4];
				for (i = 0; i < 4; i++)
					v[i] = Double.parseDouble(vals[i]);
				double[] rgb = new double[3];
				System.arraycopy(v, 0, rgb, 0, 3);
				input = new DenseVector(rgb);
				net.setInputValues(input);
				net.run();
				classification = v[3] == 1 ? 0 : 1;
				prediction = net.getBinaryOutputValue() ? 1 : 0;
				if (classification == prediction)
					correct += 1;
			}
			in.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return correct / size * 100;
	}

}
