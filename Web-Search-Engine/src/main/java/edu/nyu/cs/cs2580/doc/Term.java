package edu.nyu.cs.cs2580.doc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Term implements Serializable {

	private static final long serialVersionUID = 9196791987858188257L;

	private final String token;

	private int id;

	// the number of times the term appears in the corpus, over full corpus
	private int corpusFrequency;

	// the number of documents the term appears in.
	private int docFrequency;

	private Double idf = null;

	private Corpus corpus;

	private List<Document> postingList = new ArrayList<Document>();

	public Term(int id, String token) {
		this.id = id;
		this.token = token;
	}

	public int getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public void addPost(Document doc) {
		this.postingList.add(doc);
	}

	public List<Document> getPostingList() {
		return this.postingList;
	}

	public void increaseCorpusFreq() {
		this.corpusFrequency += 1;
	}

	public void increasetDocFreq() {
		this.docFrequency += 1;
	}

	public int getDocFrequency() {
		return this.docFrequency;
	}

	public int getCorpusFreqency() {
		return this.corpusFrequency;
	}

	public double getInverseDocFreq() {
		if (idf == null) {
			idf = computeIDF();
		}
		return idf;
	}

	/**
	 * Searches successor document of given doc id.
	 * <p>
	 * Successor document is the one with id greater than given doc id and next
	 * to doc id.
	 * 
	 * @param docid
	 *            current document id
	 * @return successor of doc id; return -1 if not found
	 */
	public int next(int docid) {
		List<Document> postings = this.getPostingList();
		// search from last position
		for (int i = lastDocId; i < postings.size(); i++) {
			Document post = postings.get(i);
			if (post.docId > docid) {
				lastDocId = i;
				return post.docId;
			}
		}
		return -1;
	}

	public void resetCachedIndex() {
		this.lastDocId = 0;
	}

	// cache last search position
	public int lastDocId = 0;

	private double computeIDF() {
		assert corpus != null;
		double N = corpus.numOfDocs();
		int df = this.getDocFrequency();
		double idf = 1 + Math.log10(N * 1.0 / df);
		return idf;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void setCorpus(Corpus corpus) {
		this.corpus = corpus;
	}

	@Override
	public String toString() {
		return "Term [token=" + token + ", id=" + id + "]";
	}

}
