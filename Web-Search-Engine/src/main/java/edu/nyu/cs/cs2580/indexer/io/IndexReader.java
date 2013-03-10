package edu.nyu.cs.cs2580.indexer.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.nyu.cs.cs2580.common.Options;
import edu.nyu.cs.cs2580.doc.Corpus;
import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.doc.SerializeUtil;

public class IndexReader implements Readable {

	protected static final Logger logger = LogManager
			.getLogger(IndexReader.class);

	@Override
	public Corpus loadIndex() throws IOException {
		Corpus corpus = loadCorpus();
		// load documents
		List<Document> docs = loadDocs();
		// reform
		for (Document doc : docs) {
			corpus.addDocument(doc);
		}
		return corpus;
	}

	private List<Document> loadDocs() throws IOException {
		DocumentIO io = new DocumentIO();
		return io.loadDocuments();
	}

	private Corpus loadCorpus() throws IOException {
		String indexFile = Options.corpusIndexPath();
		logger.info("Load corpus index from: {} ", indexFile);
		BufferedReader reader = new BufferedReader(new FileReader(indexFile));
		try {
			String corpusString = reader.readLine();
			Corpus corpus = SerializeUtil.instance().toCorpus(corpusString);
			logger.info("{} documents loaded with {} terms",
					corpus.numOfDocs(), corpus.numOfTerms());
			return corpus;
		} finally {
			reader.close();
		}
	}
}
