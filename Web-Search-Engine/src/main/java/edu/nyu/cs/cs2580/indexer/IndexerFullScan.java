package edu.nyu.cs.cs2580.indexer;

import java.io.IOException;

import edu.nyu.cs.cs2580.indexer.io.FullScanLoader;
import edu.nyu.cs.cs2580.indexer.io.Loader;
import edu.nyu.cs.cs2580.searcher.AbstractSearcher;
import edu.nyu.cs.cs2580.searcher.FullScanSearcher;
import edu.nyu.cs.cs2580.server.SearchEngine;

/**
 * Implementation of a simple full scan Indexer
 * 
 * @author Daniel
 */
public class IndexerFullScan extends Indexer {

	@Override
	public int documentTermFrequency(String term, String url) {
		SearchEngine.Check(false, "Not implemented!");
		return 0;
	}

	@Override
	public AbstractSearcher createSearcher() {
		return new FullScanSearcher(corpus);
	}

	@Override
	public Loader createLoader() throws IOException {
		return new FullScanLoader();
	}
}
