package edu.nyu.cs.cs2580.indexer;

import java.io.IOException;

import edu.nyu.cs.cs2580.indexer.io.Loader;
import edu.nyu.cs.cs2580.query.Query;
import edu.nyu.cs.cs2580.searcher.AbstractSearcher;

/**
 * @CS2580: Implement this class for HW2.
 */
public class IndexerInvertedCompressed extends Indexer {

	@Override
	public AbstractSearcher createSearcher(Query query) {
		return null;
	}

	@Override
	public int documentTermFrequency(String term, String url) {
		return 0;
	}

	@Override
	public Loader createLoader() throws IOException {
		return null;
	}

}
