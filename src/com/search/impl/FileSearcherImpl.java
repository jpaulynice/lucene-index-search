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
 * Default implementation for {@link FileSearcher}. This is not thread-safe,
 * it's only an example on how to make use of apache lucene to index text files.
 *
 * @author Jay Paulynice
 *
 */
public class FileSearcherImpl implements FileSearcher {
    private static final Logger LOG = LoggerFactory
            .getLogger(FileSearcherImpl.class);

    /** lucene file system directory */
    private FSDirectory fsDir;

    /** lucene directory reader */
    private DirectoryReader iReader;

    /** lucene index searcher */
    private IndexSearcher searcher;

    /*
     * (non-Javadoc)
     *
     * @see com.search.FileSearcher#search(java.lang.String, int)
     */
    @Override
    public void search(final String queryStr, final int maxHits)
            throws IOException, ParseException {
        initSearcher();
        searchIndex(queryStr, maxHits);
        close();
    }

    /**
     * Search the index for given query and return only specified hits.
     *
     * @param searcher the lucene index searcher
     * @param queryStr the plain query string
     * @param maxHits max hits to limit search to
     * @throws IOException if error reading from disk
     * @throws ParseException if error parsing queryStr
     */
    private void searchIndex(final String queryStr, final int maxHits)
            throws IOException, ParseException {
        final Query query = LuceneUtils.getQueryParser().parse(queryStr);

        final long now = System.nanoTime();
        final ScoreDoc[] hits = searcher.search(query, null, maxHits).scoreDocs;
        final long time = (System.nanoTime() - now) / 1000000;
        LOG.info(String
                .format("Search took %d milli seconds.  Found %d documents matching the query: %s",
                        time, hits.length, queryStr));

        getResults(hits);
    }

    /**
     * Get search results
     *
     * @param searcher the lucene index searcher
     * @param hits results array
     * @throws IOException if error reading from disk
     */
    private void getResults(final ScoreDoc[] hits) throws IOException {
        LOG.info("Search results:");
        for (final ScoreDoc d : hits) {
            final Document doc = searcher.doc(d.doc);
            LOG.info(doc.get("filepath"));
        }
    }

    /**
     * Init lucene index for searching
     *
     * @throws IOException
     */
    private void initSearcher() throws IOException {
        initFSDirectory();
        initIndexReader();
        initIndexSearcher();
    }

    private void initIndexReader() throws IOException {
        if (iReader == null) {
            iReader = DirectoryReader.open(fsDir);
        }
    }

    private void initIndexSearcher() {
        if (searcher == null) {
            searcher = new IndexSearcher(iReader);
        }
    }

    /**
     * Init fs directory index
     *
     * @throws IOException
     */
    private void initFSDirectory() throws IOException {
        if (fsDir == null) {
            fsDir = FSDirectory.open(new File(LuceneUtils.LUCENE_DIR));
        }
    }

    @Override
    public void close() throws IOException {
        closeIndexReader();
        closeFSDirectory();
    }

    private void closeIndexReader() throws IOException {
        if (iReader != null) {
            LOG.info("closing lucene index reader...");
            iReader.close();
            iReader = null;
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
            fsDir = null;
        }
    }
}
