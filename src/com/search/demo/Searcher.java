package com.search.demo;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

import com.search.FileSearcher;
import com.search.impl.FileSearcherImpl;

public class Searcher {

    /**
     * Main method to search directory
     *
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws IOException,
            ParseException {
        final String query = "lucene";
        final int hits = 100;

        final FileSearcher searcher = new FileSearcherImpl();
        searcher.search(query, hits);
    }
}
