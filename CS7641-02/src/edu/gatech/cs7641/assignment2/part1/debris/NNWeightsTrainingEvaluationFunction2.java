package edu.gatech.cs7641.assignment2.part1.debris;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import opt.EvaluationFunction;
import shared.Instance;
import util.linalg.DenseVector;
import util.linalg.Vector;
import func.nn.feedfwd.FeedForwardNetwork;
import func.nn.feedfwd.FeedForwardNeuralNetworkFactory;

public class NNWeightsTrainingEvaluationFunction2 implements EvaluationFunction {
	
	private static final int SIZELIMIT = 1024;
	private int[] nodeCounts = { 3, 2, 1 };
	private FeedForwardNetwork net;
	private double[][] training;

	public NNWeightsTrainingEvaluationFunction2(Random random) {
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
		double fitness = 0;
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
			/* magnitude of output in the right direction */
			output = net.getOutputValues();
			fitness += classification * output.get(0) + (1-classification)* - output.get(0);
			
		}
		/* Reward for getting instances right, reward for getting instances less wrong */
		return fitness/SIZELIMIT*100;
	}

}
