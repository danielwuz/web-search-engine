package edu.nyu.cs.cs2580.filter;

public class DummyFilter extends Filter {

	@Override
	protected String filter(String content) {
		return content;
	}

}
