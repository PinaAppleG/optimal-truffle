package edu.gatech.cs7641.assignment2;

import edu.gatech.cs7641.assignment2.model.Location;

public class NNWeightsLocation implements Location {

	private double[] weights;
	
	private NNWeightsLocation add(NNWeightsLocation a,NNWeightsLocation b) {
		double[] weightsA = a.getWeights();
		double[] weightsB = b.getWeights();
		double[] sum = new double[weightsA.length];
		for (int i = 0; i < weightsA.length; i++)
			sum[i] =weightsA[i]+weightsB[i];
		return new NNWeightsLocation(sum);
	}

	public double[] getWeights() {
		return weights;
	}

	public NNWeightsLocation(double[] weights) {
		this.weights=weights;
	}

	@Override
	public void print() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("Weights:\n");
		for (int i=0; i<weights.length; i++) sbuf.append("["+i+"]"+weights[i]+"\n");
		return sbuf.toString();
	}

	@Override
	public Location mateWith(Location location) {
		return add(this, (NNWeightsLocation)location);
	}

}
