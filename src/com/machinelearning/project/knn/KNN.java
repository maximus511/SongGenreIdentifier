package com.machinelearning.project.knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KNN 
{

	static HashMap<String,ArrayList<Word>> inputMap = new HashMap<String,ArrayList<Word>>();
	public static HashMap<String,ArrayList<Word>> testMap = new HashMap<String,ArrayList<Word>>();
	static ArrayList<String> wordList = new ArrayList<String>();
	static ArrayList<String> keys = new ArrayList<String>();
	static HashMap<String , ArrayList<Word>> rockTagMap = new HashMap<String, ArrayList<Word>>();
	static HashMap<String , ArrayList<Word>> alternativeTagMap = new HashMap<String, ArrayList<Word>>();
	static HashMap<String , ArrayList<Word>> popTagMap = new HashMap<String, ArrayList<Word>>();
	static HashMap<String , ArrayList<Word>> indieTagMap = new HashMap<String, ArrayList<Word>>();
	static HashMap<String , ArrayList<Word>> electronicTagMap = new HashMap<String, ArrayList<Word>>();
	public static ArrayList<String> rockGenreList = new ArrayList<String>();
	public static ArrayList<String> indieGenreList = new ArrayList<String>();
	public static ArrayList<String> alternativeGenreList = new ArrayList<String>();
	public static ArrayList<String> popGenreList = new ArrayList<String>();
	public static ArrayList<String> electronicGenreList = new ArrayList<String>();
	static ArrayList<Word> rockReferenceList = new ArrayList<Word>();
	static ArrayList<Word> popReferenceList = new ArrayList<Word>();
	static ArrayList<Word> alternateReferenceList = new ArrayList<Word>();
	static ArrayList<Word> indieReferenceList = new ArrayList<Word>();
	static ArrayList<Word> electronicReferenceList = new ArrayList<Word>();
	private static ArrayList<String> testWordList = new ArrayList<String>();


	
	public static ArrayList<Word> getRockReferenceList() {
		return rockReferenceList;
	}

	public static void setRockReferenceList(ArrayList<Word> rockReferenceList) {
		KNN.rockReferenceList = rockReferenceList;
	}

	public static ArrayList<Word> getPopReferenceList() {
		return popReferenceList;
	}

	public static void setPopReferenceList(ArrayList<Word> popReferenceList) {
		KNN.popReferenceList = popReferenceList;
	}

	public static ArrayList<Word> getAlternateReferenceList() {
		return alternateReferenceList;
	}

	public static void setAlternateReferenceList(
			ArrayList<Word> alternateReferenceList) {
		KNN.alternateReferenceList = alternateReferenceList;
	}

	public static ArrayList<Word> getIndieReferenceList() {
		return indieReferenceList;
	}

	public static void setIndieReferenceList(ArrayList<Word> indieReferenceList) {
		KNN.indieReferenceList = indieReferenceList;
	}

	public static ArrayList<Word> getElectronicReferenceList() {
		return electronicReferenceList;
	}

	public static void setElectronicReferenceList(
			ArrayList<Word> electronicReferenceList) {
		KNN.electronicReferenceList = electronicReferenceList;
	}

	public static void readTrainingFile(String fileName)
	{
		String line = null; 
		BufferedReader br = null;
		Word lyricWord = null;
		File inpFile = new File(fileName);
		try
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(inpFile)));
			line = br.readLine();

			String[] column = line.split(",");
			for(String c : column)
			{
				wordList.add(c);			
			}

			while ((line = br.readLine()) != null)
			{

				String[] inputValues = line.split(",");
				String word = inputValues[0];
				//check if the word is in the hashmap
				if(inputMap.get(word)==null)
				{
					inputMap.put(word, new ArrayList<Word>());
				}			

				for(int i=1; i<wordList.size()-1;i++)
				{
					lyricWord = new Word(wordList.get(i) , Double.parseDouble(inputValues[i]),inputValues[(inputValues.length) -1]);
					inputMap.get(word).add(lyricWord);
				}
			}
			GenerateReferenceMapValues();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public static void main(String[] args) {
	//public static void callKNN() {
		// TODO Auto-generated method stub
		readTrainingFile("TrainProg.csv");
		String line = null; 
		BufferedReader br = null;
		Word lyricWord = null;
		File inpFile = new File("TestProg.csv");
		try
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(inpFile)));
			line = br.readLine();

			String[] column = line.split(",");
			for(String c : column)
			{
				testWordList.add(c);			
			}

			while ((line = br.readLine()) != null)
			{

				String[] inputValues = line.split(",");
				String word = inputValues[0];
				//check if the word is in the hashmap
				if(testMap.get(word)==null)
				{
					testMap.put(word, new ArrayList<Word>());
				}			

				for(int i=1; i<testWordList.size()-1;i++)
				{
					lyricWord = new Word(testWordList.get(i) , Double.parseDouble(inputValues[i]),inputValues[(inputValues.length) -1]);
					testMap.get(word).add(lyricWord);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		for( Map.Entry<String, ArrayList<Word>> entry : testMap.entrySet() )
		{
			String key = entry.getKey();
			keys.add(key);
			ArrayList<Word> mapValues = entry.getValue();
			//Collections.sort(mapValues);
			testMap.put(key, mapValues);
		}

		//GenerateReferenceMapValues();
		CalculateMinimumDistance(450);
	}

	//iterate thru each hashmap values , check if any particular value has rock genre , if so then take one instance of rock genre



	private static void CalculateMinimumDistance(int k)
	{
		int loop =0;
		for(Map.Entry<String,ArrayList<Word>> mapElements : testMap.entrySet())
		{
			HashMap<String, Double> minimumDistance = new HashMap<String, Double>();
			int minGenre = Integer.MIN_VALUE;

			//	String trackId = mapElements.getKey();
			String trackId = mapElements.getKey();
			ArrayList<Word> lyricsWord = mapElements.getValue();
			//FindDistance(lyricsWord);
			minimumDistance.putAll(CalculateDistance.FindDistance(lyricsWord, rockTagMap));
			minimumDistance.putAll(CalculateDistance.FindDistance(lyricsWord, popTagMap));
			minimumDistance.putAll(CalculateDistance.FindDistance(lyricsWord, alternativeTagMap));
			minimumDistance.putAll(CalculateDistance.FindDistance(lyricsWord, indieTagMap));
			minimumDistance.putAll(CalculateDistance.FindDistance(lyricsWord, electronicTagMap));
			ArrayList<Double> sortedValues = new ArrayList<Double>(minimumDistance.values());
			Collections.sort(sortedValues);
			HashMap<String, Double> sortedDistance = new HashMap<String, Double>();
			int rec=0;
			for(Double d : sortedValues)
			{
				for(Map.Entry<String,Double> e : minimumDistance.entrySet())
				{
					if(e.getValue().equals(d))
					{
						sortedDistance.put(e.getKey(), d);
						rec++;
						break;
					}
					if(rec==k)
					{
						break;
					}
				}

			}
			//System.out.println(" Values - "+sortedValues.toString());
			Set<String> keys = sortedDistance.keySet();
			Integer[] maxPoints = new Integer[5];
			Arrays.fill(maxPoints, 0);

			for(String id: keys)
			{
				Word w = inputMap.get(id).get(0);
				String genre = w.getGenreValue();
				switch (genre) 
				{
				case "rock":
				{
					maxPoints[0]+=1;
					break;
				}

				case "alternative":
				{
					maxPoints[1]+=1;	
					break;
				}

				case "pop":
				{
					maxPoints[2]+=1;	
					break;
				}

				case "electronic":
				{
					maxPoints[3]+=1;	
					break;
				}

				case "indie":
				{
					maxPoints[4]+=1;
					break;
				}	

				}
			}
			Integer max = maxPoints[0];
			for(int i=0 ; i< maxPoints.length;i++)
			{
				if (maxPoints[i]>=max) 
				{
					max=maxPoints[i];
					minGenre = i;
				}
			}
			switch (minGenre) 
			{
			case 0:
			{
				rockGenreList.add(trackId);
				break;
			}

			case 1:
			{
				alternativeGenreList.add(trackId);
				break;
			}

			case 2:
			{
				popGenreList.add(trackId);
				break;
			}
			case 3:
			{
				indieGenreList.add(trackId);
				break;
			}
			case 4:
			{
				electronicGenreList.add(trackId);
				break;
			}
			}

		}
		System.out.println("Rock Genre List " + " " + rockGenreList.size());
		System.out.println("Alternate Genre List " + " " + alternativeGenreList.size());
		System.out.println("Pop Genre List " + " " + popGenreList.size());
		System.out.println("Indie Genre List " + " " + indieGenreList.size());
		System.out.println("Electronic Genre List " + " " + electronicGenreList.size());

	}

	private static void GenerateReferenceMapValues() 
	{
		// TODO Auto-generated method stub
		for( Map.Entry<String, ArrayList<Word>> entryMap : inputMap.entrySet())
		{
			ArrayList<Word> mapValue = entryMap.getValue();

			Word checkWord = mapValue.get(0);
			// check for word Genre
			String wordGenre = checkWord.getGenreValue().toLowerCase();

			switch (wordGenre) 
			{
			case "rock":
			{
				rockTagMap.put(entryMap.getKey(), mapValue);
				break;
			}

			case "alternative":
			{
				alternativeTagMap.put(entryMap.getKey(), mapValue);
				break;
			}

			case "pop":
			{
				popTagMap.put(entryMap.getKey(), mapValue);
				break;
			}

			case "electronic":
			{
				electronicTagMap.put(entryMap.getKey(), mapValue);
				break;
			}

			case "indie":
			{
				indieTagMap.put(entryMap.getKey(), mapValue);
				break;
			}	
			
			}
		}

	}

}