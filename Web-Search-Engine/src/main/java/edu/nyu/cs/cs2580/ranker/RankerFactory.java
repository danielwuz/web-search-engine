package edu.nyu.cs.cs2580.ranker;

public class RankerFactory {

	String index_path;

	public RankerFactory(String index_path) {
		this.index_path = index_path;
	}

	public Ranker create(String token) {
		if (Ranker.TYPES.COSINE.equalsIgnoreCase(token)) {
			return new CosineRanker(index_path);
		} else if (Ranker.TYPES.QL.equalsIgnoreCase(token)) {
			return new QLRanker(index_path);
		} else if (Ranker.TYPES.PHRASE.equalsIgnoreCase(token)) {
			return new PhraseRanker(index_path);
		} else if (Ranker.TYPES.LINEAR.equalsIgnoreCase(token)) {
			return new LinearRanker(index_path);
		} else {
			return new SimpleRanker(index_path);
		}
	}
}
