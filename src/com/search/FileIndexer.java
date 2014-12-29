package com.search;

import java.io.IOException;

/**
 * Simple interface with one method to index a directory.
 *
 * @author Jay Paulynice
 *
 */
public interface FileIndexer {
    /**
     * Method to index a directory by passing the directory to index and file
     * type.
     *
     * @param dir
     *            the directory to index
     * @param suffix
     *            the file type
     * @throws IOException
     */
    public void index(String dir, String suffix) throws IOException;
}
