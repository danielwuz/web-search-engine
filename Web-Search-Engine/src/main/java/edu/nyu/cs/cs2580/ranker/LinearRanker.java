package edu.nyu.cs.cs2580.ranker;

import java.util.Vector;

import edu.nyu.cs.cs2580.Document;
import edu.nyu.cs.cs2580.Index;
import edu.nyu.cs.cs2580.ScoredDocument;

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

	public LinearRanker(Index index_source) {
		super(index_source);
		RankerFactory factory = RankerFactory.instance();
		// instantiate rankers
		cosineRanker = factory.create(Ranker.TYPES.COSINE);
		qlRanker = factory.create(Ranker.TYPES.QL);
		phraseRanker = factory.create(Ranker.TYPES.PHRASE);
		numviewsRanker = factory.create(Ranker.TYPES.NUMVIEWS);
	}

	@Override
	public ScoredDocument runquery(Vector<String> query, Document doc) {
		ScoredDocument cos = cosineRanker.runquery(query, doc);
		ScoredDocument ql = qlRanker.runquery(query, doc);
		ScoredDocument phrase = phraseRanker.runquery(query, doc);
		ScoredDocument numviews = numviewsRanker.runquery(query, doc);
		// linear interpolation
		double score = (beta_cos * cos._score) + (beta_LMP * ql._score)
				+ (beta_phrase * phrase._score)
				+ (beta_numviews * numviews._score);
		return new ScoredDocument(doc._docid, doc.get_title_string(), score);
	}
}
