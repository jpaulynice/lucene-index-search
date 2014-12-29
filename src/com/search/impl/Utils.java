package com.search.impl;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Utils {
    public static FSDirectory getIndexDir() throws IOException {
        return FSDirectory.open(new File("/tmp/lucene-index"));
    }

    public static QueryParser getQueryParser() {
        return new QueryParser(Version.LUCENE_47, "contents",
                new StandardAnalyzer(Version.LUCENE_47));
    }
}
