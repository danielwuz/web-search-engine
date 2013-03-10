package edu.nyu.cs.cs2580.indexer.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.nyu.cs.cs2580.doc.DocumentRaw;
import edu.nyu.cs.cs2580.filter.Filter;
import edu.nyu.cs.cs2580.filter.HtmlFilter;
import edu.nyu.cs.cs2580.filter.SpecialCharacterFilter;
import edu.nyu.cs.cs2580.filter.StemmingFilter;

public class InvertedLoader extends Loader {

	private List<File> files = new ArrayList<File>();

	private int pointer = 0;

	public InvertedLoader() {
		logger.info("Use corpus loader: {}", this.getClass().getSimpleName());
	}

	@Override
	protected void load(File file) throws FileNotFoundException {
		constructIndexFromFile(file);
	}

	private void constructIndexFromFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			// recursively construct index
			for (File f : files) {
				constructIndexFromFile(f);
			}
			return;
		}
		files.add(file);
	}

	@Override
	public boolean hasNext() {
		return pointer < files.size();
	}

	@Override
	public DocumentRaw next() {
		File next = files.get(pointer);
		pointer += 1;
		try {
			return createContent(next);
		} catch (IOException e) {
			logger.error("IOException while loading corpus: {}", e.getMessage());
			return null;
		}
	}

	private DocumentRaw createContent(File file) throws IOException {
		String title = createTitle(file);
		String body = createBody(file);
		return new DocumentRaw(title, body);
	}

	private String createBody(File file) throws IOException {
		StringBuffer body = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Filter filter = getFilter();
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				body.append(line);
			}
			return filter.process(body.toString());
		} finally {
			reader.close();
		}
	}

	@Override
	public Filter getFilter() {
		return new StemmingFilter(new SpecialCharacterFilter(new HtmlFilter()));
	}

	private String createTitle(File file) throws IOException {
		return file.getName();
	}

}
