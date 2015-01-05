package com.search.impl;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.search.FileIndexer;
import com.search.util.DocumentUtil;
import com.search.util.LuceneUtils;

/**
 * Default implementation for {@link FileIndexer}. This is not thread-safe, it's
 * only an example on how to make use of apache lucene to index text files.
 *
 * @author Jay Paulynice
 *
 */
public class FileIndexerImpl implements FileIndexer {
    private static final Logger LOG = LoggerFactory
            .getLogger(FileIndexerImpl.class);

    /** lucene index writer */
    private IndexWriter iWriter;

    /** lucene file system directory */
    private FSDirectory fsDir;

    /*
     * (non-Javadoc)
     * 
     * @see com.search.FileIndexer#index(java.lang.String, java.lang.String)
     */
    @Override
    public void index(final String dirToIndex, final String suffix)
            throws IOException {
        initLuceneIndex();
        final long now = System.nanoTime();
        indexDirectory(new File(dirToIndex), suffix);
        final long time = (System.nanoTime() - now) / 1000000000;
        LOG.info(String.format("Indexed %d files in %d seconds.",
                iWriter.maxDoc(), time));
    }

    /**
     * Method to index a directory recursively.
     *
     * @param indexWriter the lucene index writer object
     * @param dataDir the directory to index
     * @param suffix the file type
     * @throws IOException if errors trying to read file
     */
    private void indexDirectory(final File dataDir, final String suffix)
            throws IOException {
        final File[] files = dataDir.listFiles();
        for (final File f : files) {
            if (f.isDirectory()) {
                indexDirectory(f, suffix);
            } else {
                indexFile(f, suffix);
            }
        }
    }

    /**
     * Index a file by creating a Document and adding fields
     *
     * @param indexWriter the lucene index writer object
     * @param f the file to index
     * @param suffix the file type
     * @throws IOException if errors trying to read file
     */
    private void indexFile(final File f, final String suffix)
            throws IOException {
        if (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()
                || (suffix != null && !f.getName().endsWith(suffix))) {
            return;
        }
        LOG.info("Indexing file " + f.getCanonicalPath());
        final Document doc = DocumentUtil.fileToLuceneDoc(f);
        iWriter.addDocument(doc);
    }

    /**
     * Init lucene index for indexing
     *
     * @throws IOException
     */
    private void initLuceneIndex() throws IOException {
        initFSDirectory();
        initIndexWriter();
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

    /**
     * Init index writer
     *
     * @throws IOException
     */
    private void initIndexWriter() throws IOException {
        if (iWriter == null) {
            LOG.info("Initializing index writer...");
            iWriter = new IndexWriter(fsDir, LuceneUtils.CONFIG);
        }
    }

    @Override
    public void close() throws IOException {
        closeIndexWriter();
        closeFSDirectory();
    }

    /**
     * close index writer
     *
     * @throws IOException
     */
    private void closeIndexWriter() throws IOException {
        if (iWriter != null) {
            LOG.info("Shutting down index writer...");
            iWriter.close();
            iWriter = null;
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