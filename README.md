Lucene-Index-Search-Example
===========================

This is a simple lucene index and search example.  There are 2 classes:  

1. SimpleFileIndexer.java
2. SimpleSearcher.java

In order to make this work, it's required to put lucene-core-3.0.3.jar (<a href="http://grepcode.com/snapshot/repo1.maven.org/maven2/org.apache.lucene/lucene-core/3.0.3">Lucene Jar 3.0.3</a>) on the classpath which can be downloaded online.

Currently, the application is set to index files ending in ".java" which can be changed in the main method of SimpleFileIndexer.java
Then we search for "lucene" and listing the locations of the files.
