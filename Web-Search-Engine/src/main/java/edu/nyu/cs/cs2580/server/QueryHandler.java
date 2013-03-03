package edu.nyu.cs.cs2580.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.evaluator.Evaluator;
import edu.nyu.cs.cs2580.ranker.Ranker;
import edu.nyu.cs.cs2580.ranker.RankerFactory;

class QueryHandler implements HttpHandler {

	private static Logger _log = LogManager.getLogger(QueryHandler.class);

	private static String plainResponse = "Request received, but I am not smart enough to echo yet!\n";

	private RankerFactory rankerFactory;

	public QueryHandler(String index_path) {
		this.rankerFactory = RankerFactory.instance(index_path);
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

		QueryParams query_map = new QueryParams(exchange);
		if (query_map.searchable()) {
			Ranker _ranker = query_map.getRanker();
			// Invoke different ranking functions
			String query = query_map.getQuery();
			List<ScoredDocument> sds = _ranker.runquery(query);
			Collections.sort(sds);
			queryResponse += convertToString(query, sds);
		}

		// evaluate
		if (query_map.evaluable()) {
			Evaluator e = new Evaluator(queryResponse);
			queryResponse = e.eval();
		}

		// Construct a simple response.
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/plain");
		exchange.sendResponseHeaders(200, 0); // arbitrary number of bytes
		OutputStream output = query_map.getOutput();
		output.write(query_map.toString().getBytes());
		output.write(queryResponse.getBytes());
		output.close();
	}

	private String convertToString(String query, List<ScoredDocument> sds) {
		StringBuffer buffer = new StringBuffer();
		Iterator<ScoredDocument> itr = sds.iterator();
		while (itr.hasNext()) {
			ScoredDocument sd = itr.next();
			buffer.append(URLDecoder.decode(query) + "\t" + sd.asString());
			buffer.append("\n");
		}
		return buffer.toString();
	}

	private class QueryParams {

		private HttpExchange exchange;

		private Map<String, String> query_map = new HashMap<String, String>();

		public QueryParams(HttpExchange exchange) {
			this.exchange = exchange;
			String uriQuery = exchange.getRequestURI().getQuery();
			this.setQueryMap(uriQuery);
		}

		public boolean evaluable() {
			String eval = query_map.get("evaluate");
			return "true".equalsIgnoreCase(eval);
		}

		public boolean searchable() {
			String uriQuery = exchange.getRequestURI().getQuery();
			String uriPath = exchange.getRequestURI().getPath();
			return (uriPath != null) && (uriQuery != null)
					&& uriPath.equals("/search");
		}

		public boolean outputHTML() {
			String os = query_map.get("format");
			return "html".equals(os);
		}

		public OutputStream getOutput() {
			if (this.outputHTML()) {
				return exchange.getResponseBody();
			}
			return System.out;
		}

		public Ranker getRanker() {
			String rankerType = query_map.get("ranker");
			return rankerFactory.create(rankerType);
		}

		public String getQuery() {
			return query_map.get("query");
		}

		private Map<String, String> setQueryMap(String query) {
			// default value
			query_map.put("query", "test");
			query_map.put("ranker", "simple");
			query_map.put("format", "html");
			query_map.put("evaluate", "false");
			// value from HTTP request
			String[] params = query.split("&");
			for (String param : params) {
				String name = param.split("=")[0];
				String value = param.split("=")[1];
				query_map.put(name, value);
			}
			return query_map;
		}

		@Override
		public String toString() {
			return "QueryParams [query_map=" + query_map + "]\n";
		}
	}
}
