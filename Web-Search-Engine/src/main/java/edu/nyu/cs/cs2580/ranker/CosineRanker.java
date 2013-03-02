package edu.nyu.cs.cs2580.ranker;

import java.util.Vector;

import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.indexer.Index;

public class CosineRanker extends AbstractRanker {

	public CosineRanker(Index index_source) {
		super(index_source);
	}

	@Override
	public ScoredDocument runquery(Vector<String> query, Document doc) {
		double score = 0.0;
		double docLength = 0.0;
		double queryLength = 0.0;

		for (String term : query) {
			// If a query term does not occur in the corpus,do not need to
			// include it in the query length normalization.
			if (!Document.contains(term)) {
				continue;
			}
			double tf_idf = computeTF_IDF(doc, term);
			// compute length to do normalization
			docLength += tf_idf * tf_idf;
			queryLength += 1;
			// accumulate score
			score += tf_idf;
		}
		// normalization
		if (docLength > 0 && queryLength > 0) {
			score = score / (Math.sqrt(docLength) * Math.sqrt(queryLength));
		}
		return new ScoredDocument(doc._docid, doc.get_title_string(), score);
	}

	private double computeTF_IDF(Document doc, String term) {
		double tf = computeTF(doc, term);
		double idf = computeIDF(term);
		double tf_idf = tf * idf;
		return tf_idf;
	}

	private double computeTF(Document doc, String term) {
		// natural term frequency
		double tf = doc.getNaturalTermFreq(term);
		return tf;
	}

	private double computeIDF(String term) {
		double N = super.numDocs();
		int df = Document.documentFrequency(term);
		double idf = 1 + Math.log10(N * 1.0 / df);
		return idf;
	}
}
