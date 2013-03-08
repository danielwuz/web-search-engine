package edu.nyu.cs.cs2580.ranker;

import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.indexer.Indexer;
import edu.nyu.cs.cs2580.query.Query;
import edu.nyu.cs.cs2580.server.CgiArguments;

/**
 * Instructors' code for illustration purpose. Non-tested code.
 * 
 * @author congyu
 */
public class RankerConjunctive extends AbstractRanker {

	public RankerConjunctive(Indexer indexer) {
		super(indexer);
		System.out.println("Using Ranker: " + this.getClass().getSimpleName());
	}

	@Override
	public ScoredDocument runquery(Query query, Document doc) {
		return new ScoredDocument(doc, 1.0);
	}

}
