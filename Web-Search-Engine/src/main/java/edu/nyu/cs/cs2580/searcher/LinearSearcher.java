package edu.nyu.cs.cs2580.searcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.nyu.cs.cs2580.doc.Corpus;
import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.Term;
import edu.nyu.cs.cs2580.query.Query;

public class LinearSearcher extends AbstractSearcher {

	private List<Term> terms;

	public LinearSearcher(Corpus corpus, Query query) {
		super(corpus, query);
		Vector<String> tokens = query.getTokens();
		this.terms = corpus.getTerms(tokens);
	}

	@Override
	public Document nextDoc(int docid) {
		if (terms.isEmpty()) {
			// no query criteria
			return null;
		}
		while (true) {
			Set<Integer> docIds = new HashSet<Integer>();
			for (Term term : terms) {
				docid = term.next(docid);
				if (docid == -1) {
					// not found
					resetTerms();
					return null;
				}
				docIds.add(docid);
				// update for next term
				docid -= 1;
			}
			if (docIds.size() == 1) {
				// agreed
				return corpus.getDocument(docid + 1);
			}
		}
	}

	private void resetTerms() {
		for (Term term : terms) {
			term.resetCachedIndex();
		}
	}
}
