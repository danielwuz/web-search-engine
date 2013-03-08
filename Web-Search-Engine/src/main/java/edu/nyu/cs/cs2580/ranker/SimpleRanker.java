package edu.nyu.cs.cs2580.ranker;

import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.indexer.Indexer;
import edu.nyu.cs.cs2580.query.Query;

/**
 * Score the document. Here we have provided a very simple ranking model,where a
 * document is scored 1.0 if it gets hit by at least one query term.
 * 
 * @author dawu
 * 
 */
public class SimpleRanker extends AbstractRanker {

	public SimpleRanker(Indexer indexer) {
		super(indexer);
	}

	@Override
	public ScoredDocument runquery(Query query, Document doc) {

		double score = 0.0;
		for (String queryToken : query.getTokens()) {
			if (doc.containsToken(queryToken)) {
				score = 1.0;
				break;
			}
			if (score > 0.0) {
				break;
			}
		}
		return new ScoredDocument(doc, score);
	}

}
