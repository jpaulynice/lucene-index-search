package com.search.impl;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.search.FileSearcher;

/**
 * Simple searcher class
 *
 * @author Jules Jay Paulynice
 *
 */
public class FileSearcherImpl implements FileSearcher {

    /**
     * Search a given index for the search term and return maxHits results.
     *
     * @param indexDir
     * @param queryStr
     * @param maxHits
     * @throws Exception
     */
    @Override
    public void searchIndex(final String queryStr, final int maxHits)
            throws Exception {
        final Directory luceneDir = FSDirectory.open(new File(
                "/tmp/lucene-index"));

        final DirectoryReader ireader = DirectoryReader.open(luceneDir);
        final IndexSearcher searcher = new IndexSearcher(ireader);
        final QueryParser parser = new QueryParser(Version.LUCENE_47,
                "contents", new StandardAnalyzer(Version.LUCENE_47));

        final Query query = parser.parse(queryStr);
        search(searcher, query, maxHits);
        ireader.close();
    }

    private void search(final IndexSearcher searcher, final Query query,
            final int maxHits) throws IOException {
        final ScoreDoc[] hits = searcher.search(query, null, maxHits).scoreDocs;
        System.out.println("Found " + hits.length);
        System.out.println("Search results: \n");
        for (int i = 0; i < hits.length; i++) {
            final int docId = hits[i].doc;
            final Document d = searcher.doc(docId);
            System.out.println(d.get("filename"));
        }
    }
}
