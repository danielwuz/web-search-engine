package edu.nyu.cs.cs2580.filter;


public class SpecialCharacterFilter extends Filter {

	public SpecialCharacterFilter(Filter next) {
		super(next);
	}

	@Override
	protected String filter(String content) {
		content = content.replaceAll("\\W", " ");
		content = content.replaceAll("\\s+", " ");
		return content;
	}

}
