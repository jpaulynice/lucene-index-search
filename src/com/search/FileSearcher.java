package com.search;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

/**
 * Simple interface for searching a lucene index directory
 *
 * @author Jay Paulynice
 *
 */
public interface FileSearcher {
    /**
     * Search a lucene index directory for given query string and returning
     * specified number of top results.
     *
     * @param queryStr string to search for
     * @param maxHits number of results to return
     * @throws IOException if problems occur while processing a file
     *
     * @throws ParseException if unable to parse the query
     */
    public void search(final String queryStr, final int maxHits)
            throws IOException, ParseException;
}
