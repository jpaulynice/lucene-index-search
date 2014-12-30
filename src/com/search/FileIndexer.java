package com.search;

import java.io.IOException;

/**
 * Simple interface for text file indexing.
 *
 * @author Jay Paulynice
 *
 */
public interface FileIndexer {
    /**
     * Method to index text files in a directory and sub directories by passing
     * the top level directory to index and file type. If the file type is null
     * then all files are indexed in the directory
     *
     * @param dir
     *            the top level directory to index
     * @param suffix
     *            the file type
     * @throws IOException
     */
    public void index(String dir, String suffix) throws IOException;
}
