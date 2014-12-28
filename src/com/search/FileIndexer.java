package com.search;

/**
 * Simple interface with one method to index a directory.
 *
 * @author Jay Paulynice
 *
 */
public interface FileIndexer {
    /**
     * Method to index a directory by passing the directory to index, location
     * to store the index, and word endings.
     *
     * @param dir
     *            the directory to index
     * @param suffix
     *            the file type
     * @return number of documents indexed
     * @throws Exception
     */
    public int index(String dir, String suffix) throws Exception;
}
