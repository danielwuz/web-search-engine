package edu.nyu.cs.cs2580.searcher;

import edu.nyu.cs.cs2580.doc.Corpus;
import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.query.Query;

public abstract class AbstractSearcher {

	protected final Corpus corpus;

	protected final Query query;

	public AbstractSearcher(Corpus corpus, Query query) {
		this.corpus = corpus;
		this.query = query;
	}

	/**
	 * Iterator access to documents, used in HW2 for retrieving terms features
	 * for the query matching the documents.
	 * 
	 * @param docid
	 * 
	 * @return the next Document after {@code docid} satisfying {@code query} or
	 *         null if no such document exists.
	 */
	public abstract Document nextDoc(int docid);
}
