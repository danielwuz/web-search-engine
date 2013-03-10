package edu.nyu.cs.cs2580.filter;

import org.apache.commons.lang.StringUtils;

public abstract class Filter {

	protected Filter nextFilter = null;

	public Filter() {

	}

	public Filter(Filter next) {
		this.nextFilter = next;
	}

	public String process(String content) {
		if (StringUtils.isEmpty(content)) {
			return content;
		}
		if (nextFilter != null) {
			content = nextFilter.process(content);
		}
		content = this.filter(content);
		return content;
	}

	protected abstract String filter(String content);

}
