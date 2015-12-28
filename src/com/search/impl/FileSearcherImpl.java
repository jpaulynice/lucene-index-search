package com.search.impl;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.search.FileSearcher;
import com.search.util.LuceneUtils;

/**
 * Default implementation for {@link FileSearcher}.
 *
 * @author Jay Paulynice
 */
public class FileSearcherImpl implements FileSearcher {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /** lucene file system directory */
    private FSDirectory fsDir;

    /** lucene directory reader */
    private DirectoryReader iReader;

    /** lucene index searcher */
    private IndexSearcher searcher;

    public FileSearcherImpl() {
        init();
    }

    private void init() {
        try {
            fsDir = FSDirectory.open(new File(LuceneUtils.LUCENE_DIR));
            iReader = DirectoryReader.open(fsDir);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        searcher = new IndexSearcher(iReader);
    }

    /*
     * (non-Javadoc)
     * @see com.search.FileSearcher#search(java.lang.String, int)
     */
    @Override
    public void search(final String queryStr, final int maxHits) {
        searchIndex(queryStr, maxHits);
    }

    /**
     * Search the index for given query and return only specified hits.
     *
     * @param queryStr the plain query string
     * @param maxHits max hits to limit search to
     * @throws IOException if error reading from disk
     * @throws ParseException if error parsing queryStr
     */
    private void searchIndex(final String queryStr, final int maxHits) {
        final long now = System.currentTimeMillis();

        final Query query = parseQuery(queryStr);
        final ScoreDoc[] hits = search(query, maxHits);

        LOG.info("Search took {} milli seconds...found {} documents matching the query: {}", 
            System.currentTimeMillis() - now, hits.length, queryStr);

        printSearchResults(hits);
    }
    
    private Query parseQuery(final String queryStr) {
        Query query = null;
        try {
            query = LuceneUtils.getQueryParser().parse(queryStr);
        } catch (final ParseException e) {
            throw new RuntimeException(e);
        }
        return query;
    }

    private ScoreDoc[] search(final Query query, final int maxHits) {
        ScoreDoc[] hits = null;
        try {
            hits = searcher.search(query, null, maxHits).scoreDocs;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return hits;
    }

    /**
     * Log the search results
     *
     * @param hits results array
     * @throws IOException if error reading from disk
     */
    private void printSearchResults(final ScoreDoc[] hits) {
        if (hits.length > 0) {
            LOG.info("Search results:");
            for (final ScoreDoc d : hits) {
                try {
                    Document doc = searcher.doc(d.doc);
                    LOG.info(doc.get("filepath"));
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void close() {
        closeIndexReader();
        closeFSDirectory();
    }

    /**
     * close lucene index reader
     *
     * @throws IOException
     */
    private void closeIndexReader() {
        if (iReader != null) {
            LOG.info("closing lucene index reader...");
            try {
                iReader.close();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * close fs directory index
     *
     * @throws IOException
     */
    private void closeFSDirectory() {
        if (fsDir != null) {
            LOG.info("closing FSDirectory...");
            fsDir.close();
        }
    }
}
