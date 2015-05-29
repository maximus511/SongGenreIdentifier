/**
 * 
 */
package com.machinelearning.project.svm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import com.machinelearning.project.naivebayes.SongData;

/**
 * @author Rahul
 *
 */
public class DataSetFormatter {
	
	public static TreeMap<String, SongData> trainDataMap = new TreeMap<String, SongData>();
	public static TreeMap<String, SongData> testDataMap = new TreeMap<String, SongData>();
	public static ArrayList<String> wordSet = new ArrayList<String>();
	public static String header;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readInputDataSet();
		createFormattedDataset(trainDataMap,"TrainProg.txt");
		createFormattedDataset(testDataMap,"TestProg.txt");
	}

	public static void readInputDataSet()
	{
		BufferedReader bReader = null;
		try {
			File file = new File("Random_Lyrics_Dataset.csv");
			bReader = new BufferedReader(new FileReader(file));
			String currentLine = null;
			header= bReader.readLine();
			int count=0;
			while((currentLine=bReader.readLine())!=null && count != 200)
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
				}
				if(!wordSet.contains(currRecord[1].trim()))
				{
					wordSet.add(currRecord[1].trim());
				}
				count++;
			}
			count=0;
			while((currentLine=bReader.readLine())!=null && count!=30)
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
				}
				count++;
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
	
	public static void createFormattedDataset(TreeMap<String,SongData> trainDataMap, String fileName)
	{
		try {
			 
 
			File file = new File(fileName);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			StringBuffer header = new StringBuffer();
			header.append("TrackId").append(",");
			int i=0;
			for(String words: wordSet)
			{
				header.append(words);
				i++;
					header.append(",");
				
			}
			header.append("Genre");
			bw.write(header.toString());
			bw.newLine();
			
			for(SongData sd : trainDataMap.values())
			{
				StringBuffer content = new StringBuffer();
				content.append(sd.getTrackId()).append(",");
				i=0;
				for(String word: wordSet)
				{
					Integer x = sd.getWordCount().get(word);
					if(x!=null)
					{
						content.append(x.toString());
					}
					else
					{
						content.append(0);
					}
						content.append(",");
					
				}
				content.append(sd.getGenre());
				bw.write(content.toString());
				bw.newLine();
			}
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
