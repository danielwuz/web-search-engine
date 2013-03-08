package edu.nyu.cs.cs2580.indexer.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import edu.nyu.cs.cs2580.doc.DocumentRaw;

public class FullScanLoader extends Loader {

	// process regular file
	private BufferedReader reader;

	private String next = null;

	@Override
	protected void load(File file) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(file));
		next = readNext();
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public DocumentRaw next() {
		if (!hasNext()) {
			return null;
		}
		Scanner s = new Scanner(next);
		s.useDelimiter("\t");
		String title = s.next();
		String body = s.next();
		int numViews = Integer.parseInt(s.next());
		s.close();
		next = readNext();
		return new DocumentRaw(title, body, numViews);
	}

	private String readNext() {
		try {
			assert reader != null;
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		if (reader != null) {
			reader.close();
		}
		super.finalize();
	}

}
