package edu.nyu.cs.cs2580.filter;

import java.util.Scanner;

import edu.nyu.cs.cs2580.common.Stemmer;

public class StemmingFilter extends Filter {

	Stemmer stemmer = new Stemmer();

	public StemmingFilter(Filter next) {
		super(next);
	}

	@Override
	protected String filter(String content) {
		StringBuffer buffer = new StringBuffer();
		Scanner s = new Scanner(content);
		while (s.hasNext()) {
			String token = s.next();
			token = token.toLowerCase().trim();
			token = stemmer.stem(token);
			buffer.append(token + " ");
		}
		s.close();
		return buffer.toString();
	}

}
