package edu.gatech.cs7641.assignment2.part1.debris;

import opt.example.ContinuousPeaksEvaluationFunction;
import opt.example.CountOnesEvaluationFunction;
import opt.example.FlipFlopEvaluationFunction;
import opt.example.FourPeaksEvaluationFunction;

public class RunSelectedOptimizationProblems {
	
	public static int BITS=80;
	public static int SAMPLES=10;

	public static void main(String[] args) {
		BitStringOptimizationProcedure bs = new BitStringOptimizationProcedure();
		bs.seedRandom(1L);
		bs.run(new FlipFlopEvaluationFunction(), BITS, SAMPLES, 1000);
		bs.seedRandom(1L);
		bs.run(new FourPeaksEvaluationFunction(10), BITS, SAMPLES, 1000);
		bs.seedRandom(1L);
		bs.run(new CountOnesEvaluationFunction(), BITS, SAMPLES, 1000);
		bs.seedRandom(1L);
		bs.run(new ContinuousPeaksEvaluationFunction(10), BITS, SAMPLES, 1000);
	}

}
