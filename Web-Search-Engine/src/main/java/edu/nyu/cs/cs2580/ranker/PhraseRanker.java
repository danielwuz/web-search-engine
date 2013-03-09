package edu.nyu.cs.cs2580.ranker;

import java.util.List;

import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.doc.Term;
import edu.nyu.cs.cs2580.indexer.Indexer;
import edu.nyu.cs.cs2580.query.Query;

public class PhraseRanker extends AbstractRanker {

	public PhraseRanker(Indexer indexer) {
		super(indexer);
	}

	@Override
	public ScoredDocument runquery(Query query, Document doc) {
		// this input doc must have at least 1 phrase
		List<String> tokens = query.getTokens();
		List<Term> phrase = corpus.getTerms(tokens);
		int score = doc.countPhrase(phrase);
		return new ScoredDocument(doc, score);
	}
}
