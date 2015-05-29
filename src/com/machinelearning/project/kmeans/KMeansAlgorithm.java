package com.machinelearning.project.kmeans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * Class implementing the K-Means algorithm to classify the Lyrics dataset.
 *
 */
public class KMeansAlgorithm {

	public static ArrayList<String[]> songLyricsData = new ArrayList<String[]>();
	public static Integer noOfClusters=0;
	public static Centroid[] centroidList;
	public static HashMap<String, String> result = new HashMap<String, String>();
	
	
	public static Centroid[] getCentroidList() {
		return centroidList;
	}

	public static void setCentroidList(Centroid[] centroidList) {
		KMeansAlgorithm.centroidList = centroidList;
	}

	/**
	 * Function to read input dataset
	 * @param fileName
	 */
	public static void readData(String fileName)
	{
		BufferedReader bReader = null;
		try {
			File file = new File(fileName);
			bReader = new BufferedReader(new FileReader(file));
			String currentLine = null;
			bReader.readLine();
			while((currentLine=bReader.readLine())!=null && !currentLine.equals(""))
			{
				
				String[] recordSplit = currentLine.trim().split(",");
				String[] record = new String[recordSplit.length];
				for(int i=0;i<recordSplit.length;i++)
				{
					record[i] = recordSplit[i];
				}
				songLyricsData.add(record);
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

	/**
	 * Generate the K clusters and segregate the data into different clusters.
	 * @param k
	 */
	public static void generateKCluster(Integer k)
	{
		noOfClusters =k;
		centroidList = new Centroid[noOfClusters];
		int i=0;
		int prevRandom=0;
		//Randomly select k records to act as initial means
		while(i<noOfClusters)
		{
			int random= (int)(Math.random() * (songLyricsData.size()-50*i));
			while(random == 0 || prevRandom==random)
			{
				random= (int)(Math.random() * (songLyricsData.size()-50*i));
			}
			prevRandom= random;
			Centroid  center = new Centroid();
			center.setCentroidAttributes(songLyricsData.get(random));
			centroidList[i]= center;
			i++;
			System.out.println(" Centroid - "+center.getCentroidAttributes()[0]+center.getCentroidAttributes()[center.getCentroidAttributes().length-1]);
		}
		
		//Traverse through the original dataset and identify the cluster for each data using euclidean
		//distance
		for(String[] lyricsData : songLyricsData)
		{
			int flagIndex =0;
			double minEuclideanDist = Integer.MAX_VALUE;
			int m=0;
			for(Centroid cent: centroidList)
			{
				double sqEuclideanDist = 0.0;
				//calculate euclidean distance
				for(int j=1;j<lyricsData.length-1;j++)
				{
					double tempDiff = Double.valueOf(lyricsData[j])-Double.valueOf(cent.getCentroidAttributes()[j]);
					sqEuclideanDist += (tempDiff*tempDiff);
				}
				double eucDistance = Math.sqrt(sqEuclideanDist);
				if(eucDistance<minEuclideanDist)
				{
					flagIndex = m;
					minEuclideanDist = eucDistance;
				}
				m++;
			}
			centroidList[flagIndex].getCluster().add(lyricsData);
		}

		Boolean swapFlag = true;
		//Iterate through the data in each cluster and identify the new cluster for the data based
		//on the means. Terminate when no more movement of data across clusters.
		int x=1;
		while(swapFlag)
		{
			ArrayList<ArrayList<String[]>> swapList = 
					new ArrayList<ArrayList<String[]>>();
			for(int ind =0 ; ind<noOfClusters ;ind++)
			{
				swapList.add(new ArrayList<String[]>());
			}
			//calculate the mean of the clusters
			for(Centroid centroid: centroidList)
			{
				centroid.setCentroidAttributes(calculateMean(centroid.getCluster()));
			}
			x++;
			int currentCluster =0;
			//traverse through data of each cluster and using euclidean distance find its 
			//new cluster
			for(Centroid irisCentroids : centroidList)
			{
				Iterator<String[]> iterator =  irisCentroids.getCluster().iterator();
				while(iterator.hasNext()) 
				{
					String[] irisData = iterator.next();
					swapFlag=true;
					int flagIndex =0;
					double minEuclideanDist = Integer.MAX_VALUE;
					int m=0;
					for(Centroid cent: centroidList)
					{
						double sqEuclideanDist = 0.0;
						for(int j=1;j<irisData.length-1;j++)
						{
							double tempDiff = Double.valueOf(irisData[j])-Double.valueOf(cent.getCentroidAttributes()[j]);
							sqEuclideanDist += (tempDiff*tempDiff);
						}
						double eucDistance = Math.sqrt(sqEuclideanDist);
						if(eucDistance<minEuclideanDist)
						{
							flagIndex = m;
							minEuclideanDist = eucDistance;
						}
						m++;
					}
					//check if the cluster identified as closest is the cluster to which the data
					//already belongs.
					if(flagIndex != currentCluster)
					{
						swapList.get(flagIndex).add(irisData);
						iterator.remove();
						swapFlag=true;
					}
					else
					{
						swapFlag=false;
					}
				}
				currentCluster++;
			}
			
			//Copy all the data to their new clusters
			for(int index=0;index<centroidList.length;index++)
			{
				centroidList[index].getCluster().addAll(swapList.get(index));
			}

		}
		
		//Writing the final clusters output to a file
		try {

			File file = new File("output.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			int clusterId=1;
			String[] g = new String[]{"rock","alternate","pop","electronic","indie"};
			int index=0;
			for(Centroid c: centroidList)
			{
				bw.write("-------------------------\nCluster - "+clusterId+"\nSize= " + c.getCluster().size());
				bw.newLine();
				for(String[] d: c.getCluster())
				{
					bw.write(d[0].toString()+" "+d[d.length-1].toString());
					result.put(d[0].toString(), g[index]);
					bw.newLine();
				}
				bw.newLine();
				clusterId++;
				index++;
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Calculate the mean of the cluster
	 * @param valueList
	 * @return
	 */
	public static String[] calculateMean(ArrayList<String[]> valueList)
	{
		double[]a = new double[valueList.get(0).length];
		for(String[] value : valueList)
		{
			for(int i=1;i<value.length-1;i++)
			{
				a[i]+=Double.valueOf(value[i]);
			}
			//a2+=Double.valueOf(value[1]);
			//a3+=Double.valueOf(value[2]);
			//a4+=Double.valueOf(value[3]);
		}

		String[] result = new String[a.length];
		for(int i=0;i<a.length;i++)
		{
			result[i] = (String.valueOf(a[i]/(double)valueList.size()));
		}
		return result;
	}


	/**
	 * Calculate entropy for each cluster
	 * @param clusterData
	 * @return
	 */
	public static double calculateEntropyForCluster(List<String[]> clusterData)
	{

		double entropy = 0;
		int classACount=0; //Iris-Setosa
		int classBCount=0; //Iris-versicolor
		int classCCount =0;//Iris-virginica
		int classDCount =0;
		int classECount =0;
		for(int a = 0; a < clusterData.size(); a++) {
			if(clusterData.get(a)[clusterData.get(a).length-1].trim().equals("rock"))
			{
				classACount++;
			}
			else if(clusterData.get(a)[clusterData.get(a).length-1].trim().equals("alternate"))
			{
				classBCount++;
			}
			else if(clusterData.get(a)[clusterData.get(a).length-1].trim().equals("pop"))
			{
				classCCount++;
			}
			else if(clusterData.get(a)[clusterData.get(a).length-1].trim().equals("indie"))
			{
				classDCount++;
			}
			else if(clusterData.get(a)[clusterData.get(a).length-1].trim().equals("electronic"))
			{
				classECount++;
			}
		}
		double total = classBCount+classACount+classCCount+classDCount+classECount;
		if(classACount != 0)
		{
			entropy =-((double)classACount/(double)(total)) * (Math.log10((double)classACount/(double)(total)) / Math.log10(2));
		}
		if(classBCount != 0)
		{
			entropy +=-((double)classBCount/(double)(total)) * (Math.log10((double)classBCount/(double)(total)) / Math.log10(2));
		}	
		if(classCCount != 0)
		{
			entropy +=-((double)classCCount/(double)(total)) * (Math.log10((double)classCCount/(double)(total)) / Math.log10(2));
		}
		if(classDCount != 0)
		{
			entropy +=-((double)classDCount/(double)(total)) * (Math.log10((double)classDCount/(double)(total)) / Math.log10(2));
		}	
		if(classECount != 0)
		{
			entropy +=-((double)classECount/(double)(total)) * (Math.log10((double)classECount/(double)(total)) / Math.log10(2));
		}	

		return entropy;

	}

	/**
	 * Calculate the total entropy of the cluster system
	 * @return
	 */
	public static double calculateTotalEntropy()
	{
		Double[] clusterEntropy = new Double[noOfClusters];
		Double totalEntropy = 0.0;
		for(int i=0; i<noOfClusters;i++)
		{
			clusterEntropy[i] = calculateEntropyForCluster(centroidList[i].getCluster());
		}
		for(int j=0;j<noOfClusters;j++)
		{
			totalEntropy += ((double)centroidList[j].getCluster().size()/(double)songLyricsData.size())*clusterEntropy[j];
		}
		return totalEntropy;
	}

	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args)
	{
		if(args.length!=2)
		{
			System.out.println("Invalid number of arguments! \n [USAGE] java KMeansAlgorithm <filename> <number of clusters>");
			return;
		}
		readData(args[0]);
		try 
		{
			generateKCluster(5);
		} catch(NumberFormatException ne)
		{
			ne.printStackTrace();
			return;
		}

		System.out.println("Entropy for k= 5 is "+calculateTotalEntropy());
	}

}
