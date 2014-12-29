package com.search.impl;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import com.search.FileSearcher;

/**
 * Simple searcher class
 *
 * @author Jules Jay Paulynice
 *
 */
public class FileSearcherImpl implements FileSearcher {

    /*
     * (non-Javadoc)
     * 
     * @see com.search.FileSearcher#search(java.lang.String, int)
     */
    @Override
    public void search(final String queryStr, final int maxHits)
            throws IOException, ParseException {
        final DirectoryReader ireader = DirectoryReader.open(Utils
                .getIndexDir());
        final IndexSearcher searcher = new IndexSearcher(ireader);

        searchIndex(searcher, queryStr, maxHits);
        ireader.close();
    }

    /**
     * Search the index for given query and return only specified hits.
     *
     * @param searcher
     * @param queryStr
     * @param maxHits
     * @throws IOException
     * @throws ParseException
     */
    private void searchIndex(final IndexSearcher searcher,
            final String queryStr, final int maxHits) throws IOException,
            ParseException {
        final Query query = Utils.getQueryParser().parse(queryStr);
        final ScoreDoc[] hits = searcher.search(query, null, maxHits).scoreDocs;
        System.out.println(String.format(
                "Found %d documents matching the query: %s", hits.length,
                queryStr));
        System.out.println("Search results: \n");
        for (final ScoreDoc d : hits) {
            final Document doc = searcher.doc(d.doc);
            System.out.println(doc.get("filepath"));
        }
    }
}
