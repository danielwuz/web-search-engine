package edu.nyu.cs.cs2580.indexer.io;

import java.io.IOException;

public interface Writable {

	/**
	 * Called when the SearchEngine is in {@code Mode.INDEX} mode. Subclass must
	 * construct the index from the provided corpus at {@code corpus_prefix}.
	 * 
	 * Document processing must satisfy the following: 1) Non-visible page
	 * content is removed, e.g., those inside <script> tags 2) Tokens are
	 * stemmed with Step 1 of the Porter's algorithm 3) No stop word is removed,
	 * you need to dynamically determine whether to drop the processing of a
	 * certain inverted list.
	 * 
	 * The index must reside at the directory of index_prefix, no other data can
	 * be stored (either in a hidden file or in a temporary directory). We will
	 * construct your index on one machine and move the index to a different
	 * machine for serving, so do NOT try to play tricks.
	 */
	public void constructIndex() throws IOException;
}
