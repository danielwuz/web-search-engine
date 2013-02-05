package edu.nyu.cs.cs2580;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Instructors' simple version. As implemented now, this version does not "echo"
 * the user query. It simply returns the same string and logs the user request
 * every time.
 */
public class EchoServer {

	// @CS2580: please use a port number 258XX, where XX corresponds
	// to your group number.
	private static int port = 25800;

	public static final String SEARCH_PATH = "/search";

	public static final String QUERY_STR = "query=";

	public static void main(String[] args) throws IOException {
		// Create the server.
		InetSocketAddress addr = new InetSocketAddress(port);
		HttpServer server = HttpServer.create(addr, -1);

		// Attach specific paths to their handlers.
		server.createContext("/", new EchoHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Listening on port: " + Integer.toString(port));
	}
}

/**
 * Instructors' simple version.
 */
class EchoHandler implements HttpHandler {
	private static String plainResponse = "Request received, but I am not smart enough to echo yet!\n";

	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		if (!requestMethod.equalsIgnoreCase("GET")) { // GET requests only.
			return;
		}

		// Print the user request header.
		Headers requestHeaders = exchange.getRequestHeaders();
		System.out.print("Incoming request: ");
		for (String key : requestHeaders.keySet()) {
			System.out.print(key + ":" + requestHeaders.get(key) + "; ");
		}
		System.out.println();
		// Parse http request
		URI params = exchange.getRequestURI();
		String path = params.getPath();
		if (EchoServer.SEARCH_PATH.equals(path)) {
			responseQuery(exchange);
		} else {
			responseUnknown(exchange);
		}
	}

	private void responseQuery(HttpExchange exchange) throws IOException {
		URI uri = exchange.getRequestURI();
		String query = uri.getQuery();
		String[] params = parseParameters(query);
		String msg = "";
		if (params != null) {
			for (String param : params) {
				msg += param + " ";
			}
		}
		msg += "\n";
		response(exchange, msg);
	}

	private String[] parseParameters(String query) {
		if (query.startsWith(EchoServer.QUERY_STR)) {
			query = query.replaceFirst(EchoServer.QUERY_STR, "");
			return query.split("\\+");
		}
		return null;
	}

	private void response(HttpExchange exchange, String msg) throws IOException {
		// Construct a simple response.
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/plain");
		exchange.sendResponseHeaders(200, 0); // arbitrary number of bytes
		OutputStream responseBody = exchange.getResponseBody();
		responseBody.write(msg.getBytes());
		responseBody.close();
	}

	private void responseUnknown(HttpExchange exchange) throws IOException {
		response(exchange, plainResponse);
	}
}