package edu.nyu.cs.cs2580.indexer.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.nyu.cs.cs2580.common.Options;
import edu.nyu.cs.cs2580.doc.Corpus;
import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.DocumentRaw;
import edu.nyu.cs.cs2580.doc.SerializeUtil;
import edu.nyu.cs.cs2580.doc.Term;
import edu.nyu.cs.cs2580.indexer.loader.Loader;

public class IndexWriter implements Writable {

	protected static final Logger logger = LogManager
			.getLogger(IndexWriter.class);

	private Loader loader;

	private Corpus corpus;

	public IndexWriter(Loader loader) {
		this.loader = loader;
		this.corpus = new Corpus();
	}

	@Override
	public void constructIndex() throws IOException {
		String corpusFile = Options.corpusPath();
		logger.info("Construct index from: {}", corpusFile);

		DocumentIO cache = new DocumentIO();
		this.loader.loadCorpus(corpusFile);
		while (loader.hasNext()) {
			DocumentRaw raw = loader.next();
			if (raw == null) {
				// ignore error
				continue;
			}
			Document doc = processDocument(raw);
			cache.addDocument(doc);
		}
		cache.flush();
		logger.info("Indexed {} docs with {} terms.", corpus.numOfDocs(),
				corpus.numOfTerms());
		// persist to file
		String indexFile = Options.corpusIndexPath();
		logger.info("Store index to: {}", indexFile);
		String corpusString = SerializeUtil.instance().asRecord(corpus);
		FileWriter writer = new FileWriter(indexFile);
		writer.write(corpusString);
		writer.close();
	}

	/**
	 * Process the raw content (i.e., one line in corpus.tsv) corresponding to a
	 * document, and constructs the token vectors for both title and body.
	 * 
	 * @param content
	 */
	protected Document processDocument(DocumentRaw raw) {
		Vector<Term> titleTokens = new Vector<Term>();
		readTermVector(raw.getTitle(), titleTokens);

		Vector<Term> bodyTokens = new Vector<Term>();
		readTermVector(raw.getBody(), bodyTokens);

		Document doc = corpus.createDoc();
		doc.setDocumentRaw(raw);
		doc.setTitleTokens(titleTokens);
		doc.setBodyTokens(bodyTokens);

		Set<Term> uniqueTerms = new HashSet<Term>();
		updateStatistics(titleTokens, uniqueTerms);
		updateStatistics(bodyTokens, uniqueTerms);
		// updateTermOccur(doc.getBodyTokens(), doc.docId);
		for (Term term : uniqueTerms) {
			term.addPost(doc);
		}
		logger.debug("Process document {}: {}", doc.docId, doc.getTitle());
		return doc;
	}

	/**
	 * Tokenize {@code content} into terms, translate terms into their integer
	 * representation, store the integers in {@code tokens}.
	 * 
	 * @param content
	 * @param tokens
	 */
	private void readTermVector(String content, Vector<Term> tokens) {
		Scanner s = new Scanner(content); // Uses white space by default.
		while (s.hasNext()) {
			String token = s.next();
			Term idx = corpus.createTerm(token);
			tokens.add(idx);
		}
		s.close();
	}

	/**
	 * Update the corpus statistics with {@code tokens}. Using {@code uniques}
	 * to bridge between different token vectors.
	 * 
	 * @param tokens
	 * @param uniques
	 */
	private void updateStatistics(Vector<Term> tokens, Set<Term> uniques) {
		for (Term term : tokens) {
			uniques.add(term);
			term.increaseCorpusFreq();
			corpus.increaseTermCount();
		}
	}
}
