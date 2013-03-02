package edu.nyu.cs.cs2580.ranker;

import java.util.Vector;

import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.indexer.Index;

public class QLRanker extends AbstractRanker {

	public static double LAMBDA = 0.5;

	public QLRanker(Index index_source) {
		super(index_source);
	}

	@Override
	public ScoredDocument runquery(Vector<String> query, Document doc) {
		double score = 0.0;
		for (String term : query) {
			// add term score
			if (Document.contains(term)) {
				score += ql_score(doc, term);
			}
		}
		return new ScoredDocument(doc._docid, doc.get_title_string(), score);
	}

	private double ql_score(Document doc, String term) {
		int C = Document.termFrequency();
		int cq = Document.termFrequency(term);
		int ntf = doc.getNaturalTermFreq(term);
		int D = doc.getBodyLength();
		return (1 - LAMBDA) * (ntf * 1.0 / D) + LAMBDA * (cq * 1.0 / C);
	}

}
