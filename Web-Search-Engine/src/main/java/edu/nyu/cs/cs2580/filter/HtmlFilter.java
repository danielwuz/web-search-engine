package edu.nyu.cs.cs2580.filter;


public class HtmlFilter extends Filter {

	@Override
	protected String filter(String content) {
		content = content.replaceAll("(?im)<head>.*</head>", "");
		content = content.replaceAll("(?im)<script>.*?</script>", "");
		content = content.replaceAll("<[^<>]+/?>", " ");
		return content;
	}

}
