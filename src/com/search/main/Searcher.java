package com.search.main;

import java.io.File;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.search.searcher.FileSearcher;
import com.search.searcher.impl.FileSearcherImpl;

public class Searcher {
	
	/**
	 * Main method
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		File indexDir = new File("/Users/julespaulynice/Documents/luna/index");
		Directory directory = FSDirectory.open(indexDir);

		String query = "lucene";
		int hits = 100;

		FileSearcher searcher = new FileSearcherImpl();
		searcher.searchIndex(directory, query, hits);

	}
}
