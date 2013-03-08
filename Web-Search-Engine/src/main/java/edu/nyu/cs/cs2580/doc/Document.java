package edu.nyu.cs.cs2580.doc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

// @CS2580: This is a simple implementation that you will be changing
// in homework 2.  For this homework, don't worry about how this is done.
public class Document implements Serializable {

	private static final long serialVersionUID = 6915845015202985440L;

	public final int docId;

	protected Corpus corpus = null;

	// <K,V>=<term id, term frequency in current doc>
	private Map<Integer, Integer> termFrequency = new HashMap<Integer, Integer>();

	// <K,V>=<term id, term offset list>
	private Map<Integer, List<Integer>> termOffsets = new HashMap<Integer, List<Integer>>();

	protected Vector<Term> titleTokens = new Vector<Term>();

	protected Vector<Term> bodyTokens = new Vector<Term>();

	private DocumentRaw rawDoc;

	public Document(int docid, Corpus corpus) {
		this.docId = docid;
		this.corpus = corpus;
	}

	public void setDocumentRaw(DocumentRaw rawDoc) {
		this.rawDoc = rawDoc;
	}

	public void setTitleTokens(Vector<Term> titleTokens) {
		this.titleTokens = titleTokens;
		countTermFrequency(titleTokens);
	}

	private void countTermFrequency(Vector<Term> terms) {
		for (Term term : terms) {
			countTermFrequency(term);
		}
	}

	private void countTermFrequency(Term term) {
		Integer termId = term.getId();
		if (!termFrequency.containsKey(termId)) {
			termFrequency.put(termId, 0);
		}
		termFrequency.put(termId, termFrequency.get(termId) + 1);
	}

	public Vector<Term> getTitleTokens() {
		return this.titleTokens;
	}

	public void setBodyTokens(Vector<Term> bodyTokens) {
		this.bodyTokens = bodyTokens;
		countTermFrequency(bodyTokens);
		updateTermOccur(bodyTokens);
	}

	/**
	 * Indexes terms with their occurrences (i.e., offsets) in the documents.
	 * 
	 * @param tokens
	 *            terms
	 */
	private void updateTermOccur(Vector<Term> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Term term = tokens.get(i);
			writeTermOffset(term, i);
		}
	}

	private void writeTermOffset(Term term, int offset) {
		Integer termId = term.getId();
		if (!termOffsets.containsKey(termId)) {
			List<Integer> offsets = new ArrayList<Integer>();
			termOffsets.put(termId, offsets);
		}
		termOffsets.get(termId).add(offset);
	}

	public Vector<Term> getBodyTokens() {
		return this.bodyTokens;
	}

	public int getTermFrequency(Term term) {
		Integer tf = termFrequency.get(term.getId());
		return tf == null ? 0 : tf;
	}

	public int getBodyLength() {
		return bodyTokens.size();
	}

	public String getTitle() {
		return this.rawDoc.getTitle();
	}

	public boolean containsToken(String queryToken) {
		Integer term = corpus.getTerm(queryToken).getId();
		return this.termFrequency.containsKey(term);
	}

	public int getNumViews() {
		return this.rawDoc.getNumViews();
	}

	@Override
	public String toString() {
		return "Document [docId=" + docId + ", termOffsets=" + termOffsets
				+ "]";
	}

}
