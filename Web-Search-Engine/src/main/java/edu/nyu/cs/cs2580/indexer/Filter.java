package edu.nyu.cs.cs2580.indexer;

import java.util.Scanner;

import org.apache.commons.lang.StringUtils;

import edu.nyu.cs.cs2580.common.Stemmer;

public abstract class Filter {

	protected Filter nextFilter = null;

	public Filter() {

	}

	public Filter(Filter next) {
		this.nextFilter = next;
	}

	public String process(String content) {
		if (nextFilter != null) {
			content = nextFilter.process(content);
		}
		content = this.filter(content);
		return content;
	}

	protected abstract String filter(String content);

	public static class StemmingFilter extends Filter {

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

	public static class HtmlFilter extends Filter {

		@Override
		protected String filter(String content) {
			if (StringUtils.isEmpty(content)) {
				return content;
			}
			content = content.replaceAll("<head>.*</head>", "");
			content = content.replaceAll("<[^>]+>", " ");
			return content;
		}

	}

	public static class SpecialCharacterFilter extends Filter {

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

	public static class DummyFilter extends Filter {

		@Override
		protected String filter(String content) {
			return content;
		}

	}
}
