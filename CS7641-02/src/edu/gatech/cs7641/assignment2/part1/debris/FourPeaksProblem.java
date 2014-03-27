package edu.gatech.cs7641.assignment2.part1.debris;

import java.util.Random;

import opt.EvaluationFunction;
import opt.HillClimbingProblem;
import opt.example.FourPeaksEvaluationFunction;
import shared.Instance;
import util.linalg.Vector;

public class FourPeaksProblem implements HillClimbingProblem {

	EvaluationFunction training;
	int t;
	Random random;
	
	public FourPeaksProblem(Random random, int t) {
		this.training = new FourPeaksEvaluationFunction(t);
		this.t=t;
		this.random=random;
	}

	@Override
	public double value(Instance d) {
		return training.value(d);
	}

	@Override
	public Instance random() {
		double[] bits = new double[t*10];
		for (int i=0; i<bits.length; i++) bits[i]=random.nextBoolean()?1:0;
		return new Instance(bits);
	}

	@Override
	public Instance neighbor(Instance d) {
		Vector bits = d.getData();
		int i=random.nextInt(bits.size());
		bits.set(i, bits.get(i)==0?1:0);
		return new Instance(bits);		
	}

}
