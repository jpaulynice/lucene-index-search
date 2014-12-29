package com.search.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserPrincipal;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;

import com.search.FileIndexer;

/**
 * Default implementation for {@link FileIndexer}
 *
 * @author Jay Paulynice
 *
 */
public class FileIndexerImpl implements FileIndexer {
    private static SimpleDateFormat DATE_FORMATTER;

    public FileIndexerImpl() {
        DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.search.FileIndexer#index(java.lang.String, java.lang.String)
     */
    @Override
    public int index(final String dirToIndex, final String suffix)
            throws IOException {
        final IndexWriterConfig config = new IndexWriterConfig(
                Version.LUCENE_47, new StandardAnalyzer(Version.LUCENE_47));
        final IndexWriter indexWriter = new IndexWriter(Utils.getIndexDir(),
                config);
        final File dataDir = new File(dirToIndex);

        indexDirectory(indexWriter, dataDir, suffix);
        final int numIndexed = indexWriter.maxDoc();
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
    private void indexDirectory(final IndexWriter indexWriter,
            final File dataDir, final String suffix) throws IOException {
        final File[] files = dataDir.listFiles();
        for (final File f : files) {
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
    private void indexFileWithIndexWriter(final IndexWriter indexWriter,
            final File f, final String suffix) throws IOException {

        if (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()
                || (suffix != null && !f.getName().endsWith(suffix))) {
            return;
        }
        System.out.println("Indexing file " + f.getCanonicalPath());
        indexWriter.addDocument(getLuceneDoc(f));
    }

    /**
     * Get file attributes and create lucene document.
     *
     * @param f
     *            the file
     * @return lucene document
     * @throws IOException
     */
    private Document getLuceneDoc(final File f) throws IOException {
        final Path paths = Paths.get(f.getCanonicalPath());
        final UserPrincipal owner = Files.getOwner(paths);
        final String username = owner.getName();
        final BasicFileAttributes attr = Files.readAttributes(paths,
                BasicFileAttributes.class);

        final String lastModified = getAttrVal(attr, FileProperties.MODIFIED);
        final String created = getAttrVal(attr, FileProperties.CREATED);
        final String size = String.valueOf(attr.size());
        final String content = FileUtils.readFileToString(f);
        final String path = f.getCanonicalPath();
        final String name = f.getName();

        return newLuceneDoc(content, path, name, username, lastModified, size,
                created, getDocType(f));
    }

    /**
     * Create lucene document from file attributes
     *
     * @param content
     * @param path
     * @param name
     * @param username
     * @param modified
     * @param size
     * @param created
     * @param docType
     * @return
     */
    private Document newLuceneDoc(final String content, final String path,
            final String name, final String username, final String modified,
            final String size, final String created, final String docType) {
        final Document doc = new Document();
        doc.add(new Field("contents", content, TextField.TYPE_NOT_STORED));
        doc.add(new StringField("filepath", path, Field.Store.YES));
        doc.add(new StringField("filename", name, Field.Store.YES));
        doc.add(new StringField("author", username, Field.Store.YES));
        doc.add(new StringField("lastModified", modified, Field.Store.YES));
        doc.add(new StringField("size", size, Field.Store.YES));
        doc.add(new StringField("created", created, Field.Store.YES));
        doc.add(new StringField("doctype", docType, Field.Store.YES));

        return doc;
    }

    /**
     * Get date attributes
     *
     * @param attr
     * @param prop
     * @return
     */
    private String getAttrVal(final BasicFileAttributes attr,
            final FileProperties prop) {
        switch (prop) {
        case MODIFIED:
            return DATE_FORMATTER.format((attr.lastModifiedTime().toMillis()));
        case CREATED:
            return DATE_FORMATTER.format((attr.creationTime().toMillis()));
        default:
            throw new IllegalArgumentException("not supported.");
        }
    }

    /**
     * Get document type
     *
     * @param f
     * @return
     */
    private String getDocType(final File f) {
        final int start = f.getName().lastIndexOf(".");
        return f.getName().substring(start + 1);
    }
}