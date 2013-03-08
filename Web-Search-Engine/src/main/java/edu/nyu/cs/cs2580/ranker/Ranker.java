package edu.nyu.cs.cs2580.ranker;

import java.util.Vector;

import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.indexer.Indexer;
import edu.nyu.cs.cs2580.query.Query;
import edu.nyu.cs.cs2580.server.CgiArguments.RankerType;

public interface Ranker {

	ScoredDocument runquery(Query query, Document doc);

	Vector<ScoredDocument> runQuery(Query query, int numResults);

	public static class RankerFactory {
		private Indexer indexer;

		public RankerFactory(Indexer indexer) {
			this.indexer = indexer;
		}

		public Ranker create(RankerType ranker) {
			switch (ranker) {
			case CONJUNCTIVE:
				return new RankerConjunctive(indexer);
			case FAVORITE:
			case COSINE:
				return new CosineRanker(indexer);
			case QL:
				return new QLRanker(indexer);
			case PHRASE:
				return new PhraseRanker(indexer);
			case LINEAR:
				return new LinearRanker(indexer);
			case NUMVIEWS:
				return new NumViewsRanker(indexer);
			case FULLSCAN:
			case NONE:
				return new SimpleRanker(indexer);
			default:
				// Do nothing.
			}
			throw new IllegalArgumentException("Unrecognized ranker type:  "
					+ ranker);
		}
	}
}
