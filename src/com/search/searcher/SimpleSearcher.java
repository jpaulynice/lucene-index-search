package com.search.searcher;

import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Simple searcher class
 * 
 * @author Jules Jay Paulynice
 * 
 */
public class SimpleSearcher {

	/**
	 * Main method
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		File indexDir = new File("/Users/julespaulynice/Documents/search/index");
		Directory directory = FSDirectory.open(indexDir);

		String query = "java";
		int hits = 100;

		SimpleSearcher searcher = new SimpleSearcher();
		searcher.searchIndex(directory, query, hits);

	}

	/**
	 * Search a given index for the search term and return maxHits results.
	 * 
	 * @param indexDir
	 * @param queryStr
	 * @param maxHits
	 * @throws Exception
	 */
	private void searchIndex(Directory indexDir, String queryStr, int maxHits)
			throws Exception {
		DirectoryReader ireader = DirectoryReader.open(indexDir);
		IndexSearcher searcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Version.LUCENE_47, "contents",new StandardAnalyzer(Version.LUCENE_47));

		Query query = parser.parse(queryStr);
		ScoreDoc[] hits = searcher.search(query, null, maxHits).scoreDocs;

		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println(d.get("filename"));
		}

		System.out.println("Found " + hits.length);
	}
}
