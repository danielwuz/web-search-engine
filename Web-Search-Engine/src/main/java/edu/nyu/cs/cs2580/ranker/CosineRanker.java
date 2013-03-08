package edu.nyu.cs.cs2580.ranker;

import java.util.Vector;

import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.doc.Term;
import edu.nyu.cs.cs2580.indexer.Indexer;
import edu.nyu.cs.cs2580.query.Query;

public class CosineRanker extends AbstractRanker {

	public CosineRanker(Indexer indexer) {
		super(indexer);
	}

	@Override
	public ScoredDocument runquery(Query query, Document doc) {
		double score = 0.0;
		double docLength = 0.0;
		double queryLength = 0.0;

		Vector<String> tokens = query.getTokens();
		for (String token : tokens) {
			// If a query term does not occur in the corpus,do not need to
			// include it in the query length normalization.
			if (!super.corpus.containsToken(token)) {
				continue;
			}
			Term term = super.corpus.getTerm(token);
			double tf_idf = computeTF_IDF(doc, term);
			// compute length to do normalization
			docLength += tf_idf * tf_idf;
			queryLength += 1;
			// accumulate score
			score += tf_idf;
		}
		// TODO normalization
		if (docLength > 0 && queryLength > 0) {
			// score = score / (Math.sqrt(docLength) * Math.sqrt(queryLength));
		}
		return new ScoredDocument(doc, score);
	}

	private double computeTF_IDF(Document doc, Term term) {
		double tf = doc.getTermFrequency(term);
		double idf = term.getInverseDocFreq();
		double tf_idf = tf * idf;
		return tf_idf;
	}

}
