package edu.nyu.cs.cs2580.indexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.nyu.cs.cs2580.doc.Document;

public class Index implements Iterable<Document> {

	private static Logger _log = LogManager.getLogger(Index.class);

	// documents in corpus
	public Vector<Document> _documents;

	/*
	 * Constructor with corpus path
	 */
	public Index(String index_source) {
		_log.debug("Indexing documents ...");

		_documents = new Vector<Document>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					index_source));
			try {
				String line = null;
				int did = 0;
				while ((line = reader.readLine()) != null) {
					Document d = new Document(did, line);
					_documents.add(d);
					did++;
				}
			} finally {
				reader.close();
			}
		} catch (IOException ioe) {
			_log.error("Oops " + ioe.getMessage());
		}
		_log.debug("Done indexing " + _documents.size() + " documents...");
	}

	public int documentFrequency(String s) {
		return Document.documentFrequency(s);
	}

	public int termFrequency(String s) {
		return Document.termFrequency(s);
	}

	public int termFrequency() {
		return Document.termFrequency();
	}

	public int numDocs() {
		return _documents.size();
	}

	public Document getDoc(int did) {
		return (did >= _documents.size() || did < 0) ? null : _documents
				.get(did);
	}

	@Override
	public Iterator<Document> iterator() {
		return _documents.iterator();
	}

}
