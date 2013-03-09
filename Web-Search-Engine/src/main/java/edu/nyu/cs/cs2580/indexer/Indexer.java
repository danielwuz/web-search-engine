package edu.nyu.cs.cs2580.indexer;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.nyu.cs.cs2580.doc.Corpus;
import edu.nyu.cs.cs2580.indexer.io.Loader;
import edu.nyu.cs.cs2580.query.Query;
import edu.nyu.cs.cs2580.searcher.AbstractSearcher;

/**
 * This is the abstract Indexer class for all concrete Indexer implementations.
 * 
 * Use {@link Indexer.Factory} to create concrete Indexer implementation. Do NOT
 * change the interface of this class.
 * 
 * In HW1: instructor's {@link IndexerFullScan} is provided as is.
 * 
 * In HW2: student will implement {@link IndexerInvertedDoconly},
 * {@link IndexerInvertedOccurrence}, and {@link IndexerInvertedCompressed}. See
 * comments below for more info.
 * 
 * @author congyu
 * @author fdiaz
 */
public abstract class Indexer {

	protected static final Logger logger = LogManager.getLogger(Indexer.class);

	public Corpus corpus;

	// Provided for serialization.
	public Indexer() {
	}

	public void constructIndex() throws IOException {
		Loader loader = Loader.createLoader();
		new IndexWriter(loader).constructIndex();
	}

	public void loadIndex() throws IOException {
		corpus = new IndexReader().loadIndex();
	}

	/**
	 * Creates a document searcher object for a query.
	 * <p>
	 * 
	 * @param query
	 *            a user input query, typically the one past along in url while
	 *            using GET
	 * @return document searcher, providing various search algorithms
	 * 
	 * @see edu.nyu.cs.cs2580.searcher.AbstractSearcher
	 */
	public abstract AbstractSearcher createSearcher(Query query);

	/**
	 * APIs for statistics needed for ranking.
	 * 
	 * {@link numDocs} and {@link totalTermFrequency} must return correct
	 * results for the current state whenever they are called, either during
	 * index construction or during serving (obviously).
	 * 
	 * {@link corpusDocFrequencyByTerm}, {@link corpusTermFrequency}, and
	 * {@link documentTermFrequency} must return correct results during serving.
	 */
	// Number of times {@code term} appeared in the document {@code url}.
	public abstract int documentTermFrequency(String term, String url);

	/**
	 * All Indexers must be created through this factory class based on the
	 * provided {@code options}.
	 */
	public static class Factory {
		public static Indexer getIndexer(String indexerType) {
			logger.info("Using Indexer: {}", indexerType);
			if (indexerType.equals("fullscan")) {
				return new IndexerFullScan();
			} else if (indexerType.equals("inverted-doconly")) {
				return new IndexerInvertedDoconly();
			} else if (indexerType.equals("inverted-occurrence")) {
				return new IndexerInvertedOccurrence();
			} else if (indexerType.equals("inverted-compressed")) {
				return new IndexerInvertedCompressed();
			}
			throw new IllegalArgumentException("Indexer " + indexerType
					+ " not found!");
		}
	}

}
