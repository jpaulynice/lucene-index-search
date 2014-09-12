package com.search.searcher;

import org.apache.lucene.store.Directory;

/**
 * 
 * @author Jay Paulynice
 *
 */
public interface FileSearcher {
	/**
	 * Search a lucene index directory for given query string and returning specified 
	 * number of top results.
	 * 
	 * @param indexDir
	 * @param queryStr
	 * @param maxHits
	 * @throws Exception
	 */
	public void searchIndex(Directory indexDir, String queryStr, int maxHits)
			throws Exception;
}
