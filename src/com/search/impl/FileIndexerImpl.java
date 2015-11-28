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
 * Default implementation for {@link FileIndexer}.
 *
 * @author Jay Paulynice
 */
public class FileIndexerImpl implements FileIndexer {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /** lucene index writer */
    private IndexWriter iWriter;

    /** lucene file system directory */
    private FSDirectory fsDir;

    public FileIndexerImpl() {
        init();
    }

    private void init() {
        try {
            fsDir = FSDirectory.open(new File(LuceneUtils.LUCENE_DIR));
            iWriter = new IndexWriter(fsDir, LuceneUtils.CONFIG);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.search.FileIndexer#index(java.lang.String, java.lang.String)
     */
    @Override
    public void index(final String dirToIndex, final String suffix) {
        final long now = System.currentTimeMillis();

        indexDirectory(new File(dirToIndex), suffix);

        LOG.info("Indexed {} files in {} milli seconds.", iWriter.maxDoc(),
                System.currentTimeMillis() - now);
    }

    /**
     * Method to index a directory recursively.
     *
     * @param dataDir the directory to index
     * @param suffix the file type
     * @throws IOException if errors trying to read file
     */
    private void indexDirectory(final File dataDir, final String suffix) {
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
     * @param f the file to index
     * @param suffix the file type
     * @throws IOException if errors trying to read file
     */
    private void indexFile(final File f, final String suffix) {
        if (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()
                || (suffix != null && !f.getName().endsWith(suffix))) {
            return;
        }

        try {
            LOG.info("Indexing file: {}", f.getCanonicalPath());
            final Document doc = DocumentUtil.fileToLuceneDoc(f);
            iWriter.addDocument(doc);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        closeIndexWriter();
        closeFSDirectory();
    }

    /**
     * close index writer
     *
     * @throws IOException
     */
    private void closeIndexWriter() {
        if (iWriter != null) {
            LOG.info("Shutting down index writer...");
            try {
                iWriter.close();
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
