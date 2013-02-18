package edu.nyu.cs.cs2580.ranker;

import java.util.Vector;

import edu.nyu.cs.cs2580.Document;
import edu.nyu.cs.cs2580.ScoredDocument;

/**
 * 
 * Simple ranker that does not use the Ranker class.
 * 
 * @author dawu
 * 
 */
public class SimpleRanker extends AbstractRanker {

	public SimpleRanker(String index_source) {
		super(index_source);
	}

	public ScoredDocument runquery(Vector<String> queryVector, Document doc) {
		// Get the document vector.
		Vector<String> dv = doc.get_title_vector();

		/*
		 * Score the document. A document is scored 1.0 if it gets hit by at
		 * least one query term.
		 */
		double score = 0.0;
		for (int i = 0; i < dv.size(); ++i) {
			for (int j = 0; j < queryVector.size(); ++j) {
				if (dv.get(i).equals(queryVector.get(j))) {
					score = 1.0;
					break;
				}
			}
		}
		return new ScoredDocument(doc._docid, doc.get_title_string(), score);
	}

}
