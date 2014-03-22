package part2;

import java.io.IOException;

import dist.Distribution;

import shared.DataSet;
import shared.Instance;
import shared.filt.IndependentComponentAnalysis;
import shared.reader.DataSetLabelBinarySeperator;
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
			set = DataLoader
					.loadData("/Users/sephirothxxsama/git/abagail/bin/shared/test/abalone.arff");
			DataSetLabelBinarySeperator.seperateLabels(set);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println("\nRun on Binarized Dataset\n");
		compareKMeansOverK(set, DEFAULT_MAX_K);
		
		IndependentComponentAnalysis ica = 
				new IndependentComponentAnalysis(set,-1);
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
		double[] logp = new double[dim];
		double d;
		for (int k = 0; k < dim; k++) {
			logp[k] = 0;
			km = new KMeansClusterer(k + off);
			km.estimate(set);
			centers[k] = km.getClusterCenters();
			for (int i = 0; i < set.size(); i++) {
				d = km.logp(set.get(i));
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
