package com.search.main;

import java.io.File;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.search.indexer.FileIndexer;
import com.search.indexer.impl.FileIndexerImpl;

public class Indexer {
	/**
	 * Main method
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		Directory directory = FSDirectory.open(new File("/Users/julespaulynice/Documents/luna/index"));
		File dataDir = new File("/Users/julespaulynice/Documents/workspace");
		String suffix = "java";

		FileIndexer indexer = new FileIndexerImpl();

		int numIndex = indexer.index(directory, dataDir, suffix);

		System.out.println("Total files indexed " + numIndex);
	}
}
