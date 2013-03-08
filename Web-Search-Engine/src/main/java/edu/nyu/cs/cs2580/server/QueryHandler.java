package edu.nyu.cs.cs2580.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import edu.nyu.cs.cs2580.doc.ScoredDocument;
import edu.nyu.cs.cs2580.indexer.Indexer;
import edu.nyu.cs.cs2580.query.Query;
import edu.nyu.cs.cs2580.ranker.Ranker;
import edu.nyu.cs.cs2580.ranker.Ranker.RankerFactory;

class QueryHandler implements HttpHandler {

	private static Logger _logger = LogManager.getLogger(QueryHandler.class);

	// For accessing the underlying documents to be used by the Ranker. Since
	// we are not worried about thread-safety here, the Indexer class must take
	// care of thread-safety.
	private Indexer _indexer;

	public QueryHandler(Indexer indexer) {
		_indexer = indexer;
	}

	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		if (!requestMethod.equalsIgnoreCase("GET")) { // GET requests only.
			return;
		}

		// Print the user request header.
		Headers requestHeaders = exchange.getRequestHeaders();
		_logger.debug("Incoming request: ");
		for (String key : requestHeaders.keySet()) {
			_logger.debug("{} : {} ;", key, requestHeaders.get(key));
		}

		// Validate the incoming request.
		String uriQuery = exchange.getRequestURI().getQuery();
		String uriPath = exchange.getRequestURI().getPath();
		if (uriPath == null || uriQuery == null) {
			respondWithMsg(exchange, "Something wrong with the URI!");
		}
		if (!uriPath.equals("/search")) {
			respondWithMsg(exchange, "Only /search is handled!");
		}
		_logger.debug("Query: {}", uriQuery);

		// Process the CGI arguments.
		CgiArguments cgiArgs = new CgiArguments(uriQuery);
		if (cgiArgs._query.isEmpty()) {
			respondWithMsg(exchange, "No query is given!");
		}

		// Create the ranker.
		Ranker ranker = new RankerFactory(_indexer).create(cgiArgs._rankerType);
		assert ranker != null;

		// Processing the query.
		Query processedQuery = new Query(cgiArgs._query);

		// Ranking.
		Vector<ScoredDocument> scoredDocs = ranker.runQuery(processedQuery,
				cgiArgs._numResults);
		StringBuffer response = new StringBuffer();
		switch (cgiArgs._outputFormat) {
		case TEXT:
			constructTextOutput(scoredDocs, response);
			break;
		case HTML:
			// @CS2580: Plug in your HTML output
			response = convertToString(cgiArgs._query, scoredDocs);
			break;
		default:
			// nothing
		}
		respondWithMsg(exchange, response.toString());
		_logger.debug("Finished query: {}", cgiArgs._query);
	}

	private void constructTextOutput(final Vector<ScoredDocument> docs,
			StringBuffer response) {
		for (ScoredDocument doc : docs) {
			response.append(response.length() > 0 ? "\n" : "");
			response.append(doc.asTextResult());
		}
		response.append(response.length() > 0 ? "\n" : "");
	}

	private void respondWithMsg(HttpExchange exchange, final String message)
			throws IOException {
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/plain");
		exchange.sendResponseHeaders(200, 0); // arbitrary number of bytes
		OutputStream responseBody = exchange.getResponseBody();
		responseBody.write(message.getBytes());
		responseBody.close();
	}

	private StringBuffer convertToString(String query,
			Vector<ScoredDocument> sds) {
		// TODO
		StringBuffer buffer = new StringBuffer();
		Iterator<ScoredDocument> itr = sds.iterator();
		while (itr.hasNext()) {
			ScoredDocument sd = itr.next();
			buffer.append(URLDecoder.decode(query) + "\t" + sd.asString());
			buffer.append("\n");
		}
		return buffer;
	}

}
