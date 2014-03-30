package edu.gatech.cs7641.assignment2.part2.debris;

import java.io.IOException;

import shared.DataSet;
import shared.DistanceMeasure;
import shared.EuclideanDistance;
import shared.Instance;
import shared.filt.IndependentComponentAnalysis;
import shared.reader.DataSetLabelBinarySeperator;
import edu.gatech.cs7641.assignment2.part2.support.DataLoader;
import edu.gatech.cs7641.assignment2.part2.support.URLDataLoader;
import func.KMeansClusterer;

public class RunKMeans {

	private static final int DEFAULT_MAX_K = 32;
	private static final int STARTING_K = 2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataSet set;
		try {
			set = DataLoader.loadData("C:/Users/nm5144/git/optimal-truffle/datasets/votes/house-votes-84.arff");
			//DataSetLabelBinarySeperator.seperateLabels(set);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("\nRun on Binarized Dataset\n");
		compareKMeansOverK(set, DEFAULT_MAX_K);

		IndependentComponentAnalysis ica = new IndependentComponentAnalysis(
				set, -1);
		ica.filter(set);
		System.out.println("\nRun on Binarized Dataset projected over ICA\n");
		compareKMeansOverK(set, DEFAULT_MAX_K);

	}

	public static void compareKMeansOverK(DataSet set, int maxValueOfK) {
		KMeansClusterer km;
		int dim = maxValueOfK + 1 - STARTING_K;
		int off = maxValueOfK + 1 - dim;
		int bestK = 0;
		Instance[][] centers = new Instance[dim][];
		int clusterCenter;
		DistanceMeasure dist = new EuclideanDistance();
		double[] logp = new double[dim];
		double d;
		for (int k = 0; k < dim; k++) {
			logp[k] = 0;
			km = new KMeansClusterer(k + off);
			km.estimate(set);
			centers[k] = km.getClusterCenters();
			for (int i = 0; i < set.size(); i++) {
				clusterCenter = (int) km.value(set.get(i)).getData().get(0);
				d = dist.value(set.get(i), centers[k][clusterCenter]);
				if (d == d)
					logp[k] += d;
			}
			System.out.println((k + off) + ", " + logp[k]);
			if (logp[k] > logp[bestK])
				bestK = k;
		}
		System.out.println("Best K=" + (bestK + off));
		System.out.println("Centers:");
		for (int i = 0; i < centers[bestK].length; i++) {
			System.out.println(centers[bestK][i].getData().toString());
		}
	}

}
