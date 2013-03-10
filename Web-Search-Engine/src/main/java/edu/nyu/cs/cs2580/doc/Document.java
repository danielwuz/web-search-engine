package edu.nyu.cs.cs2580.doc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author dawu
 * 
 */
public class Document implements Serializable {

	private static final long serialVersionUID = 6915845015202985440L;

	public final int docId;

	protected Corpus corpus = null;

	// <K,V>=<term id, term frequency in current doc>
	protected Map<Integer, Integer> termFrequency = new HashMap<Integer, Integer>();

	// <K,V>=<term id, term offset list>
	protected Map<Integer, List<Integer>> termOffsets = new HashMap<Integer, List<Integer>>();

	protected List<Integer> titleTokens = new Vector<Integer>();

	protected List<Integer> bodyTokens = new Vector<Integer>();

	// Title field. Title field CANNOT contain character #, for # is the
	// delimiter when persisting document to disk.
	protected String title;

	protected int numViews;

	public Document(int docid) {
		this.docId = docid;
	}

	public Document(int docid, Corpus corpus) {
		this.docId = docid;
		this.corpus = corpus;
	}

	public void setDocumentRaw(DocumentRaw rawDoc) {
		this.title = rawDoc.getTitle();
		this.numViews = rawDoc.getNumViews();
	}

	public void setTitleTokens(List<Term> titleTokens) {
		for (Term term : titleTokens) {
			this.titleTokens.add(term.getId());
		}
		countTermFrequency(this.titleTokens);
	}

	private void countTermFrequency(List<Integer> terms) {
		for (Integer termId : terms) {
			countTermFrequency(termId);
		}
	}

	private void countTermFrequency(Integer termId) {
		if (!termFrequency.containsKey(termId)) {
			termFrequency.put(termId, 0);
		}
		termFrequency.put(termId, termFrequency.get(termId) + 1);
	}

	public List<Integer> getTitleTokens() {
		return this.titleTokens;
	}

	public void setBodyTokens(List<Term> bodyTokens) {
		for (Term term : bodyTokens) {
			this.bodyTokens.add(term.getId());
		}
		countTermFrequency(this.bodyTokens);
		updateTermOccur(this.bodyTokens);
	}

	/**
	 * Indexes terms with their occurrences (i.e., offsets) in the documents.
	 * 
	 * @param tokens
	 *            terms
	 */
	private void updateTermOccur(List<Integer> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Integer termId = tokens.get(i);
			writeTermOffset(termId, i);
		}
	}

	private void writeTermOffset(Integer termId, int offset) {
		if (!termOffsets.containsKey(termId)) {
			List<Integer> offsets = new ArrayList<Integer>();
			termOffsets.put(termId, offsets);
		}
		termOffsets.get(termId).add(offset);
	}

	public List<Integer> getBodyTokens() {
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
		return this.title;
	}

	public boolean containsToken(String queryToken) {
		Integer term = corpus.getTerm(queryToken).getId();
		return this.termFrequency.containsKey(term);
	}

	public int getNumViews() {
		return this.numViews;
	}

	@Override
	public String toString() {
		return "Document [docId=" + docId + ", termOffsets=" + termOffsets
				+ "]";
	}

	public int countPhrase(List<Term> phrase) {
		int count = 0;
		int end = bodyTokens.size() - phrase.size() + 1;
		for (int i = 0; i < end; i++) {
			if (isMatchFromPosition(i, phrase)) {
				count++;
			}
		}
		return count;

	}

	public boolean containsPhrase(List<Term> phrase) {
		int end = bodyTokens.size() - phrase.size() + 1;
		for (int i = 0; i < end; i++) {
			if (isMatchFromPosition(i, phrase)) {
				return true;
			}
		}
		return false;
	}

	private boolean isMatchFromPosition(int i, List<Term> phrase) {
		for (int j = 0; j < phrase.size(); j++) {
			Integer termId1 = bodyTokens.get(i + j);
			Integer termId2 = phrase.get(j).getId();
			if (!termId1.equals(termId2)) {
				return false;
			}
		}
		return true;
	}

	public void setCorpus(Corpus corpus) {
		this.corpus = corpus;
	}

}
