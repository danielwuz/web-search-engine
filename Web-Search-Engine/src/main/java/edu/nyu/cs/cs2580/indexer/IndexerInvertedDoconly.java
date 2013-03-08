package edu.nyu.cs.cs2580.indexer;

import java.io.IOException;

import edu.nyu.cs.cs2580.indexer.io.InvertedLoader;
import edu.nyu.cs.cs2580.indexer.io.Loader;
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
	public Loader createLoader() throws IOException {
		return new InvertedLoader();
	}

	@Override
	public AbstractSearcher createSearcher() {
		return new LinearSearcher(corpus);
	}

}
