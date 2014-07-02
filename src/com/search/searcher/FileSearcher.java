package com.search.searcher;

import org.apache.lucene.store.Directory;

public interface FileSearcher {
	
	public void searchIndex(Directory indexDir, String queryStr, int maxHits)
			throws Exception;
}
