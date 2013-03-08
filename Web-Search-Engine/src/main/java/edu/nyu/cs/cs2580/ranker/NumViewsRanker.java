package edu.nyu.cs.cs2580.ranker;

import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.indexer.Indexer;
import edu.nyu.cs.cs2580.query.Query;

public class NumViewsRanker extends AbstractRanker {

	public NumViewsRanker(Indexer indexer) {
		super(indexer);
	}

	@Override
	public ScoredDocument runquery(Query query, Document doc) {
		int score = doc.getNumViews();
		return new ScoredDocument(doc, score);
	}

}
