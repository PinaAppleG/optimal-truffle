package edu.gatech.cs7641.assignment2.part1.debris;


public class MyNNWeights implements Location {

	private double[] weights;
	
	private MyNNWeights add(MyNNWeights a,MyNNWeights b) {
		double[] weightsA = a.getWeights();
		double[] weightsB = b.getWeights();
		double[] sum = new double[weightsA.length];
		for (int i = 0; i < weightsA.length; i++)
			sum[i] =weightsA[i]+weightsB[i];
		return new MyNNWeights(sum);
	}

	public double[] getWeights() {
		return weights;
	}

	public MyNNWeights(double[] weights) {
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
		return add(this, (MyNNWeights)location);
	}

}
