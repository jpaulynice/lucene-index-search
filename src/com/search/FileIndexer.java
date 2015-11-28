package com.search;

import java.io.IOException;

/**
 * Simple interface for indexing text files in a directory.
 *
 * @author Jay Paulynice
 */
public interface FileIndexer extends Closeable {
    /**
     * Method to index text files in a directory and its sub directories by
     * passing the top level directory to index and file type. If the file type
     * is {@code null} then all files are indexed in the directory.
     *
     * @param dataDir the top level directory to index
     * @param suffix the file type (.java, .txt, etc...)
     * @throws IOException if problems occur while processing a file
     */
    void index(String dataDir, String suffix) throws IOException;
}