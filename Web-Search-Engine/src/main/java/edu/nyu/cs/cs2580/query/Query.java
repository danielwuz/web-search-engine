package edu.nyu.cs.cs2580.query;

import java.util.Scanner;
import java.util.Vector;

/**
 * Representation of a user query.
 * 
 * In HW1: instructors provide this simple implementation.
 * 
 * In HW2: students must implement {@link QueryPhrase} to handle phrases.
 * 
 * @author congyu
 * @auhtor fdiaz
 */
public class Query {
	private final String query;
	private final Vector<String> tokens;

	public Query(String query) {
		this.query = query;
		// Process the raw query into tokens.
		this.tokens = buildQuery(query);
	}

	private Vector<String> buildQuery(String query) {
		if (query == null) {
			return new Vector<String>();
		}
		// Build query vector
		Scanner s = new Scanner(query);
		s.useDelimiter("\\+");
		Vector<String> qv = new Vector<String>();
		while (s.hasNext()) {
			String term = s.next();
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
