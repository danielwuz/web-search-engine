package edu.nyu.cs.cs2580.indexer.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.nyu.cs.cs2580.common.Options;
import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.SerializeUtil;

public class DocumentIO {

	private static final Logger logger = LogManager.getLogger(DocumentIO.class);

	private List<Document> cache;

	// default capacity
	public static int CAPACITY = 1000;

	private int count = 0;

	public DocumentIO() {
		this(CAPACITY);
	}

	public DocumentIO(int capcity) {
		DocumentIO.CAPACITY = capcity;
		this.cache = new ArrayList<Document>(CAPACITY);
	}

	/**
	 * Caches a document in memory.
	 * <p>
	 * If cache size exceeds capacity, then flush all cached document onto disk.
	 * 
	 * @param doc
	 *            Document
	 */
	public void addDocument(Document doc) {
		this.cache.add(doc);
		if (cache.size() >= DocumentIO.CAPACITY) {
			this.flush();
		}
	}

	public List<Document> loadDocuments() throws IOException {
		File[] files = loadDocumentFiles();
		List<Document> results = new ArrayList<Document>();
		for (File file : files) {
			List<Document> docs = loadFromFile(file);
			if (docs != null) {
				results.addAll(docs);
			}
		}
		return results;
	}

	private List<Document> loadFromFile(File file) throws IOException {
		logger.info("Load document from: {} ", file.getName());
		List<Document> docs = new ArrayList<Document>();
		BufferedReader reader = null;
		try {
			SerializeUtil parser = SerializeUtil.instance();
			reader = new BufferedReader(new FileReader(file));
			String docString = "";
			while ((docString = reader.readLine()) != null) {
				docs.add(parser.toDocument(docString));
			}
		} finally {
			close(reader);
		}
		return docs;
	}

	private File[] loadDocumentFiles() {
		String indexPrefix = Options.indexPrefix();
		final String docIndex = Options.docIndex();
		File indexPath = new File(indexPrefix);
		File[] filenames = indexPath.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.startsWith(docIndex);
			}
		});
		return filenames;
	}

	/**
	 * Flushes cached documents into disk
	 */
	public void flush() {
		if (cache.isEmpty()) {
			return;
		}
		String indexFile = getFileName();
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(indexFile));
			SerializeUtil parser = SerializeUtil.instance();
			for (Document doc : cache) {
				writer.write(parser.asRecord(doc));
				writer.write("\n");
			}
		} catch (IOException e) {
			logger.error("Error happens when persisting documents: {} ",
					e.getMessage());
		} finally {
			close(writer);
		}
		this.cache.clear();
		System.gc();
	}

	private void close(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	private String getFileName() {
		String indexFile = Options.docIndexPath();
		return indexFile + (count++);
	}
}
