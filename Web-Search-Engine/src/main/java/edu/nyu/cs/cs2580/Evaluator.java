package edu.nyu.cs.cs2580;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

class Evaluator {

	private static Logger _logger = Logger.getLogger(Evaluator.class);

	private HashMap<String, HashMap<Integer, Double>> relevance_judgments = new HashMap<String, HashMap<Integer, Double>>();

	private static final String DELIMITER = "\t";

	private String relDataPath;

	public Evaluator(String p) {
		this.relDataPath = p;
		// first read the relevance judgments into the HashMap
		readRelevanceJudgments();
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			_logger.info("need to provide relevance_judgments");
			return;
		}
		String p = args[0];
		BasicConfigurator.configure();
		Evaluator e = new Evaluator(p);
		e.eval();
	}

	private void eval() {
		// now evaluate the results from stdin
		// evaluate k = 1
		int K = 1;
		evaluateStdin(K);
	}

	private void readRelevanceJudgments() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(relDataPath));
			String line = null;
			while ((line = reader.readLine()) != null) {
				// parse the query,did,relevance line
				Scanner s = new Scanner(line);
				s.useDelimiter("\t");
				String query = s.next();
				int did = Integer.parseInt(s.next());
				String grade = s.next();
				double rel = 0.0;
				// convert to binary relevance
				if (isRelavant(grade)) {
					rel = 1.0;
				}
				if (!relevance_judgments.containsKey(query)) {
					HashMap<Integer, Double> qr = new HashMap<Integer, Double>();
					relevance_judgments.put(query, qr);
				}
				HashMap<Integer, Double> qr = relevance_judgments.get(query);
				qr.put(did, rel);
				s.close();
			}
		} catch (IOException ioe) {
			_logger.error("Oops " + ioe.getMessage());
		} finally {
			closeReader(reader);
		}
	}

	private void closeReader(BufferedReader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				_logger.error(e.getMessage(), e);
			}
		}
	}

	private boolean isRelavant(String grade) {
		return (grade.equals("Perfect")) || (grade.equals("Excellent"))
				|| (grade.equals("Good"));
	}

	public void evaluateStdin(int K) {
		// only consider one query per call
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in));
			String line = null;
			double RR = 0.0;
			double N = 0.0;
			int count = 0;
			while ((line = reader.readLine()) != null) {
				Scanner s = new Scanner(line);
				s.useDelimiter(Evaluator.DELIMITER);
				String query = s.next();
				int did = Integer.parseInt(s.next());
				String title = s.next();
				double rel = Double.parseDouble(s.next());
				if (relevance_judgments.containsKey(query) == false) {
					throw new IOException("query not found");
				}
				HashMap<Integer, Double> qr = relevance_judgments.get(query);
				if (qr.containsKey(did) != false) {
					RR += qr.get(did);
				}
				++N;
				s.close();
				if (++count == K) {
					break;
				}
			}
			double recall = RR * 1.0 / N;
			_logger.info("Recall at K=" + K + ": " + recall);
		} catch (Exception e) {
			_logger.error("Error:" + e.getMessage());
		} finally {
			closeReader(reader);
		}
	}
}
