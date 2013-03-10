package edu.nyu.cs.cs2580.indexer.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.nyu.cs.cs2580.common.Options;
import edu.nyu.cs.cs2580.doc.DocumentRaw;
import edu.nyu.cs.cs2580.filter.Filter;

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

	/**
	 * Filter that is used to preprocess raw text.
	 * <p>
	 * This filter is used in both <code>SearchEngine.Mode.INDEX</code> phase
	 * and <code>SearchEngine.Mode.SERVE</code> phase.<br/>
	 * 
	 * In both phases, filter must be identical, otherwise query might mismatch
	 * indexed corpus.
	 * 
	 * @return filter for preprocess text
	 */
	public abstract Filter getFilter();

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public static Loader createLoader() {
		String loader = Options.loaderType();
		if (LoaderType.SIMPLE.equals(loader)) {
			return new FullScanLoader();
		} else if (LoaderType.WIKI.equals(loader)) {
			return new InvertedLoader();
		}
		throw new IllegalArgumentException("Unrecognized corpus loader");
	}
}
