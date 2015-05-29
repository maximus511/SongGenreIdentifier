//package com.machinelearning.project.ensemble;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManagerFactorySpi;

import org.xml.sax.HandlerBase;

import com.machinelearning.project.kmeans.KMeansAlgorithm;
import com.machinelearning.project.knn.KNN;
import com.machinelearning.project.knn.Word;
import com.machinelearning.project.naivebayes.NaiveBayesClassifier;

public class EnsembleClassifier {

	public static HashMap<String, String> finalResult = new HashMap<String, String>();

	public static void min(String[] args)
	{
		KMeansAlgorithm.kmeans(new String[]{"TestProg.txt","5"});
		NaiveBayesClassifier.naiveBayes();
		KNN.callKNN();
		Integer[] result = new Integer[5];
		for(Map.Entry<String, String> id : NaiveBayesClassifier.resultMap.entrySet())
		{
			switch (id.getValue())
			{
			case "rock":
			{
				result[0]+=1;
				break;
			}

			case "alternative":
			{
				result[1]+=1;
				break;
			}

			case "pop":
			{
				result[2]+=1;
				break;
			}

			case "electronic":
			{
				result[3]+=1;
				break;
			}

			case "indie":
			{
				result[4]+=1;
				break;
			}
			}

			if(KNN.rockGenreList.contains(id.getKey()))
			{
				result[0]+=1;
			}
			else if(KNN.alternativeGenreList.contains(id.getKey()))
			{
				result[1]+=1;
			}
			else if(KNN.popGenreList.contains(id.getKey()))
			{
				result[2]+=1;
			}
			else if(KNN.electronicGenreList.contains(id.getKey()))
			{
				result[3]+=1;
			}
			else if(KNN.indieGenreList.contains(id.getKey()))
			{
				result[4]+=1;
			}

			switch(KMeansAlgorithm.result.get(id.getKey()))
			{
			case "rock":
			{
				result[0]+=1;
				break;
			}

			case "alternative":
			{
				result[1]+=1;
				break;
			}

			case "pop":
			{
				result[2]+=1;
				break;
			}

			case "electronic":
			{
				result[3]+=1;
				break;
			}

			case "indie":
			{
				result[4]+=1;
				break;
			}
			default : break;
			}

			int max=result[0];
			int index=0;
			for(int i=0;i<5;i++)
			{
				if(max<=result[i])
				{
					max=result[i];
					index=i;
				}
			}

			switch (index) 
			{
			case 0:
			{
				finalResult.put(id.getKey(),"rock");
				break;
			}

			case 1:
			{
				finalResult.put(id.getKey(),"alternative");
				break;
			}

			case 2:
			{
				finalResult.put(id.getKey(),"pop");
				break;
			}
			case 3:
			{
				finalResult.put(id.getKey(),"electronic");
				break;
			}
			case 4:
			{
				finalResult.put(id.getKey(),"indie");
				break;
			}
			}
		}

		System.out.println("Accuracy- "+checkAccuracy());

	}

	private static double checkAccuracy() {
		int matching =0;
		for(Map.Entry<String,String> entry : finalResult.entrySet())
		{
			if(KNN.testMap.containsKey(entry.getKey()))
			{
				if(KNN.testMap.get(entry.getKey()).get(0).getGenreValue().equals(entry.getValue()))
				{
					matching++;
				}
			}
		}
		return ((double)matching/(double)finalResult.size());

	}
}

