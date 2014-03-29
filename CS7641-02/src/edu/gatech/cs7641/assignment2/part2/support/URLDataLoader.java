package edu.gatech.cs7641.assignment2.part2.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import shared.AttributeType;
import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;
import util.linalg.DenseVector;

public class URLDataLoader {
	public static final String VOTES = "https://archive.ics.uci.edu/ml/machine-learning-databases/voting-records/house-votes-84.data";
	public static final String SPONGE = "https://archive.ics.uci.edu/ml/machine-learning-databases/sponge/sponge.data";

	public static DataSet getVotes() throws IOException, MalformedURLException {
		HttpURLConnection conn = null;
		BufferedReader br = null;
		try {
			URL url = new URL(VOTES);
			conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			br = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			ArrayList<Instance> instances = new ArrayList<Instance>();
			Pattern pattern = Pattern.compile("[ ,]+");
			while ((line = br.readLine()) != null) {
				String[] split = pattern.split(line.trim());
				double[] input = new double[split.length];
				input[0]=split[0].equals("republican")?1:0;
				for (int i = 1; i < input.length; i++) {
					input[i] = split[i].equals("?")?0:split[i].equals("y")?1:2;
				}
				instances.add(new Instance(input));
			}
			AttributeType[] types = new AttributeType[instances.get(0).size()];
			double[] max = new double[types.length];
			for (int i = 0; i < types.length; i++) {
				types[i]=AttributeType.DISCRETE;
				max[i]=3;
			}
			max[0]=2;
			DataSetDescription description = new DataSetDescription(types,new DenseVector(max ) );
			DataSet data = new DataSet(instances.toArray(new Instance[instances
					.size()]), description);
			data.setDescription(description);
			return data;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			try {
				if (br != null)
					br.close();
			} catch (IOException e2) {

			}
			if (conn != null)
				conn.disconnect();
			throw e;
		}
	}

}
