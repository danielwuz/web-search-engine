package edu.nyu.cs.cs2580.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpServer;

public class SearchEngine {
	private static Logger _logger = LogManager.getLogger(SearchEngine.class);

	// @CS2580: please use a port number 258XX, where XX corresponds
	// to your group number.
	public static void main(String[] args) throws IOException {
		// Create the server.
		if (args.length < 2) {
			_logger.error("arguments for this program are: [PORT] [PATH-TO-CORPUS]");
			return;
		}
		int port = Integer.parseInt(args[0]);
		String index_path = args[1];
		InetSocketAddress addr = new InetSocketAddress(port);
		HttpServer server = HttpServer.create(addr, -1);

		// Attach specific paths to their handlers.
		server.createContext("/", new QueryHandler(index_path));
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		_logger.info("Listening on port: " + Integer.toString(port));
	}
}
