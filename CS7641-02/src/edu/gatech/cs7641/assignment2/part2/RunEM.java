package edu.gatech.cs7641.assignment2.part2;

import java.io.IOException;

import dist.MixtureDistribution;
import edu.gatech.cs7641.assignment2.part2.support.DataLoader;
import edu.gatech.cs7641.assignment2.part2.support.URLDataLoader;
import func.EMClusterer;
import shared.DataSet;
import shared.Instance;
import shared.reader.DataSetLabelBinarySeperator;

public class RunEM {

	private static final int STARTING_K = 2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataSet set;
		try {
			set = URLDataLoader.getVotes();
			//DataSetLabelBinarySeperator.seperateLabels(set);
			compareEMClustersOverK(set, 0.001, 1, 2);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public static void compareEMClustersOverK(DataSet set, double tolerance,
			int maxIterations, int maxValueOfK) {
		int dim = maxValueOfK + 1 - STARTING_K;
		int off = maxValueOfK + 1 - dim;
		EMClusterer em;
		EMClusterer[] ems = new EMClusterer[dim];
		for (int k = STARTING_K; k <= maxValueOfK; k++) {
			em = new EMClusterer(k, tolerance, maxIterations);
			em.estimate(set);
			ems[k-off] = em;
		}
	}

}
