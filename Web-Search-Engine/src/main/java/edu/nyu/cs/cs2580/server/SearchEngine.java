package edu.nyu.cs.cs2580.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpServer;

import edu.nyu.cs.cs2580.common.Options;
import edu.nyu.cs.cs2580.indexer.Indexer;

public class SearchEngine {

	private static Logger _logger = LogManager.getLogger(SearchEngine.class);

	/**
	 * Running mode of the search engine.
	 */
	public static enum Mode {
		NONE, INDEX, SERVE,
	};

	public static Mode MODE = Mode.NONE;

	public static int PORT = -1;

	public static void main(String[] args) {
		try {
			SearchEngine.parseCommandLine(args);
			checkParameters();
			switch (SearchEngine.MODE) {
			case INDEX:
				startIndexing();
				break;
			case SERVE:
				startServing();
				break;
			default:
				_logger.error("Wrong mode for SearchEngine!");
			}
		} catch (Exception e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private static void parseCommandLine(String[] args) throws IOException,
			NumberFormatException {
		for (String arg : args) {
			String[] vals = arg.split("=", 2);
			String key = vals[0].trim();
			String value = vals[1].trim();
			if (key.equals("--mode") || key.equals("-mode")) {
				try {
					MODE = Mode.valueOf(value.toUpperCase());
				} catch (IllegalArgumentException e) {
					// Ignored, error msg will be printed below.
				}
			} else if (key.equals("--port") || key.equals("-port")) {
				PORT = Integer.parseInt(value);
			} else if (key.equals("--options") || key.equals("-options")) {
				Options.initialize(value);
			}
		}
	}

	private static void checkParameters() {
		if (MODE == Mode.NONE) {
			throw new IllegalArgumentException(
					"Must provide a valid mode: serve or index!");
		}
		if (MODE == Mode.SERVE && PORT == -1) {
			throw new IllegalArgumentException(
					"Must provide a valid port number (258XX) in serve mode!");
		}
	}

	private static void startServing() throws IOException,
			ClassNotFoundException {
		// Create the handler and its associated indexer.
		String indexType = Options.indexerType();
		Indexer indexer = Indexer.Factory.getIndexer(indexType);
		assert indexer != null;
		indexer.loadIndex();
		QueryHandler handler = new QueryHandler(indexer);

		// Establish the serving environment
		InetSocketAddress addr = new InetSocketAddress(SearchEngine.PORT);
		HttpServer server = HttpServer.create(addr, -1);
		server.createContext("/", handler);
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		_logger.info("Listening on port: {} ", SearchEngine.PORT);
	}

	private static void startIndexing() throws IOException {
		String type = Options.indexerType();
		Indexer indexer = Indexer.Factory.getIndexer(type);
		assert indexer != null;
		indexer.constructIndex();
	}

	/**
	 * Prints {@code msg} and exits the program if {@code condition} is false.
	 */
	public static void Check(boolean condition, String msg) {
		if (!condition) {
			System.err.println("Fatal error: " + msg);
			System.exit(-1);
		}
	}

}
