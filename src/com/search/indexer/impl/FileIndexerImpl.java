package com.search.indexer.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserPrincipal;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.search.indexer.FileIndexer;

/**
 * Simple file indexer class.
 * 
 * @author Jules Jay Paulynice
 * 
 */
public class FileIndexerImpl implements FileIndexer{
	
	public FileIndexerImpl(){
		
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
	@Override
	public int index(Directory indexDir, File dataDir, String suffix)
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
		System.out.println("Indexing file " + f.getCanonicalPath());

		Document doc = new Document();
		doc.add(new Field("contents", new FileReader(f),TextField.TYPE_NOT_STORED));
		doc.add(new StringField("filepath", f.getCanonicalPath(),Field.Store.YES));
		doc.add(new StringField("filename", f.getName(), Field.Store.YES));
		
		Path path = Paths.get(f.getCanonicalPath());
		UserPrincipal owner = Files.getOwner(path);
		String username = owner.getName();
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
		
		doc.add(new StringField("author", username, Field.Store.YES));
		doc.add(new StringField("lastModified", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format((attr.lastModifiedTime().toMillis())), Field.Store.YES));
		doc.add(new StringField("size", String.valueOf(attr.size()), Field.Store.YES));
		doc.add(new StringField("created", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format((attr.creationTime().toMillis())), Field.Store.YES));

		indexWriter.addDocument(doc);
	}
}