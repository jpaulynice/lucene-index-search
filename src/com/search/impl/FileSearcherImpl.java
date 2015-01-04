package com.search.impl;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.search.FileSearcher;
import com.search.util.LuceneUtils;

/**
 * Simple searcher class
 *
 * @author Jay Paulynice
 *
 */
public class FileSearcherImpl implements FileSearcher {
    private static final Logger LOG = LoggerFactory
            .getLogger(FileSearcherImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.search.FileSearcher#search(java.lang.String, int)
     */
    @Override
    public void search(final String queryStr, final int maxHits)
            throws IOException, ParseException {
        final DirectoryReader ireader = DirectoryReader.open(LuceneUtils
                .getIndexDir());
        final IndexSearcher searcher = new IndexSearcher(ireader);

        searchIndex(searcher, queryStr, maxHits);
        ireader.close();
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
    private void searchIndex(final IndexSearcher searcher,
            final String queryStr, final int maxHits) throws IOException,
            ParseException {
        final Query query = LuceneUtils.getQueryParser().parse(queryStr);

        final long now = System.nanoTime();
        final ScoreDoc[] hits = searcher.search(query, null, maxHits).scoreDocs;
        final long time = (System.nanoTime() - now) / 1000000;
        LOG.info(String
                .format("Search took %d milli seconds.  Found %d documents matching the query: %s",
                        time, hits.length, queryStr));

        getResults(searcher, hits);
    }

    /**
     * Get search results
     *
     * @param searcher the lucene index searcher
     * @param hits results array
     * @throws IOException if error reading from disk
     */
    private void getResults(final IndexSearcher searcher, final ScoreDoc[] hits)
            throws IOException {
        LOG.info("Search results:");
        for (final ScoreDoc d : hits) {
            final Document doc = searcher.doc(d.doc);
            LOG.info(doc.get("filepath"));
        }
    }
}
