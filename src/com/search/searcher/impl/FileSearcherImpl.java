package com.search.searcher.impl;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import com.search.searcher.FileSearcher;

/**
 * Simple searcher class
 * 
 * @author Jules Jay Paulynice
 * 
 */
public class FileSearcherImpl implements FileSearcher{

	/**
	 * Search a given index for the search term and return maxHits results.
	 * 
	 * @param indexDir
	 * @param queryStr
	 * @param maxHits
	 * @throws Exception
	 */
	@Override
	public void searchIndex(Directory indexDir, String queryStr, int maxHits)
			throws Exception {
		DirectoryReader ireader = DirectoryReader.open(indexDir);
		IndexSearcher searcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Version.LUCENE_47, "contents",new StandardAnalyzer(Version.LUCENE_47));

		Query query = parser.parse(queryStr);
		ScoreDoc[] hits = searcher.search(query, null, maxHits).scoreDocs;

		System.out.println("Search results: \n");
		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println(d.get("filename"));
		}
		ireader.close();

		System.out.println("Found " + hits.length);
	}
}
