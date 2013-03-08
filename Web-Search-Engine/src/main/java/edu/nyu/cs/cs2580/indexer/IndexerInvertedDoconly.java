package edu.nyu.cs.cs2580.indexer;

import edu.nyu.cs.cs2580.query.Query;
import edu.nyu.cs.cs2580.searcher.AbstractSearcher;
import edu.nyu.cs.cs2580.searcher.LinearSearcher;

/**
 * @CS2580: Implement this class for HW2.
 */
public class IndexerInvertedDoconly extends Indexer {

	@Override
	public int documentTermFrequency(String term, String url) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AbstractSearcher createSearcher(Query query) {
		return new LinearSearcher(corpus, query);
	}

}
