package edu.nyu.cs.cs2580.indexer;

import org.apache.commons.lang.StringUtils;

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

		public StemmingFilter(Filter next) {
			super(next);
		}

		@Override
		protected String filter(String content) {
			return content.toLowerCase();
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
