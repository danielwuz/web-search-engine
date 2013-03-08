package edu.nyu.cs.cs2580.searcher;

import java.util.List;
import java.util.Vector;

import edu.nyu.cs.cs2580.doc.Corpus;
import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.Term;
import edu.nyu.cs.cs2580.query.Query;

public class LinearSearcher extends AbstractSearcher {

	public LinearSearcher(Corpus corpus) {
		super(corpus);
	}

	@Override
	public Document nextDoc(Query query, int docid) {
		Vector<String> tokens = query.getTokens();
		List<Term> terms = corpus.getTerms(tokens);
		boolean agreed = false;
		while (!agreed) {
			for (Term term : terms) {
				Document doc = nextDocument(terms.get(0), docid);
				if (doc == null) {
					//not found
					return null;
				}
				
			}
		}
		if (terms.size() == 1) {
			// single token in query
			return nextDocument(terms.get(0), docid);
		}
		Document result = null;
		for (int i = 0; i < terms.size() - 1; i++) {
			Term w1 = terms.get(i);
			Term w2 = terms.get(i + 1);
			result = nextDocument(w1, w2, docid);
			if (result != null) {
				docid = result.docId - 1;
			}
		}
		return result;
	}

	private Document nextDocument(Term w1, Term w2, int docid) {
		Document doc1 = nextDocument(w1, docid);
		Document doc2 = nextDocument(w2, docid);
		if (doc1 == null || doc2 == null) {
			return null;
		}
		if (doc1.equals(doc2)) {
			// found
			return doc1;
		}
		docid = Math.max(doc1.docId, doc2.docId);
		return nextDocument(w1, w2, docid - 1);
	}

	private Document nextDocument(Term w, int docid) {
		// TODO dummy impl, need to be improved
		List<Document> postings = w.getPostingList();
		for (Document post : postings) {
			if (post.docId > docid) {
				return post;
			}
		}
		return null;
	}
}
