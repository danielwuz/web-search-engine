package edu.nyu.cs.cs2580.indexer.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.nyu.cs.cs2580.doc.DocumentRaw;

public abstract class Loader implements Iterator<DocumentRaw> {

	protected final static Logger logger = LogManager.getLogger(Loader.class);

	public void loadCorpus(String corpus) throws FileNotFoundException {
		load(new File(corpus));
	}

	protected abstract void load(File file) throws FileNotFoundException;

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
