//package com.machinelearning.project.knn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CalculateDistance {


	public static HashMap<String, Double> FindDistance(ArrayList<Word> lyricsWord , HashMap<String,ArrayList<Word>> genreList) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		for(Map.Entry<String,ArrayList<Word>> words : genreList.entrySet())
		{
			double eucDist = 0d;
			for(Word w : words.getValue())
			{
				Boolean foundWord = false;
				for(Word m : lyricsWord)
				{
					if(m.getWord().equalsIgnoreCase(w.getWord()))
					{
						eucDist =  eucDist + (Math.pow((w.getWordCount() - m.getWordCount()),2));
						foundWord = true;
						break;
					}
				}
				if(!foundWord)
				{
					eucDist = eucDist + Math.pow(w.getWordCount(),2);
				}
			}
			for(Word w : lyricsWord)
			{
				Boolean found =false;
				for(Word m : words.getValue())
				{
					if(m.getWord().equalsIgnoreCase(w.getWord()))
					{
						found = true;
						break;
					}
				}
				
				if(!found)
				{
					eucDist+=Math.pow(w.getWordCount(),2);
				}
			}

			result.put(words.getKey(), Math.sqrt(eucDist));
		}

		return result;
	}


}
