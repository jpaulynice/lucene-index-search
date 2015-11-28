package com.search.demo;

import com.search.FileSearcher;
import com.search.impl.FileSearcherImpl;

public class Searcher {
    public static void main(final String[] args) {
        final String query = "android";
        final int hits = 100;

        final FileSearcher searcher = new FileSearcherImpl();
        searcher.search(query, hits);
        searcher.close();
    }
}