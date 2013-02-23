package edu.nyu.cs.cs2580;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import edu.nyu.cs.cs2580.ranker.Ranker;
import edu.nyu.cs.cs2580.ranker.RankerFactory;

class QueryHandler implements HttpHandler {
	private Logger _log = Logger.getLogger(QueryHandler.class);

	private static String plainResponse = "Request received, but I am not smart enough to echo yet!\n";

	private RankerFactory rankerFactory;

	public QueryHandler(String index_path) {
		this.rankerFactory = RankerFactory.instance(index_path);
	}

	public static Map<String, String> getQueryMap(String query) {
		String[] params = query.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			map.put(name, value);
		}
		return map;
	}

	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		if (!requestMethod.equalsIgnoreCase("GET")) { // GET requests only.
			return;
		}

		// Print the user request header.
		Headers requestHeaders = exchange.getRequestHeaders();
		_log.debug("Incoming request: ");
		for (String key : requestHeaders.keySet()) {
			_log.debug(key + ":" + requestHeaders.get(key) + "; ");
		}
		String queryResponse = "";
		String uriQuery = exchange.getRequestURI().getQuery();
		String uriPath = exchange.getRequestURI().getPath();

		if ((uriPath != null) && (uriQuery != null)) {
			if (uriPath.equals("/search")) {
				Map<String, String> query_map = getQueryMap(uriQuery);
				Set<String> keys = query_map.keySet();
				if (keys.contains("query")) {
					String ranker_type = query_map.get("ranker");
					// Invoke different ranking functions
					Ranker _ranker = rankerFactory.create(ranker_type);
					Vector<ScoredDocument> sds = _ranker.runquery(query_map
							.get("query"));
					Iterator<ScoredDocument> itr = sds.iterator();
					while (itr.hasNext()) {
						ScoredDocument sd = itr.next();
						if (queryResponse.length() > 0) {
							queryResponse = queryResponse + "\n";
						}
						queryResponse = queryResponse + query_map.get("query")
								+ "\t" + sd.asString();
					}
					if (queryResponse.length() > 0) {
						queryResponse = queryResponse + "\n";
					}
				}
			}
		}

		// Construct a simple response.
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/plain");
		exchange.sendResponseHeaders(200, 0); // arbitrary number of bytes
		OutputStream responseBody = exchange.getResponseBody();
		responseBody.write(queryResponse.getBytes());
		responseBody.close();
	}
}
