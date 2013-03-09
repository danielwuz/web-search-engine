package edu.nyu.cs.cs2580.searcher;

import java.util.Arrays;

import junit.framework.TestCase;
import edu.nyu.cs.cs2580.doc.Corpus;
import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.Term;
import edu.nyu.cs.cs2580.query.Query;

public class PhraseSearcherTest extends TestCase {

	private PhraseSearcher searcher;

	private Corpus corpus;

	private Term t1, t2, t3, t4, t5, t6;

	private Document d0, d1, d2;

	public void setUp() {
		// prepare corpus
		corpus = new Corpus();
		t1 = corpus.createTerm("this");
		t2 = corpus.createTerm("is");
		t3 = corpus.createTerm("a");
		t4 = corpus.createTerm("test");
		t5 = corpus.createTerm("phrase");
		t6 = corpus.createTerm("garbage");

		d0 = corpus.createDoc();
		d1 = corpus.createDoc();
		d2 = corpus.createDoc();

		// adding post order defines the order that term appears in doc
		t1.addPost(d0);
		t2.addPost(d0);
		t3.addPost(d0);
		t4.addPost(d0);
		t5.addPost(d0);
		Term[] v0 = { t1, t2, t3, t4, t5 };
		d0.setBodyTokens(Arrays.asList(v0));

		t2.addPost(d1);
		t5.addPost(d1);
		t4.addPost(d1);
		t1.addPost(d1);
		Term[] v3 = { t2, t5, t4, t1 };
		d1.setBodyTokens(Arrays.asList(v3));

		t6.addPost(d2);
		t4.addPost(d2);
		t5.addPost(d2);
		t3.addPost(d2);
		Term[] v2 = { t6, t4, t5, t3 };
		d2.setBodyTokens(Arrays.asList(v2));

	}

	public void testNull() {
		{
			Query query = new Query("a+is");
			searcher = new PhraseSearcher(corpus, query);
			Document doc = searcher.nextDoc(-1);
			assertNull(doc);
		}
		{
			Query query = new Query("t1+t3");
			searcher = new PhraseSearcher(corpus, query);
			Document doc = searcher.nextDoc(-1);
			assertNull(doc);
		}
	}

	public void testHitInDoc() {
		Query query = new Query("test+phrase");
		searcher = new PhraseSearcher(corpus, query);
		Document doc = searcher.nextDoc(-1);
		// found in doc0
		assertEquals(0, doc.docId);
		doc = searcher.nextDoc(doc.docId);
		// found in doc2
		assertEquals(2, doc.docId);
		// no more found
		doc = searcher.nextDoc(doc.docId);
		assertNull(doc);
	}

	public void testSingleToken() {
		Query query = new Query("test");
		searcher = new PhraseSearcher(corpus, query);
		// intendedly skip doc0
		Document doc = searcher.nextDoc(0);
		assertEquals(1, doc.docId);
		doc = searcher.nextDoc(doc.docId);
		// found in doc2
		assertEquals(2, doc.docId);

	}
}
