package edu.nyu.cs.cs2580.ranker;

import java.util.Vector;

import edu.nyu.cs.cs2580.Document;
import edu.nyu.cs.cs2580.Index;
import edu.nyu.cs.cs2580.ScoredDocument;

public class NumViewsRanker extends AbstractRanker {

	public NumViewsRanker(Index index_source) {
		super(index_source);
	}

	@Override
	public ScoredDocument runquery(Vector<String> query, Document doc) {
		int score = doc.get_numviews();
		return new ScoredDocument(doc._docid, doc.get_title_string(), score);
	}

}
