package edu.nyu.cs.cs2580.ranker;

import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.indexer.Indexer;
import edu.nyu.cs.cs2580.query.Query;

public class PhraseRanker extends AbstractRanker {

	public PhraseRanker(Indexer indexer) {
		super(indexer);
	}

	@Override
	public ScoredDocument runquery(Query query, Document doc) {
		// TODO
		return null;
	}

}
