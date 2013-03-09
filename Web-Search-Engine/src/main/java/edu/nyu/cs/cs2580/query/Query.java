package edu.nyu.cs.cs2580.query;

import java.util.Scanner;
import java.util.Vector;

import edu.nyu.cs.cs2580.indexer.Filter;
import edu.nyu.cs.cs2580.indexer.io.Loader;

/**
 * Representation of a user query.
 * <p>
 * Tokens are separated by "+" (plus sign) in query string.
 * <p>
 * This class also supports phrase query. In case of phrase, query is surrounded
 * by double quotations. <br/>
 * For example If the raw query is "new york city", the presence of the phrase
 * "new york city" must be recorded here and be used in indexing and ranking.
 * 
 * @author dawu
 * @see edu.nyu.cs.cs2580.searcher.AbstractSearcher
 * @see edu.nyu.cs.cs2580.searcher.PhraseSearcher
 */
public class Query {
	// raw query string
	private final String query;

	// query tokens
	private final Vector<String> tokens;

	public Query(String query) {
		this.query = query;
		// Process the raw query into tokens.
		this.tokens = buildQuery(query);
	}

	public boolean isPhraseQuery() {
		return query != null && query.startsWith("\"") && query.endsWith("\"");
	}

	private Vector<String> buildQuery(String query) {
		if (query == null) {
			return new Vector<String>();
		}
		// Build query vector
		Scanner s = new Scanner(query);
		s.useDelimiter("\\+");
		Vector<String> qv = new Vector<String>();
		Filter filter = Loader.createLoader().getFilter();
		while (s.hasNext()) {
			String term = s.next();
			term = filter.process(term).trim();
			qv.add(term);
		}
		s.close();
		return qv;
	}

	public String getQuery() {
		return query;
	}

	public Vector<String> getTokens() {
		return tokens;
	}

}
