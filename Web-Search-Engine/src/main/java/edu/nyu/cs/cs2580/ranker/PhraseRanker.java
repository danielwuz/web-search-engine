package edu.nyu.cs.cs2580.ranker;

import java.util.Vector;

import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.indexer.Index;

public class PhraseRanker extends AbstractRanker {

	public PhraseRanker(Index index_source) {
		super(index_source);
	}

	@Override
	public ScoredDocument runquery(Vector<String> query, Document doc) {
		double score = 0;
		boolean isBigram = query.size() > 1;
		// compute phrase score
		if (isBigram) {
			score += countBigramPhrase(query, doc);
		} else {
			String term = query.get(0);
			score += countUnigramPhrase(term, doc);
		}
		return new ScoredDocument(doc._docid, doc.get_title_string(), score);
	}

	private double countUnigramPhrase(String term, Document doc) {
		int count = 0;
		Vector<String> body = doc.get_body_vector();
		for (int j = 0; j < body.size(); j++) {
			String p_i = body.get(j);
			if (p_i.equals(term)) {
				count++;
			}
		}
		return count;
	}

	private double countBigramPhrase(Vector<String> query, Document doc) {
		int count = 0;
		for (int i = 0; i < query.size() - 1; i++) {
			String q_i = query.get(i);
			String q_i_1 = query.get(i + 1);
			Vector<String> body = doc.get_body_vector();
			for (int j = 0; j < body.size() - 1; j++) {
				String p_i = body.get(j);
				String p_i_1 = body.get(j + 1);
				if (p_i.equals(q_i) && p_i_1.equals(q_i_1)) {
					count++;
				}
			}
		}
		return count;
	}

}
