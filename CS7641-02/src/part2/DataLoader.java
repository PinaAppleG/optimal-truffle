package part2;

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
		} catch (Exception e) {
			if(e.getClass().isInstance(IOException.class)) throw (IOException) e;
			else if(e.getClass().isInstance(RuntimeException.class)) throw (RuntimeException) e;
			else throw new RuntimeException(e);
		}
	}
}
