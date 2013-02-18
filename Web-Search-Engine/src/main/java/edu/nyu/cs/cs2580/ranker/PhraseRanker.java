package edu.nyu.cs.cs2580.ranker;

import java.util.Vector;

import edu.nyu.cs.cs2580.Document;
import edu.nyu.cs.cs2580.ScoredDocument;

public class PhraseRanker extends AbstractRanker {

	public PhraseRanker(String index_source) {
		super(index_source);
	}

	@Override
	protected ScoredDocument runquery(Vector<String> query, Document doc) {
		return null;
	}

}
