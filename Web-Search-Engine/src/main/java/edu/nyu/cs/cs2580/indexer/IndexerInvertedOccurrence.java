package edu.nyu.cs.cs2580.indexer;

import edu.nyu.cs.cs2580.query.Query;
import edu.nyu.cs.cs2580.searcher.AbstractSearcher;
import edu.nyu.cs.cs2580.searcher.PhraseSearcher;

/**
 * @CS2580: Implement this class for HW2.
 */
public class IndexerInvertedOccurrence extends Indexer {

	
	public AbstractSearcher createSearcher(Query query) {
		// TODO return
		return new PhraseSearcher(corpus, query);
	}

	@Override
	public int documentTermFrequency(String term, String url) {
		// TODO Auto-generated method stub
		return 0;
	}

}
