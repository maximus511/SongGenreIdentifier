//package com.machinelearning.project.knn;


public class Word implements Comparable<Word>{

	String word =null;
	double wordCount = 0.0;
	String genreValue = null;
	
	Word(String word, double no , String genre)
	{
		this.word = word;
		this.wordCount = no;
		this.genreValue = genre;
	}
	
	
	public String getWord() {
		return word;
	}
	
	public double getWordCount() {
		return wordCount;
	}
	
	public String getGenreValue() {
		return genreValue;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public void setWordCount(double wordCount) {
		this.wordCount = wordCount;
	}
	
	public void setGenreValue(String genreValue) {
		this.genreValue = genreValue;
	}

	@Override
	public int compareTo(Word word) {
		// TODO Auto-generated method stub

		return (this.getWord().compareTo(word.getWord()));
	}
	
	public boolean equals(Word newWord)
	{
		return ( this.getWord().equals(newWord.getWord()) );
	}
	
	
}
