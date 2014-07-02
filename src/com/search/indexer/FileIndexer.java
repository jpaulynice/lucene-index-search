package com.search.indexer;

import java.io.File;

import org.apache.lucene.store.Directory;

public interface FileIndexer {
	
	public int index(Directory indexDir, File dataDir, String suffix) throws Exception;
}
