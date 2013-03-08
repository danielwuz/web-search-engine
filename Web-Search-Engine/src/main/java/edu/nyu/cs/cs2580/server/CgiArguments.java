package edu.nyu.cs.cs2580.server;

/**
 * CGI arguments provided by the user through the URL. This will determine which
 * Ranker to use and what output format to adopt. For simplicity, all arguments
 * are publicly accessible.
 */
public class CgiArguments {

	// The raw user query
	public String _query = "";
	// How many results to return
	public int _numResults = 10;

	// The type of the ranker we will be using.
	public enum RankerType {
		NONE, FULLSCAN, CONJUNCTIVE, FAVORITE, COSINE, PHRASE, QL, LINEAR, NUMVIEWS
	}

	public RankerType _rankerType = RankerType.NONE;

	// The output format.
	public enum OutputFormat {
		TEXT, HTML,
	}

	public OutputFormat _outputFormat = OutputFormat.TEXT;

	public CgiArguments(String uriQuery) {
		String[] params = uriQuery.split("&");
		for (String param : params) {
			String[] keyval = param.split("=", 2);
			if (keyval.length < 2) {
				continue;
			}
			String key = keyval[0].toLowerCase();
			String val = keyval[1];
			if (key.equals("query")) {
				_query = val;
			} else if (key.equals("num")) {
				try {
					_numResults = Integer.parseInt(val);
				} catch (NumberFormatException e) {
					// Ignored, search engine should never fail upon invalid
					// user input.
				}
			} else if (key.equals("ranker")) {
				try {
					_rankerType = RankerType.valueOf(val.toUpperCase());
				} catch (IllegalArgumentException e) {
					// Ignored, search engine should never fail upon invalid
					// user input.
				}
			} else if (key.equals("format")) {
				try {
					_outputFormat = OutputFormat.valueOf(val.toUpperCase());
				} catch (IllegalArgumentException e) {
					// Ignored, search engine should never fail upon invalid
					// user input.
				}
			}
		} // End of iterating over params
	}

}
