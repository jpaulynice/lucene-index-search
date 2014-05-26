package com.search.indexer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Simple file indexer class.
 * 
 * @author Jules Jay Paulynice
 * 
 */
public class SimpleFileIndexer {

	/**
	 * Main method
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		//change to a temp directory to store the index i.e: c://temp/index
		Directory directory = FSDirectory.open(new File("/Users/julespaulynice/Documents/search/index"));
		//change to directory you want to index for search
		File dataDir = new File("/Users/julespaulynice/Documents/workspace");
		String suffix = "java";

		SimpleFileIndexer indexer = new SimpleFileIndexer();

		int numIndex = indexer.index(directory, dataDir, suffix);

		System.out.println("Total files indexed " + numIndex);
	}

	/**
	 * Method to index a directory by passing the directory to index, location
	 * to store the index, and word endings.
	 * 
	 * @param indexDir
	 * @param dataDir
	 * @param suffix
	 * @return
	 * @throws Exception
	 */
	private int index(Directory indexDir, File dataDir, String suffix)
			throws Exception {

		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47,analyzer);
		IndexWriter indexWriter = new IndexWriter(indexDir, config);
		indexDirectory(indexWriter, dataDir, suffix);

		int numIndexed = indexWriter.maxDoc();
		indexWriter.close();

		return numIndexed;

	}

	/**
	 * Method to index a directory recursively.
	 * 
	 * @param indexWriter
	 * @param dataDir
	 * @param suffix
	 * @throws IOException
	 */
	private void indexDirectory(IndexWriter indexWriter, File dataDir,
			String suffix) throws IOException {

		File[] files = dataDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				indexDirectory(indexWriter, f, suffix);
			} else {
				indexFileWithIndexWriter(indexWriter, f, suffix);
			}
		}

	}

	/**
	 * Index a file by creating a Document and adding fields
	 * 
	 * @param indexWriter
	 * @param f
	 * @param suffix
	 * @throws IOException
	 */
	private void indexFileWithIndexWriter(IndexWriter indexWriter, File f,
			String suffix) throws IOException {

		if (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()) {
			return;
		}
		if (suffix != null && !f.getName().endsWith(suffix)) {
			return;
		}
		System.out.println("Indexing file " + f.getName());

		Document doc = new Document();
		doc.add(new Field("contents", new FileReader(f),TextField.TYPE_NOT_STORED));
		doc.add(new StringField("filepath", f.getCanonicalPath(),Field.Store.YES));
		doc.add(new StringField("filename", f.getName(), Field.Store.YES));

		indexWriter.addDocument(doc);
	}
}