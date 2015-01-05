package com.search.demo;

import java.io.IOException;

import com.search.FileIndexer;
import com.search.impl.FileIndexerImpl;

/**
 * @author Jay Paulynice
 *
 */
public class Indexer {
    /**
     * Main method to index directory
     *
     * @param args arguments
     * @throws IOException if problems with reading file
     */
    public static void main(final String[] args) throws IOException {
        final String dirToIndex = "/Users/julespaulynice/Documents/workspace";
        final String suffix = "java";

        final FileIndexer indexer = new FileIndexerImpl();
        indexer.index(dirToIndex, suffix);
        indexer.close();
    }
}