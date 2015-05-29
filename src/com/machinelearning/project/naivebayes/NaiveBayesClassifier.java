//package com.machinelearning.project.naivebayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * This class implements the Naive Bayes algorithm for Genre classification.
 * 
 */
public class NaiveBayesClassifier {
	public static TreeMap<String, SongData> trainDataMap = new TreeMap<String, SongData>();
	public static TreeMap<String, SongData> testDataMap = new TreeMap<String, SongData>();
	public static Integer[] countOfGenres = new Integer[5];
	public static Double[] probabilityOfGenres = new Double[5];
	public static ArrayList<String> wordSet = new ArrayList<String>();
	public static HashMap<String, Double[]> condProbMap = new HashMap<String, Double[]>();
	public static ArrayList<String> testGenreList = new ArrayList<String>();
	public static ArrayList<String> resultGenreForTest = new ArrayList<String>();
	public static HashMap<String, String> resultMap = new HashMap<String, String>();
	

	public static ArrayList<String> getResultGenreForTest() {
		return resultGenreForTest;
	}

	public static void setResultGenreForTest(ArrayList<String> resultGenreForTest) {
		NaiveBayesClassifier.resultGenreForTest = resultGenreForTest;
	}
	

	public static HashMap<String, String> getResultMap() {
		return resultMap;
	}

	public static void setResultMap(HashMap<String, String> resultMap) {
		NaiveBayesClassifier.resultMap = resultMap;
	}

	public static void readDataSet()
	{
		BufferedReader bReader = null;
		try {
			File file = new File("Random_Lyrics_Dataset.csv");
			bReader = new BufferedReader(new FileReader(file));
			String currentLine = null;
			bReader.readLine();
			int count=0;
			Arrays.fill(countOfGenres, 0);
			while((currentLine=bReader.readLine())!=null && count != 12000000)
			{
				String[] currRecord = currentLine.split(",");
				if(trainDataMap.containsKey(currRecord[0].trim()))
				{
					SongData sd = trainDataMap.get(currRecord[0].trim());
					sd.getWordCount().put(currRecord[1].trim(), Integer.parseInt(currRecord[2].trim()));
				}
				else 
				{
					SongData songData = new SongData();
					songData.setGenre(currRecord[3]);
					songData.setTrackId(currRecord[0]);
					songData.setWordCount(new HashMap<String, Integer>());
					songData.getWordCount().put(currRecord[1], Integer.parseInt(currRecord[2]));
					trainDataMap.put(currRecord[0].trim(), songData);
					switch(currRecord[3].trim())
					{
					case "rock" : countOfGenres[0]+=1; break;
					case "alternative" : countOfGenres[1]+=1;break;
					case "pop" : countOfGenres[2]+=1; break;
					case "indie" : countOfGenres[3]+=1; break;
					case "electronic" : countOfGenres[4]+=1; break;
					}
				}
				if(!wordSet.contains(currRecord[1].trim()))
				{
					wordSet.add(currRecord[1].trim());
				}
				count++;
			}
			while((currentLine=bReader.readLine())!=null)
			{
				String[] currRecord = currentLine.split(",");
				if(testDataMap.containsKey(currRecord[0].trim()))
				{
					SongData sd = testDataMap.get(currRecord[0].trim());
					sd.getWordCount().put(currRecord[1].trim(), Integer.parseInt(currRecord[2].trim()));
				}
				else 
				{
					SongData songData = new SongData();
					songData.setGenre(currRecord[3]);
					songData.setTrackId(currRecord[0]);
					songData.setWordCount(new HashMap<String, Integer>());
					songData.getWordCount().put(currRecord[1], Integer.parseInt(currRecord[2]));
					testDataMap.put(currRecord[0].trim(), songData);
					testGenreList.add(currRecord[3]);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if(bReader != null)
				{
					bReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void calculateLabelProbability()
	{
		for(int i=0;i<5;i++)
		{
			probabilityOfGenres[i]= (double)countOfGenres[i]/(double)trainDataMap.size();
		}
	}

	/**
	 * Calculate the conditional probability of each word
	 * It stores the log (base 10) value, as the same is required for calculation in Test data
	 */
	public static void calculateConditionalProbability()
	{
		int[] count = new int[5];
		Double[] condProbs = new Double[5];
		for(int i=0;i<wordSet.size();i++)
		{
			Arrays.fill(count,0);
			String word = wordSet.get(i);
			//Retrieve the word counts for zero and one class labels
			for(SongData song: trainDataMap.values())
			{
				int curWordCount = song.getWordCount().get(word)==null?0:song.getWordCount().get(word);
				switch(song.getGenre())
				{
				case "rock" : count[0]+=curWordCount; break;
				case "alternative" : count[1]+=curWordCount;break;
				case "pop" : count[2]+=curWordCount; break;
				case "indie" : count[3]+=curWordCount; break;
				case "electronic" : count[4]+=curWordCount; break;
				}
			}

			for(int k=0;k<5;k++)
			{
				condProbs[k] = Math.log10(((double)(count[k]+1))/((double)(countOfGenres[k]+wordSet.size())));
			}
			condProbMap.put(word, condProbs);
		}
	}


	/**
	 * Function to read the Test data csv file and predict the result class label
	 * for each document.
	 */
	public static void predictGenreForTestDataset()
	{
		for(SongData sd : testDataMap.values())
		{
			Double[] probabilities = probabilityOfGenres;
			//For each word in the document calculate the probability of being in
			//class 0 or class 1
			for(Map.Entry<String, Integer> wordEntry: sd.getWordCount().entrySet())
			{
				Integer wordCount = wordEntry.getValue();
				for(int k=0;k<5;k++)
				{
					probabilities[k] += (double)wordCount*condProbMap.get(wordEntry.getKey())[k];
				}
			}
			Integer resultGenre = findMaxProbability(probabilities);
			switch(resultGenre)
			{
			case 0 : resultGenreForTest.add("rock");
			resultMap.put(sd.getTrackId(), "rock");
			break;
			case 1 : resultGenreForTest.add("alternative");
			resultMap.put(sd.getTrackId(), "alternative");
			break;
			case 2 : resultGenreForTest.add("pop");
			resultMap.put(sd.getTrackId(), "pop");
			break;
			case 3 : resultGenreForTest.add("indie");
			resultMap.put(sd.getTrackId(), "indie");
			break;
			case 4 : resultGenreForTest.add("electronic");
			resultMap.put(sd.getTrackId(), "electronic");
			break;
			}
		}

	}

	private static Integer findMaxProbability(Double[] probabilities) {
		int max=0;
		for(int i=0;i<5;i++)
		{
			if(probabilities[i]>probabilities[max])
			{
				max=i;
			}
		}
		return max;

	}


	/**
	 * Function to calculate the accuracy of the predicted genres
	 * for the test data
	 * @param testLabelFile
	 * @return
	 */
	public static Double calculateAccuracy()
	{
		Double accuracy =0.0;
		int matching =0;
		for(int i=0;i<testGenreList.size();i++)
		{
			if(testGenreList.get(i).equalsIgnoreCase(resultGenreForTest.get(i)))
			{
				matching++;
			}
		}
		accuracy = (double)matching/(double)testGenreList.size();
		return accuracy*100;
	}

	public static void main(String[] args)
	{
		readDataSet();
		calculateLabelProbability();
		calculateConditionalProbability();
		predictGenreForTestDataset();
		Double accuracy = calculateAccuracy();
		System.out.println("Accuracy - "+accuracy);
	}

}
