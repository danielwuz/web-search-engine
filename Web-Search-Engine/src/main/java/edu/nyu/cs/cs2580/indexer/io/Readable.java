package edu.nyu.cs.cs2580.indexer.io;

import java.io.IOException;

import edu.nyu.cs.cs2580.doc.Corpus;

public interface Readable {

	/**
	 * Called exactly once when the SearchEngine is in {@code Mode.SERVE} mode.
	 * Subclass must load the index at {@code index_prefix} to be ready for
	 * serving the search traffic.
	 * 
	 * You must load the index from the constructed index above, do NOT try to
	 * reconstruct the index from the corpus. When the search engine is run in
	 * serve mode, it will NOT have access to the corpus, all grading for serve
	 * mode will be done with the corpus removed from the machine.
	 * 
	 * Loads the index from the index file.
	 * 
	 * N.B. For this particular implementation, loading the index from the
	 * simple serialization format is in fact slower than constructing the index
	 * from scratch. For the more efficient indices, loading should be much
	 * faster than constructing.
	 * 
	 * @throws IOException
	 */
	public abstract Corpus loadIndex() throws IOException;
}
