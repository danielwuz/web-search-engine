package edu.nyu.cs.cs2580.ranker;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

import edu.nyu.cs.cs2580.doc.Corpus;
import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.indexer.Indexer;
import edu.nyu.cs.cs2580.query.Query;
import edu.nyu.cs.cs2580.searcher.AbstractSearcher;

/**
 * This is the abstract Ranker class for all concrete Ranker implementations.
 * 
 * Use {@link Ranker.Factory} to create your concrete Ranker implementation. Do
 * NOT change the interface in this class!
 * 
 * In HW1: {@link RankerFullScan} is the instructor's simple ranker and students
 * implement four additional concrete Rankers.
 * 
 * In HW2: students will pick a favorite concrete Ranker other than
 * {@link RankerPhrase}, and re-implement it using the more efficient concrete
 * Indexers.
 * 
 * 2013-02-16: The instructor's code went through substantial refactoring
 * between HW1 and HW2, students are expected to refactor code accordingly.
 * Refactoring is a common necessity in real world and part of the learning
 * experience.
 * 
 * @author congyu
 * @author fdiaz
 */
public abstract class AbstractRanker implements Ranker {

	// The Indexer via which documents are retrieved, see {@code
	// IndexerFullScan}
	// for a concrete implementation. N.B. Be careful about thread safety here.
	protected Indexer indexer;

	protected Corpus corpus;

	/**
	 * Constructor: the construction of the Ranker requires an Indexer.
	 */
	public AbstractRanker(Indexer indexer) {
		this.indexer = indexer;
		this.corpus = indexer.corpus;
	}

	@Override
	public Vector<ScoredDocument> runQuery(Query query, int numResults) {
		Queue<ScoredDocument> rankQueue = new PriorityQueue<ScoredDocument>();
		AbstractSearcher searcher = indexer.createSearcher(query);
		Document doc = null;
		int docid = -1;
		while ((doc = searcher.nextDoc(docid)) != null) {
			ScoredDocument scoredDoc = runquery(query, doc);
			rankQueue.add(scoredDoc);
			if (rankQueue.size() > numResults) {
				rankQueue.poll();
			}
			docid = doc.docId;
		}

		Vector<ScoredDocument> results = new Vector<ScoredDocument>();
		ScoredDocument scoredDoc = null;
		while ((scoredDoc = rankQueue.poll()) != null) {
			results.add(scoredDoc);
		}
		Collections.sort(results, Collections.reverseOrder());
		return results;
	}

	public int numDocs() {
		return this.corpus.numOfDocs();
	}

}
