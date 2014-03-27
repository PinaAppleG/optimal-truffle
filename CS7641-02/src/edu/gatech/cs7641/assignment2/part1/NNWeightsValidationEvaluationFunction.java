package edu.gatech.cs7641.assignment2.part1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import opt.EvaluationFunction;
import shared.Instance;
import util.linalg.DenseVector;
import func.nn.feedfwd.FeedForwardNetwork;
import func.nn.feedfwd.FeedForwardNeuralNetworkFactory;

public class NNWeightsValidationEvaluationFunction implements EvaluationFunction {

	private FeedForwardNetwork net;
	int[] nodeCounts = { 3, 2, 1 };
	
	public NNWeightsValidationEvaluationFunction() {
		FeedForwardNeuralNetworkFactory factory = new FeedForwardNeuralNetworkFactory();
		net = factory.createClassificationNetwork(nodeCounts);
	}

	@Override
	public double value(Instance d) {
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
