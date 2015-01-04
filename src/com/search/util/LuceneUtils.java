package com.search.util;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * @author Jay Paulynice
 *
 */
public class LuceneUtils {
    /**
     * lucene version
     */
    public static final Version VERSION = Version.LUCENE_47;

    /**
     * lucene index config
     */
    public static final IndexWriterConfig CONFIG = new IndexWriterConfig(
            VERSION, new StandardAnalyzer(VERSION));

    /**
     * @return {@link FSDirectory}
     * @throws IOException if unable to open file
     */
    public static FSDirectory getIndexDir() throws IOException {
        return FSDirectory.open(new File("/tmp/lucene-index"));
    }

    /**
     * @return {@link QueryParser} to parse query
     */
    public static QueryParser getQueryParser() {
        return new QueryParser(Version.LUCENE_47, "contents",
                new StandardAnalyzer(Version.LUCENE_47));
    }
}
