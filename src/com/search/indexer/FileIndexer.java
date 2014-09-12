package com.search.indexer;

import java.io.File;

import org.apache.lucene.store.Directory;

/**
 * Simple interface with one method to index a directory.
 * 
 * @author Jay Paulynice
 *
 */
public interface FileIndexer {
	/**
	 * Method to index a directory by passing the directory to index, location
	 * to store the index, and word endings.
	 * 
	 * @param indexDir
	 * @param dataDir
	 * @param suffix
	 * @return
	 * @throws Exception
	 */
	public int index(Directory indexDir, File dataDir, String suffix) throws Exception;
}
