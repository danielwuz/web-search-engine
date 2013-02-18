package edu.nyu.cs.cs2580.ranker;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

import edu.nyu.cs.cs2580.Document;
import edu.nyu.cs.cs2580.Index;
import edu.nyu.cs.cs2580.ScoredDocument;

public abstract class AbstractRanker implements Ranker {

	private Index _index;

	public AbstractRanker(String index_source) {
		_index = new Index(index_source);
	}

	public Vector<ScoredDocument> runquery(String query) {
		Vector<ScoredDocument> retrieval_results = new Vector<ScoredDocument>();
		Vector<String> qv = buildQuery(query);
		Iterator<Document> it = _index.iterator();
		while (it.hasNext()) {
			Document doc = it.next();
			retrieval_results.add(runquery(qv, doc));
		}
		return retrieval_results;
	}

	protected abstract ScoredDocument runquery(Vector<String> query,
			Document doc);

	private Vector<String> buildQuery(String query) {
		// Build query vector
		Scanner s = new Scanner(query);
		Vector<String> qv = new Vector<String>();
		while (s.hasNext()) {
			String term = s.next();
			qv.add(term);
		}
		s.close();
		return qv;
	}
}
