package part2;

import java.io.IOException;

import shared.DataSet;
import shared.DataSetWriter;
import shared.Instance;
import shared.filt.IndependentComponentAnalysis;
import shared.reader.ArffDataSetReader;
import shared.reader.CSVDataSetReader;
import shared.reader.DataSetLabelBinarySeperator;
import shared.reader.DataSetReader;
import util.linalg.Matrix;

public class ICAProject {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String file = args[0];
		
		int numberOfComponents;
		if(args.length>1) {
			numberOfComponents = Integer.parseInt(args[1]); 
		} else {
			numberOfComponents=-1;
		}
		
		String outputFile;
		DataSetReader reader;
		DataSet data;

		/* Try for ARFF, failover to CSV */
		if (file.endsWith(".arff")) {
			reader = new ArffDataSetReader(file);
			outputFile = file.substring(0, file.length()-5)+".ica.csv";
		} else {
			reader = new CSVDataSetReader(file);
			outputFile = file.substring(0, file.length()-4)+".ica.csv";
		}
		
		/* Throwing top level Exception really irks me. */
		try {
			data = reader.read();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		/* Binarize nominals */
		DataSetLabelBinarySeperator.seperateLabels(data);
		
		/* ICA */
		IndependentComponentAnalysis ica = 
				new IndependentComponentAnalysis(data,numberOfComponents);
		
		/* ICA Projection */
		Matrix icaProjection = ica.getProjection();
		
		/* Convert projection matrix to DataSet */
		Instance[] rows = new Instance[icaProjection.m()];
		for (int i=0; i<rows.length; i++) {
			rows[i] = new Instance(icaProjection.getRow(i));
		}
		DataSet projection = new DataSet(rows);
		
		/* Write the projected data to a file */
		DataSetWriter writer  = new DataSetWriter(projection, outputFile);
		/* At least this one throws a proper exception */
		try {
			writer.write();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

}
