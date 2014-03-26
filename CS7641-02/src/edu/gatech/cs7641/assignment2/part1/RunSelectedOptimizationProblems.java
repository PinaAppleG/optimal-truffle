package edu.gatech.cs7641.assignment2.part1;

import opt.example.FlipFlopEvaluationFunction;

public class RunSelectedOptimizationProblems {

	public static void main(String[] args) {
		BitStringOptimizationProcedure bs = new BitStringOptimizationProcedure();
		bs.seedRandom(1L);
		bs.run(new FlipFlopEvaluationFunction(), 100, 100, -1);
	}

}
