package com.search;

/**
 * Sinple interface for search
 *
 * @author Jay Paulynice
 *
 */
public interface FileSearcher {
    /**
     * Search a lucene index directory for given query string and returning
     * specified number of top results.
     *
     * @param queryStr
     *            string to search for
     * @param maxHits
     *            number of results to return
     * @throws Exception
     */
    public void searchIndex(String queryStr, int maxHits) throws Exception;
}
