package com.search.demo;

import com.search.FileIndexer;
import com.search.impl.FileIndexerImpl;

public class Indexer {
    /**
     * Main method to index directory
     *
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        final String dirToIndex = "/Users/julespaulynice/Documents/workspace";
        final String suffix = "java";

        final FileIndexer indexer = new FileIndexerImpl();
        indexer.index(dirToIndex, suffix);
    }
}
