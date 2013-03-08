package edu.nyu.cs.cs2580.ranker;

import java.util.Vector;

import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.doc.Term;
import edu.nyu.cs.cs2580.indexer.Indexer;
import edu.nyu.cs.cs2580.query.Query;

public class QLRanker extends AbstractRanker {

	public QLRanker(Indexer indexer) {
		super(indexer);
	}

	public static double LAMBDA = 0.5;

	private double ql_score(Document doc, Term term) {
		long C = super.corpus.numOfTerms();
		double cq = term.getCorpusFreqency();
		int ntf = doc.getTermFrequency(term);
		int D = doc.getBodyLength();
		return (1 - LAMBDA) * (ntf * 1.0 / D) + LAMBDA * (cq * 1.0 / C);
	}

	@Override
	public ScoredDocument runquery(Query query, Document doc) {
		Vector<String> tokens = query.getTokens();
		double score = 0.0;
		for (String token : tokens) {
			// add term score
			if (super.corpus.containsToken(token)) {
				Term term = super.corpus.getTerm(token);
				score += ql_score(doc, term);
			}
		}
		return new ScoredDocument(doc, score);
	}

}
