package edu.nyu.cs.cs2580.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Stores all the options and configurations used in our search engine. For
 * simplicity, all options are publicly accessible.
 */
public class Options {

	private static Logger logger = LogManager.getLogger(Options.class);

	private static Options config;

	private Map<String, String> options = new HashMap<String, String>();

	/**
	 * Constructor for options.
	 * 
	 * @param optionFile
	 *            where all the options must reside
	 * @throws IOException
	 */
	private Options(String optionsFile) throws IOException {
		// Read options from the file.
		BufferedReader reader = new BufferedReader(new FileReader(optionsFile));
		String line = null;
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}
			String[] vals = line.split(":", 2);
			if (vals.length < 2) {
				reader.close();
			}
			options.put(vals[0].trim(), vals[1].trim());
		}
		reader.close();
	}

	public void checkOptions() {
		if (getProperty("corpus.path") == null) {
			logger.warn("Missing option: corpus.path!");
		}
		if (getProperty("index.path") == null) {
			logger.warn("Missing option: index.path!");
		}
		if (getProperty("indexer.type") == null) {
			logger.warn("Missing option: indexer.type!");
		}
		if (getProperty("eval.corpus.path") == null) {
			logger.warn("Missing option: eval.corpus.path!");
		}
		if (getProperty("corpus.loader") == null) {
			logger.warn("Missing option: corpus.loader!");
		}
	}

	private static String getProperty(String key) {
		assert config != null;
		return config.options.get(key);
	}

	public static void initialize(String value) throws IOException {
		config = new Options(value);
		config.checkOptions();
	}

	public static String indexerType() {
		return getProperty("indexer.type");
	}

	public static String indexPath() {
		return getProperty("index.path");
	}

	public static String corpusPath() {
		return getProperty("corpus.path");
	}

	public static String evalCorpus() {
		return getProperty("eval.corpus.path");
	}

	public static String loaderType() {
		return getProperty("corpus.loader");
	}

}
