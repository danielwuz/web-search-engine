package edu.nyu.cs.cs2580.ranker;

import java.util.Vector;

import edu.nyu.cs.cs2580.ScoredDocument;

public interface Ranker {

	interface TYPES {
		static String COSINE = "cosine";
		static String QL = "ql";
		static String PHRASE = "phrase";
		static String LINEAR = "linear";
	};

	Vector<ScoredDocument> runquery(String query);
}
