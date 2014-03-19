package edu.gatech.cs7641.assignment2;

import edu.gatech.cs7641.assignment2.algorithms.GeneticAlgorithm;
import edu.gatech.cs7641.assignment2.algorithms.RandomizedHillClimbing;
import edu.gatech.cs7641.assignment2.algorithms.SimulatedAnnealing;
import edu.gatech.cs7641.assignment2.model.LocalSpace;
import edu.gatech.cs7641.assignment2.model.Location;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NNWeightsProblem nnWeights = new NNWeightsProblem();
		System.out.println("Optimize NNWeights problem with RHC");
		RandomizedHillClimbing rhc = new RandomizedHillClimbing();
		Location rhcOptimum = rhc.randomSearch(nnWeights, 0.1d, 10);
		System.out.println(rhcOptimum);
		System.out.println("Peak Fitness: "+nnWeights.valueOf(rhcOptimum));
		System.out.println("Training accuracy: "+nnWeights.trainingAccuracyOf(rhcOptimum));
		System.out.println("Prediction accuracy: "+nnWeights.fullAccuracyOf(rhcOptimum));
		
		System.out.println();
		System.out.println();
		
		System.out.println("Optimize NNWeights problem with Simulated Annealing");
		SimulatedAnnealing sa = new SimulatedAnnealing();
		Location saOptimum = sa.search(nnWeights, 0.1d, 10);
		System.out.println(saOptimum);
		System.out.println("Peak Fitness: "+nnWeights.valueOf(saOptimum));
		System.out.println("Training accuracy: "+nnWeights.trainingAccuracyOf(saOptimum));
		System.out.println("Prediction accuracy: "+nnWeights.fullAccuracyOf(saOptimum));
		
		System.out.println();
		System.out.println();
		
		System.out.println("Optimize NNWeights problem with Genetic Algorithm");
		GeneticAlgorithm ga = new GeneticAlgorithm();
		Location gaOptimum = ga.search(nnWeights,100, 10, 0.001);
		System.out.println(gaOptimum);
		System.out.println("Peak Fitness: "+nnWeights.valueOf(gaOptimum));
		System.out.println("Training accuracy: "+nnWeights.trainingAccuracyOf(gaOptimum));
		System.out.println("Prediction accuracy: "+nnWeights.fullAccuracyOf(gaOptimum));
		
		
		System.out.println("Randomized Hill Climbing Network");
		nnWeights.printNetwork((NNWeightsLocation)rhcOptimum);		
		System.out.println();
		System.out.println();	
		System.out.println("Simulated Annealing Network");
		nnWeights.printNetwork((NNWeightsLocation)saOptimum);		
		System.out.println();
		System.out.println();	
		System.out.println("My Genetic Algorithm Network");
		nnWeights.printNetwork((NNWeightsLocation)gaOptimum);		
		System.out.println();
		System.out.println();
	}

}
