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
    private final IndexSearcher searcher;

    /**
     * @throws IOException if errors
     */
    public FileSearcherImpl() throws IOException {
        fsDir = FSDirectory.open(new File(LuceneUtils.LUCENE_DIR));
        iReader = DirectoryReader.open(fsDir);
        searcher = new IndexSearcher(iReader);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.search.FileSearcher#search(java.lang.String, int)
     */
    @Override
    public void search(final String queryStr, final int maxHits)
            throws IOException, ParseException {
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

    @Override
    public void close() throws IOException {
        closeIndexReader();
        closeFSDirectory();
    }

    /**
     * close lucene index reader
     *
     * @throws IOException
     */
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
