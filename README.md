Lucene-Index-Search-Example
===========================

This is a simple example of how to use apache lucene to index and search a directory.

Currently, the application is set to index files ending in `java` which can be changed in the main method of Indexer.java
```java
package com.search.demo;

import com.search.FileIndexer;
import com.search.impl.FileIndexerImpl;

public class Indexer {
    /**
     * Main method to index directory
     *
     * @param args
     */
    public static void main(final String[] args){
        final String dirToIndex = "/Users/julespaulynice/Documents/workspace";
        final String suffix = "java";

        final FileIndexer indexer = new FileIndexerImpl();
        indexer.index(dirToIndex, suffix);
    }
}
```

Then search for "lucene" and list the file paths that match our query.

```java
package com.search.demo;

import com.search.FileSearcher;
import com.search.impl.FileSearcherImpl;

public class Searcher {
    /**
     * Main method to search directory
     *
     * @param args
     */
    public static void main(final String[] args) {
        final String query = "lucene";
        final int hits = 100;

        final FileSearcher searcher = new FileSearcherImpl();
        searcher.search(query, hits);
    }
}
```
