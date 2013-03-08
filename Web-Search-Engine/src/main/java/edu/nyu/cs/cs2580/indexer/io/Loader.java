package edu.nyu.cs.cs2580.indexer.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.nyu.cs.cs2580.common.Options;
import edu.nyu.cs.cs2580.doc.DocumentRaw;

public abstract class Loader implements Iterator<DocumentRaw> {

	protected final static Logger logger = LogManager.getLogger(Loader.class);

	public interface LoaderType {
		public static final String SIMPLE = "simple-loader";
		public static final String WIKI = "wiki-loader";
	}

	public void loadCorpus(String corpus) throws FileNotFoundException {
		load(new File(corpus));
	}

	protected abstract void load(File file) throws FileNotFoundException;

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public static Loader createLoader() throws IOException {
		String loader = Options.loaderType();
		logger.info("Use corpus loader: {}", loader);
		if (LoaderType.SIMPLE.equals(loader)) {
			return new FullScanLoader();
		} else if (LoaderType.WIKI.equals(loader)) {
			return new InvertedLoader();
		}
		throw new IllegalArgumentException("Unrecognized corpus loader");
	}
}
