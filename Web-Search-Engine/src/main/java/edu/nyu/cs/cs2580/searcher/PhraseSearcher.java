package edu.nyu.cs.cs2580.searcher;

import java.util.List;

import edu.nyu.cs.cs2580.doc.Corpus;
import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.Term;
import edu.nyu.cs.cs2580.query.Query;

/**
 * A document searcher implementation providing ability of searching a phrase.
 * <p>
 * For example, if document contains content :
 * 
 * <pre>
 *  This is a phrase example.
 * </pre>
 * 
 * then this document is considered as a hit for query "phrase example", since
 * token "phrase" and "example" appear contiguously; <br/>
 * but not for query "This example", for "example" are not shown up right behind
 * "This".
 * 
 * @author dawu
 * 
 */
public class PhraseSearcher extends AbstractSearcher {

	private LinearSearcher linearSearcher;

	private List<Term> phrase;

	public PhraseSearcher(Corpus corpus, Query query) {
		super(corpus, query);
		linearSearcher = new LinearSearcher(corpus, query);
		phrase = corpus.getTerms(query.getTokens());
	}

	/**
	 * Searches successor of current document containing phrase in
	 * <code>query</code>.
	 * <p>
	 * This procedure is similar to <code>LinearSearcher</code>. <br/>
	 * First, get next document in intersection of posting lists. Then check if
	 * that document contains query phrase. Returns a hit if yes, otherwise keep
	 * searching. If none found at the end, return null.
	 * 
	 * @param docid
	 *            current document id
	 * @return successor of docid which contains phrase; return null if not
	 *         found.
	 */
	public Document nextDoc(int docid) {
		Document doc = linearSearcher.nextDoc(docid);
		if (doc == null) {
			return null;
		}
		if (doc.containsPhrase(phrase)) {
			return doc;
		}
		return nextDoc(doc.docId);
	}

}
