package com.search;


/**
 * Simple interface for searching a lucene index directory
 *
 * @author Jay Paulynice
 */
public interface FileSearcher extends Closeable {
    /**
     * Search a lucene index directory for given query string and returning
     * specified number of top results.
     *
     * @param queryStr string to search for
     * @param maxHits number of results to return
     */
    void search(final String queryStr, final int maxHits);
}