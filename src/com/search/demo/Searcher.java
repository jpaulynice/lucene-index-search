package com.search.demo;

import com.search.FileSearcher;
import com.search.impl.FileSearcherImpl;

public class Searcher {

    /**
     * Main method to search directory
     *
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        final String query = "lucene";
        final int hits = 100;

        final FileSearcher searcher = new FileSearcherImpl();
        searcher.searchIndex(query, hits);
    }
}
