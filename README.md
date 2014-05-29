Lucene-Index-Search-Example
===========================

This is a simple lucene index and search example.  There are 2 classes:  

1. SimpleFileIndexer.java
2. SimpleSearcher.java

In order to make this work, download and put these jars on the classpath:

1. <a href="http://repo1.maven.org/maven2/org/apache/lucene/lucene-queryparser/4.7.2/lucene-queryparser-4.7.2.jar">lucene-queryparser-4.7.2.jar</a>
2. <a href="http://repo1.maven.org/maven2/org/apache/lucene/lucene-core/4.7.2/lucene-core-4.7.2.jar">lucene-core-4.7.2.jar</a>
3.  <a href="http://repo1.maven.org/maven2/org/apache/lucene/lucene-analyzers-common/4.7.2/lucene-analyzers-common-4.7.2.jar">lucene-analyzers-common-4.7.2.jar</a>
  
Finally:
1.  Change SimpleFileIndexer.java to specify a temp directory to store index and directory to index
    <code>
		  //change to a temp directory to store the index i.e: c://temp/index
  		Directory directory = FSDirectory.open(new File("/Users/julespaulynice/Documents/search/index"));
		  //change to directory you want to index for search
		  File dataDir = new File("/Users/julespaulynice/Documents/workspace");
    </code>
2.  Change SimpleSearcher.java to specify index directory to search.

Currently, the application is set to index files ending in ".java" which can be changed in the main method of SimpleFileIndexer.java
Several indexing files are then created.  Then using SimpleSearcher class, we search for "lucene" and list the file paths that match our query.
