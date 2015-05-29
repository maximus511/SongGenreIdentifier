//package com.machinelearning.project.naivebayes;

import java.util.HashMap;

public class SongData {

	private String trackId;
	private HashMap<String, Integer> wordCount;
	private String genre;
	
	
	public String getTrackId() {
		return trackId;
	}
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}
	public HashMap<String, Integer> getWordCount() {
		return wordCount;
	}
	public void setWordCount(HashMap<String, Integer> wordCount) {
		this.wordCount = wordCount;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	
	
	
	
}
