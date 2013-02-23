package edu.nyu.cs.cs2580.ranker;

import edu.nyu.cs.cs2580.Index;

public class RankerFactory {

	private Index _index;

	private static RankerFactory instance;

	public static RankerFactory instance(String index_path) {
		if (instance == null) {
			instance = new RankerFactory(index_path);
		}
		return instance;
	}

	public static RankerFactory instance() {
		if (instance == null) {
			throw new RuntimeException(
					"RankerFactory instance not instantiated yet");
		}
		return instance;
	}

	private RankerFactory(String index_path) {
		_index = new Index(index_path);
	}

	public Ranker create(String token) {
		if (Ranker.TYPES.COSINE.equalsIgnoreCase(token)) {
			return new CosineRanker(_index);
		} else if (Ranker.TYPES.QL.equalsIgnoreCase(token)) {
			return new QLRanker(_index);
		} else if (Ranker.TYPES.PHRASE.equalsIgnoreCase(token)) {
			return new PhraseRanker(_index);
		} else if (Ranker.TYPES.LINEAR.equalsIgnoreCase(token)) {
			return new LinearRanker(_index);
		} else if (Ranker.TYPES.NUMVIEWS.equalsIgnoreCase(token)) {
			return new NumViewsRanker(_index);
		} else {
			return new SimpleRanker(_index);
		}
	}
}
