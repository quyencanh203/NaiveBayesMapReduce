package com.example;
import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class NaiveBayesTrainMapper extends MapReduceBase implements Mapper<LongWritable, Text,Text,DoubleWritable>{
   
	String delimiter,continousVariables;	
	int targetVariable,numColums;
	HashSet<Integer> continousVariablesIndex;
	
	public HashSet<Integer> splitvariables(String varString){
		HashSet<Integer> hs = new HashSet<Integer>();
	    StringTokenizer tok = new StringTokenizer(varString,",");
	    while(tok.hasMoreElements())
	    	hs.add(Integer.parseInt(tok.nextToken()));
		return hs;
	}
	
	@Override
	 public void configure(JobConf conf){
		delimiter = conf.get("delimiter");
		numColums = conf.getInt("numColumns", 0);
		continousVariables = conf.get("continousVariables");
	    targetVariable = conf.getInt("targetVariable",0);
	    continousVariablesIndex = new HashSet<Integer>();
	    if(continousVariables!=null)
	    	continousVariablesIndex = splitvariables(continousVariables);
	}
	@Override
	public void map(LongWritable arg0, Text value,
			OutputCollector<Text, DoubleWritable> output, Reporter arg3)
			throws IOException {
		// TODO Auto-generated method stub
		Integer varIndex = 1; 
		String record = value.toString();
		String features[] = record.split(delimiter);
	    for(int i = 0 ;i < numColums ; i++){
	    	if(varIndex!= targetVariable){
	    		if(continousVariablesIndex.contains(varIndex))
	    		 output.collect(new Text(varIndex+"_"+features[targetVariable-1]), new DoubleWritable(Double.parseDouble(features[i])));
	    	}
	    	varIndex ++;
	    }
	    output.collect(new Text(targetVariable+"_"+features[targetVariable-1]), new DoubleWritable(1.0));
	    output.collect(new Text(targetVariable+""), new DoubleWritable(1.0));
	}
}