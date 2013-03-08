package edu.nyu.cs.cs2580.indexer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.nyu.cs.cs2580.common.Options;
import edu.nyu.cs.cs2580.doc.Corpus;
import edu.nyu.cs.cs2580.indexer.io.Readable;

public class IndexReader implements Readable {

	protected static final Logger logger = LogManager
			.getLogger(IndexReader.class);

	@Override
	public Corpus loadIndex() throws IOException {
		String indexFile = Options.indexPath();
		logger.info("Load index from: {} ", indexFile);
		ObjectInputStream reader = new ObjectInputStream(new FileInputStream(
				indexFile));
		try {
			Corpus corpus = (Corpus) reader.readObject();
			logger.info("{} documents loaded with {} terms",
					corpus.numOfDocs(), corpus.numOfTerms());
			return corpus;
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new IOException("Loading index failed: " + e.getMessage());
		} finally {
			reader.close();
		}
	}

}
