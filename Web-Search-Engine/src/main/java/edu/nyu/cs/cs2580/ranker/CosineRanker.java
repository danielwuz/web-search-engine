package edu.nyu.cs.cs2580.ranker;

import java.util.Vector;

import edu.nyu.cs.cs2580.Document;
import edu.nyu.cs.cs2580.ScoredDocument;

public class CosineRanker extends AbstractRanker {

	public CosineRanker(String index_source) {
		super(index_source);
	}

	@Override
	protected ScoredDocument runquery(Vector<String> query, Document doc) {
		double score = 0.0;
		// TODO Auto-generated method stub
		return new ScoredDocument(doc._docid, doc.get_title_string(), score);
	}

}
