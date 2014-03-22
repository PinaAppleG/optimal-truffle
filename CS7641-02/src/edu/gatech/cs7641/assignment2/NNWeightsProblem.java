package edu.gatech.cs7641.assignment2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import util.linalg.DenseVector;
import util.linalg.Vector;
import edu.gatech.cs7641.assignment2.model.LocalSpace;
import edu.gatech.cs7641.assignment2.model.Location;
import func.nn.Layer;
import func.nn.Link;
import func.nn.feedfwd.FeedForwardNetwork;
import func.nn.feedfwd.FeedForwardNeuralNetworkFactory;

public class NNWeightsProblem implements LocalSpace {

	private int size=0;
	private static final int SIZELIMIT = 1000;
	private static final double INIT_SCALE = 0.1d;
	private static final double STEP_SCALE = 0.05d;
	FeedForwardNeuralNetworkFactory factory;
	FeedForwardNetwork net;
	int[] nodeCounts = { 3, 2, 1 };
	double[][] training;
	double[][] test;
	Random random;

	public NNWeightsProblem() {
		random = new Random(0L);
		factory = new FeedForwardNeuralNetworkFactory();
		net = factory.createClassificationNetwork(nodeCounts);
		training = new double[SIZELIMIT][];
		try {
			BufferedReader in = new BufferedReader(new FileReader("skin.csv"));
			String line;
			String[] vals;
			double[] v = new double[4];
			int a = 0, b = 0, i, pos = 0;
			while ((line = in.readLine()) != null) {
				size++;
				vals = line.split(",");
				v = new double[4];
				for (i = 0; i < 4; i++)
					v[i] = Double.parseDouble(vals[i]);
				if (v[3] == 1) {
					if (a < SIZELIMIT / 2) {
						training[pos++] = v;
						a++;
					}
				} else {
					if (b < SIZELIMIT / 2) {
						training[pos++] = v;
						b++;
					}
				}
			}
			//System.out.println("a="+a+" b="+b);
			in.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public void printNetwork(NNWeightsLocation weights) {
		net.setWeights(weights.getWeights());
		double[] v = {0,127,255};
		net.setInputValues(new DenseVector(v));
		net.run();
		
		Vector activations;
		
		for (int h=0; h<net.getHiddenLayerCount(); h++) {
			Layer hidden = net.getHiddenLayer(h);
			activations = hidden.getActivations();
			System.out.println("Hidden Layer #"+h+" Activation(s)");
			System.out.println(activations.toString());
			System.out.println("Hidden Layer #"+h+" Link(s)");
			for (Object item: hidden.getLinks()) {
				Link link = (Link) item;
				System.out.printf("%03f * %s -> %s\n",link.getWeight(),link.getInNode().toString(),link.getOutNode().toString());
			}
			System.out.println();
			System.out.println();
		}
		

		Layer output = net.getOutputLayer();
		activations = output.getActivations();
		System.out.println("Output Layer Activation(s)");
		System.out.println(activations.toString());
		System.out.println("Output Layer Link(s)");
		for (Object item: output.getLinks()) {
			Link link = (Link) item;
			System.out.printf("%03f * %s -> %s\n",link.getWeight(),link.getInNode().toString(),link.getOutNode().toString());
		}
		System.out.println();
		System.out.println();
	}

	@Override
	public Location getRandomLocation(Random random) {
		double[] weights = net.getWeights();
		for (int i = 0; i < weights.length; i++) {
			weights[i] = (random.nextBoolean() ? 1 : -1) * random.nextDouble()
					* INIT_SCALE;
		}
		return new NNWeightsLocation(weights);
	}

	@Override
	public double valueOf(Location start) {
		if(start==null) throw new RuntimeException("Value of empty set is undefined");
		NNWeightsLocation weights = (NNWeightsLocation) start;
		net.setWeights(weights.getWeights());
		double fitness = 0;
		Vector output;
		DenseVector input;
		int classification;
		for (double[] v : training) {
			if (v==null) throw new RuntimeException("V is null");
			if (v.length==0) throw new RuntimeException("V has zero length");
			double[] rgb = new double[3];
			System.arraycopy(v, 0, rgb, 0, 3);
			input = new DenseVector(rgb);
			net.setInputValues(input);
			net.run();
			output = net.getOutputValues();
			classification=v[3]==1?0:1;
			//if classification is one, reward high output
			//otherwise reward low output
			fitness += (output.get(0))*classification+
				(-output.get(0))*(1-classification);
		}
		return fitness;
	}

	@Override
	public Location[] neighborhoodOf(Location currentLocation, Random random) {
		double[] weights = ((NNWeightsLocation) currentLocation).getWeights();
		ArrayList<NNWeightsLocation> neighbors = new ArrayList<NNWeightsLocation>();
		for (int i = 0; i < net.getWeights().length; i++) {
			for (int j = 0; j < 2; j++) {
				double[] newWeights = new double[weights.length];
				System.arraycopy(weights, 0, newWeights, 0, weights.length);
				newWeights[i]+=(j==0?-1:1)*STEP_SCALE;
				neighbors.add(new NNWeightsLocation(newWeights));
				//neighbors.add(randomNeighborOf((NNWeights) currentLocation));
			}
		}
		//System.out.println("Neighbors: "+neighbors.size());
		return neighbors.toArray(new NNWeightsLocation[neighbors.size()]);
	}

	@SuppressWarnings("unused")
	private NNWeightsLocation randomNeighborOf(NNWeightsLocation weights,Random random) {
		double[] newWeights = weights.getWeights();
		double[] delta = ((NNWeightsLocation) getRandomLocation(random)).getWeights();
		for (int i = 0; i < newWeights.length; i++)
			newWeights[i] += delta[i];
		return new NNWeightsLocation(newWeights);
	}

	@Override
	public double trainingAccuracyOf(Location optimum) {
		if(optimum==null) throw new RuntimeException("Value of empty set is undefined");
		NNWeightsLocation weights = (NNWeightsLocation) optimum;
		net.setWeights(weights.getWeights());
		double correct = 0;
		DenseVector input;
		int classification, prediction;
		for (double[] v : training) {
			double[] rgb = new double[3];
			System.arraycopy(v, 0, rgb, 0, 3);
			input = new DenseVector(rgb);
			net.setInputValues(input);
			net.run();
			classification=v[3]==1?0:1;
			prediction=net.getBinaryOutputValue()?1:0;
			if(classification==prediction) correct+=1;
		}
		return correct/training.length;
	}

	@Override
	public double fullAccuracyOf(Location optimum) {
		if(optimum==null) throw new RuntimeException("Value of empty set is undefined");
		NNWeightsLocation weights = (NNWeightsLocation) optimum;
		net.setWeights(weights.getWeights());
		double correct = 0;
		DenseVector input;
		int classification, prediction;
		try {
			BufferedReader in = new BufferedReader(new FileReader("skin.csv"));
			String line;
			String[] vals;
			double[] v = new double[4];
			int i;
			while ((line = in.readLine()) != null) {
				vals = line.split(",");
				v = new double[4];
				for (i = 0; i < 4; i++)
					v[i] = Double.parseDouble(vals[i]);
				double[] rgb = new double[3];
				System.arraycopy(v, 0, rgb, 0, 3);
				input = new DenseVector(rgb);
				net.setInputValues(input);
				net.run();
				classification=v[3]==1?0:1;
				prediction=net.getBinaryOutputValue()?1:0;
				if(classification==prediction) correct+=1;
			}
			in.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return correct/size;
	}
}
