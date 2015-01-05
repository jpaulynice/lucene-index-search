package com.search.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserPrincipal;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import com.search.impl.FileProperties;

/**
 * @author Jay Paulynice
 *
 */
public class DocumentUtil {

    /** date formatter */
    private final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
            "MM/dd/yyyy HH:mm:ss");

    /**
     * Get file attributes and create lucene document.
     *
     * @param file the file
     * @return lucene document
     * @throws IOException if errors
     */
    public static Document fileToLuceneDoc(final File file) throws IOException {
        final Path paths = Paths.get(file.getCanonicalPath());
        final UserPrincipal owner = Files.getOwner(paths);
        final String username = owner.getName();
        final BasicFileAttributes attr = Files.readAttributes(paths,
                BasicFileAttributes.class);

        final String lastModified = getAttrVal(attr, FileProperties.MODIFIED);
        final String created = getAttrVal(attr, FileProperties.CREATED);
        final String size = String.valueOf(attr.size());
        final String content = FileUtils.readFileToString(file);
        final String path = file.getCanonicalPath();
        final String name = file.getName();

        return newLuceneDoc(content, path, name, username, lastModified, size,
                created, getDocType(file));
    }

    /**
     * Create lucene document from file attributes
     *
     * @param content the file content
     * @param path the file path
     * @param name the file name
     * @param username the owner of the file
     * @param modified last modified date
     * @param size the file size
     * @param created the file created date
     * @param docType the file type
     * @return lucene document with the fields
     */
    private static Document newLuceneDoc(final String content,
            final String path, final String name, final String username,
            final String modified, final String size, final String created,
            final String docType) {
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
     * @param attr basic file attributes object
     * @param prop property to get
     * @return property
     */
    private static String getAttrVal(final BasicFileAttributes attr,
            final FileProperties prop) {
        switch (prop) {
        case MODIFIED:
            return DATE_FORMATTER.format((attr.lastModifiedTime().toMillis()));
        case CREATED:
            return DATE_FORMATTER.format((attr.creationTime().toMillis()));
        default:
            throw new IllegalArgumentException(prop.toString()
                    + "is not supported.");
        }
    }

    /**
     * Get document type
     *
     * @param f the file
     * @return the file type
     */
    private static String getDocType(final File f) {
        final int start = f.getName().lastIndexOf(".");
        return f.getName().substring(start + 1);
    }
}
