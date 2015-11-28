package com.search.demo;

import com.search.FileIndexer;
import com.search.impl.FileIndexerImpl;

public class Indexer {
    public static void main(final String[] args) {
        final String dirToIndex = "/Users/julespaulynice/Documents/myprojects";
        final String suffix = "java";

        final FileIndexer indexer = new FileIndexerImpl();
        indexer.index(dirToIndex, suffix);
        indexer.close();
    }
}