package com.search;

import java.io.IOException;

/**
 *
 * @author Jay Paulynice
 *
 */
public interface Closeable {
    /**
     * Close index writer/reader and fs directory to reclaim resources
     *
     * @throws IOException if problems occur while closing
     */
    public void close() throws IOException;
}
