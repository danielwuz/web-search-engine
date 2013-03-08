package edu.nyu.cs.cs2580.searcher;

import junit.framework.TestCase;
import edu.nyu.cs.cs2580.doc.Corpus;
import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.Term;
import edu.nyu.cs.cs2580.query.Query;

public class LinearSearcherTest extends TestCase {

	private LinearSearcher searcher;

	private Corpus corpus;

	private Term t1, t2, t3;

	private Document d0, d1, d2, d3, d4, d5, d6, d7, d8, d9;

	public void setUp() {
		// prepare corpus
		corpus = new Corpus();
		t1 = corpus.createTerm("t1");
		t2 = corpus.createTerm("t2");
		t3 = corpus.createTerm("t3");

		d0 = corpus.createDoc();
		d1 = corpus.createDoc();
		d2 = corpus.createDoc();
		d3 = corpus.createDoc();
		d4 = corpus.createDoc();
		d5 = corpus.createDoc();
		d6 = corpus.createDoc();
		d7 = corpus.createDoc();
		d8 = corpus.createDoc();
		d9 = corpus.createDoc();

		t1.addPost(d1);
		t1.addPost(d2);
		t1.addPost(d3);
		t1.addPost(d6);
		t1.addPost(d7);
		t1.addPost(d8);
		t1.addPost(d9);

		t2.addPost(d0);
		t2.addPost(d2);
		t2.addPost(d4);
		t2.addPost(d5);
		t2.addPost(d7);
		t2.addPost(d9);

		t3.addPost(d3);
		t3.addPost(d5);
		t3.addPost(d7);
		t3.addPost(d8);
		t3.addPost(d9);
	}

	public void testNextDocWithThreeTerms() {
		Query query = new Query("t1+t2+t3");
		searcher = new LinearSearcher(corpus, query);
		int docid = -1;
		Document next = searcher.nextDoc(docid);
		assertEquals(7, next.docId);
		next = searcher.nextDoc(next.docId);
		assertEquals(9, next.docId);
		next = searcher.nextDoc(next.docId);
		assertNull(next);
		// reset upon search finish
		assertEquals(t1.lastDocId, 0);
		assertEquals(t2.lastDocId, 0);
		assertEquals(t3.lastDocId, 0);
	}

	public void testNextDocWithTwoTerms() {
		Query query = new Query("t1+t2");
		searcher = new LinearSearcher(corpus, query);
		int docid = -1;
		Document next = searcher.nextDoc(docid);
		assertEquals(2, next.docId);
		next = searcher.nextDoc(next.docId);
		assertEquals(7, next.docId);
		next = searcher.nextDoc(next.docId);
		assertEquals(9, next.docId);
		next = searcher.nextDoc(next.docId);
		assertNull(next);
	}

	public void testNextDocWithOneTerm() {
		Query query = new Query("t2");
		searcher = new LinearSearcher(corpus, query);
		int docid = -1;
		Document next = searcher.nextDoc(docid);
		assertEquals(0, next.docId);
		next = searcher.nextDoc(next.docId);
		assertEquals(2, next.docId);
		next = searcher.nextDoc(next.docId);
		assertEquals(4, next.docId);
		next = searcher.nextDoc(next.docId);
		assertEquals(5, next.docId);
		next = searcher.nextDoc(next.docId);
		assertEquals(7, next.docId);
		next = searcher.nextDoc(next.docId);
		assertEquals(9, next.docId);
		next = searcher.nextDoc(next.docId);
		assertNull(next);
	}
}
