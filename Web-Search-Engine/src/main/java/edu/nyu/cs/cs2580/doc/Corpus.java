package edu.nyu.cs.cs2580.doc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Corpus implements Serializable {

	private static final long serialVersionUID = 58574016811172625L;

	// Maps each term to their integer representation
	private Map<String, Term> _dictionary = new HashMap<String, Term>();

	// All unique terms appeared in corpus. Offsets are integer representations.
	private Vector<String> _terms = new Vector<String>();

	// Stores all Document in memory.
	private Vector<Document> _documents = new Vector<Document>();

	private long totalTermFrequency = 0;

	/**
	 * whether token is contained in vocabulary
	 */
	public boolean containsToken(String token) {
		return _dictionary.containsKey(token);
	}

	public Term getTerm(String token) {
		return _dictionary.get(token);
	}

	public Term createTerm(String token) {
		if (!this.containsToken(token)) {
			int id = _terms.size();
			_terms.add(token);
			Term term = new Term(id, token);
			term.setCorpus(this);
			_dictionary.put(token, term);
		}
		return _dictionary.get(token);
	}

	public void increaseTermCount() {
		this.totalTermFrequency += 1;
	}

	public Document createDoc() {
		int docId = this.numOfDocs();
		Document doc = new Document(docId, this);
		this.addDocument(doc);
		return doc;
	}

	private void addDocument(Document doc) {
		this._documents.add(doc);
	}

	// Number of documents in the corpus.
	public int numOfDocs() {
		return this._documents.size();
	}

	// Number of term occurrences in the corpus. If a term appears 10 times, it
	// will be counted 10 times.
	public long numOfTerms() {
		return this.totalTermFrequency;
	}

	/**
	 * Random access to documents, used prominently in HW1. In HW2, this
	 * interface should only be used to retrieve intrinsic features of the
	 * document, not the term features.
	 * 
	 * @param docid
	 * @return
	 */
	public Document getDocument(int docId) {
		if (hasDoc(docId)) {
			return this._documents.get(docId);
		}
		return null;
	}

	public boolean hasDoc(int docId) {
		return docId >= 0 && docId < _documents.size();
	}

	public static Vector<String> getTermVector(Vector<Term> tokens) {
		Vector<String> retval = new Vector<String>();
		for (Term term : tokens) {
			retval.add(term.getToken());
		}
		return retval;
	}

	public int getDocFrequencyByToken(String token) {
		Term term = this.getTerm(token);
		return term == null ? 0 : term.getDocFrequency();
	}

	public int getCorpusFrequencyByToken(String token) {
		Term term = this.getTerm(token);
		return term == null ? 0 : term.getCorpusFreqency();
	}

	public List<Term> getTerms(List<String> tokens) {
		List<Term> terms = new ArrayList<Term>();
		for (String token : tokens) {
			terms.add(getTerm(token));
		}
		return terms;
	}
}
