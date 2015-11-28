package com.search.demo;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

import com.search.FileSearcher;
import com.search.impl.FileSearcherImpl;

/**
 * Lucene file search demo
 *
 * @author Jay Paulynice
 */
public class Searcher {

    /**
     * Search directory
     *
     * @param args arguments
     * @throws IOException if problems with reading file
     * @throws ParseException if unable to parse query
     */
    public static void main(final String[] args) throws IOException,
    ParseException {
        final String query = "blah";
        final int hits = 100;

        final FileSearcher searcher = new FileSearcherImpl();
        searcher.search(query, hits);
        searcher.close();
    }
}