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
	protected Map<String, Term> _dictionary = new HashMap<String, Term>();

	// Stores all Document in memory.
	private Map<Integer, Document> _documents = new HashMap<Integer, Document>();

	protected long totalTermFrequency = 0;

	protected int termCount = 0;

	protected int docCount = 0;

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
			int id = termCount++;
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
		int docId = docCount++;
		Document doc = new Document(docId, this);
		// this.addDocument(doc);
		return doc;
	}

	public void addDocument(Document doc) {
		doc.setCorpus(this);
		this._documents.put(doc.docId, doc);
	}

	// Number of documents in the corpus.
	public int numOfDocs() {
		return this.docCount;
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
		return this._documents.get(docId);
	}

	public boolean hasDoc(int docId) {
		return _documents.containsKey(docId);
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
			Term term = getTerm(token);
			if (term == null) {
				// if given token not exists in vocabulary, ignore
				continue;
			}
			terms.add(term);
		}
		return terms;
	}

}
