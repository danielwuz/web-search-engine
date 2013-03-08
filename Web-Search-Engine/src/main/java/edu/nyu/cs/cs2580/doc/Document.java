package edu.nyu.cs.cs2580.doc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

// @CS2580: This is a simple implementation that you will be changing
// in homework 2.  For this homework, don't worry about how this is done.
public class Document implements Serializable {

	private static final long serialVersionUID = 6915845015202985440L;

	public final int docId;

	protected Corpus corpus = null;

	protected Map<Term, Integer> termFrequency = new HashMap<Term, Integer>();

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
		if (!termFrequency.containsKey(term)) {
			termFrequency.put(term, 0);
		}
		termFrequency.put(term, termFrequency.get(term) + 1);
	}

	public Vector<Term> getTitleTokens() {
		return this.titleTokens;
	}

	public void setBodyTokens(Vector<Term> bodyTokens) {
		this.bodyTokens = bodyTokens;
		countTermFrequency(bodyTokens);
	}
	
	private void updateTermOccur(Vector<Integer> tokens, int docId){
		
	}

	public Vector<Term> getBodyTokens() {
		return this.bodyTokens;
	}

	public int getTermFrequency(Term term) {
		Integer tf = termFrequency.get(term);
		return tf == null ? 0 : tf;
	}

	public int getBodyLength() {
		return bodyTokens.size();
	}

	public String getTitle() {
		return this.rawDoc.getTitle();
	}

	public boolean containsToken(String queryToken) {
		Term term = corpus.getTerm(queryToken);
		return this.termFrequency.containsKey(term);
	}

	public int getNumViews() {
		return this.rawDoc.getNumViews();
	}

	@Override
	public String toString() {
		return "Document [docId=" + docId + "]";
	}
	
}
