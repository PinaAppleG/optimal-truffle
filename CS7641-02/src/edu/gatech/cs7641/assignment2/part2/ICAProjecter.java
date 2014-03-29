package edu.gatech.cs7641.assignment2.part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import shared.DataSet;
import shared.DataSetDescription;
import shared.DataSetWriter;
import shared.Instance;
import shared.filt.IndependentComponentAnalysis;
import util.linalg.DenseVector;
import edu.gatech.cs7641.assignment2.part2.support.DataLoader;

public class ICAProjecter {

	public static void main (String[] args) {
		try {
			DataSet set = DataLoader
						.loadData("../datasets/votes/house-votes-84.arff");
			DataSet ica = projectDataNotClass(set,8);
			DataSetWriter writer = new DataSetWriter(ica, "C:/Users/nm5144/git/optimal-truffle/datasets/votes/house-votes-84-ica.csv");
			writer.write();

			set = DataLoader
					.loadData("../datasets/credit/crx.arff");
			System.out.println(set.getDescription());
			ica = projectDataNotClass(set,8);
			writer = new DataSetWriter(ica, "C:/Users/nm5144/git/optimal-truffle/datasets/credit/crx-ica.data");
			System.out.println(ica.getDescription());
			writer.write();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static DataSet projectDataNotClass(final DataSet set, int components) {
			DenseVector dv;
			Instance[] data=new Instance[set.size()], classLabels=new Instance[set.size()];
			for(int i=0; i<set.size(); i++) {
				dv=(DenseVector)set.get(i).getData();
				data[i]=new Instance(dv.get(0, dv.size()-1));
				classLabels[i]=new Instance(dv);
			}
			System.gc();
			DataSet projection = new DataSet(data);
			IndependentComponentAnalysis ica = new IndependentComponentAnalysis(projection, components);
			ica.filter(projection);
			data=new Instance[projection.size()];
			int l=projection.get(0).size();
			for(int i=0; i<projection.size(); i++) {
				double[] vals = new double[l+1];
				for (int j=0; j<l; j++) vals[j]=projection.get(i).getContinuous(j);
				vals[l]=classLabels[i].getContinuous(0);
				dv=new DenseVector(vals);
				data[i]=new Instance(dv);
			}
			return new DataSet(data);
	}
	
	public static DataSet readCSV(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        List<Instance> data = new ArrayList<Instance>();
        line = br.readLine();
        if (line==null) {
        	br.close();
        	return null;
        }
        String[] split = line.split("\\s*,\\s*");
        int l=split.length;
		int[] ranges=new int[l];
		Hashtable<String,Double>[] labels = new Hashtable[l];
		for(int i=0;i<l;i++) {
			ranges[i]=0;
			labels[i]=new Hashtable<String,Double>();
		}
		do {
            split=line.split("\\s*,\\s*");
            double[] input = new double[l];
            for (int i = 0; i < l; i++) {
                try {input[i] = Double.parseDouble(split[i].trim());}
                catch (NumberFormatException e) {
                	if(labels[i].contains(split[i].trim())) {
                		input[i]=labels[i].get(split[i].trim()).doubleValue();
                	} else {
                		labels[i].put(split[i].trim(),new Double(labels[i].size()));
                		ranges[i]=labels[i].size();
                		input[i]=labels[i].get(split[i].trim()).doubleValue();
                	}
                }
            } 
            Instance instance = new Instance(input);
            data.add(instance);
        } while ((line = br.readLine()) != null);
        br.close();
        Instance[] instances = data.toArray(new Instance[0]);
        DataSet set = new DataSet(instances);
        set.setDescription(new DataSetDescription(set));
        return set;
	}

}
