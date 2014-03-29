package edu.gatech.cs7641.assignment2.part2.support;

import java.io.IOException;

import shared.DataSet;
import shared.reader.ArffDataSetReader;
import shared.reader.CSVDataSetReader;
import shared.reader.DataSetReader;

public class DataLoader {
	public static DataSet loadData(String file) throws IOException {
		DataSetReader reader;
		/* Try for ARFF, failover to CSV */
		if (file.endsWith(".arff")) {
			reader = new ArffDataSetReader(file);
		} else {
			reader = new CSVDataSetReader(file);
		}
		try {
			return reader.read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
