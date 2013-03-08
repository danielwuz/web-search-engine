package edu.nyu.cs.cs2580.ranker;

import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.indexer.Indexer;
import edu.nyu.cs.cs2580.query.Query;
import edu.nyu.cs.cs2580.server.CgiArguments.RankerType;

public class LinearRanker extends AbstractRanker {

	// TODO
	private double beta_cos = 0.3;

	private double beta_LMP = 0.3;

	private double beta_phrase = 0.3;

	private double beta_numviews = 0.1;

	private Ranker cosineRanker;

	private Ranker qlRanker;

	private Ranker phraseRanker;

	private Ranker numviewsRanker;

	public LinearRanker(Indexer indexer) {
		super(indexer);
		RankerFactory factory = new RankerFactory(indexer);
		// instantiate rankers
		cosineRanker = factory.create(RankerType.COSINE);
		qlRanker = factory.create(RankerType.QL);
		phraseRanker = factory.create(RankerType.PHRASE);
		numviewsRanker = factory.create(RankerType.NUMVIEWS);
	}

	@Override
	public ScoredDocument runquery(Query query, Document doc) {
		ScoredDocument cos = cosineRanker.runquery(query, doc);
		ScoredDocument ql = qlRanker.runquery(query, doc);
		ScoredDocument phrase = phraseRanker.runquery(query, doc);
		ScoredDocument numviews = numviewsRanker.runquery(query, doc);
		// linear interpolation
		double score = (beta_cos * cos.getScore()) + (beta_LMP * ql.getScore())
				+ (beta_phrase * phrase.getScore())
				+ (beta_numviews * numviews.getScore());
		return new ScoredDocument(doc, score);
	}
}
