package edu.nyu.cs.cs2580.searcher;

import edu.nyu.cs.cs2580.doc.Corpus;
import edu.nyu.cs.cs2580.doc.Document;
import edu.nyu.cs.cs2580.query.Query;

public class FullScanSearcher extends AbstractSearcher {

	public FullScanSearcher(Corpus corpus) {
		super(corpus);
	}

	@Override
	public Document nextDoc(Query query, int docid) {
		return corpus.getDocument(++docid);
	}

}
