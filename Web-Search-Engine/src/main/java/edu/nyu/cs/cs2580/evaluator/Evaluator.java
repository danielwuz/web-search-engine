package edu.nyu.cs.cs2580.evaluator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.nyu.cs.cs2580.common.Config;

public class Evaluator {

	private static Logger _logger = LogManager.getLogger(Evaluator.class);

	private HashMap<String, HashMap<Integer, Double>> relevance_judgments = new HashMap<String, HashMap<Integer, Double>>();

	private static final String DELIMITER = "\t";

	private StringBuffer evalResult;

	private final String queryResponse;

	public Evaluator(String queryResponse) {
		this.queryResponse = queryResponse;
		// first read the relevance judgments into the HashMap
		readRelevanceJudgments();
	}

	public String eval() {
		evalResult = new StringBuffer();
		// now evaluate the results
		{
			int K = 1;
			evaluate(K);
		}
		{
			int K = 5;
			evaluate(K);
		}
		{
			int K = 10;
			evaluate(K);
		}
		return evalResult.toString();
	}

	private void readRelevanceJudgments() {
		BufferedReader reader = null;
		try {
			String evalCorpusPath = Config.getProperty("eval_corpus");
			reader = new BufferedReader(new FileReader(evalCorpusPath));
			String line = null;
			while ((line = reader.readLine()) != null) {
				// parse the query,did,relevance line
				Scanner s = new Scanner(line);
				s.useDelimiter("\t");
				String query = s.next();
				int did = Integer.parseInt(s.next());
				String grade = s.next();
				saveRelevance(query, grade, did);
				s.close();
			}
		} catch (IOException ioe) {
			_logger.error("Oops " + ioe.getMessage());
		} finally {
			closeReader(reader);
		}
	}

	private void saveRelevance(String query, String grade, int did) {
		if (!relevance_judgments.containsKey(query)) {
			HashMap<Integer, Double> qr = new HashMap<Integer, Double>();
			relevance_judgments.put(query, qr);
		}
		HashMap<Integer, Double> qr = relevance_judgments.get(query);
		if (isRelavant(grade)) {
			// only save relevant docId
			qr.put(did, 1.0);
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

	public void evaluate(int K) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(this.queryResponse));
			// only consider one query per call
			String line = null;
			// relevant record
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
				if (!relevance_judgments.containsKey(query)) {
					throw new IOException("Query " + query
							+ " not in evaluation corpus.");
				}
				HashMap<Integer, Double> qr = relevance_judgments.get(query);
				N = qr.size();
				if (qr.containsKey(did) && qr.get(did) == 1.0) {
					RR += 1.0;
				}
				s.close();
				if (++count == K) {
					break;
				}
			}
			double recall = RR * 1.0 / N;
			double precision = RR * 1.0 / K;
			output("Recall at K=" + K + ": " + recall);
			output("Precision at K=" + K + ": " + precision);
		} catch (Exception e) {
			output("Error:" + e.getMessage());
		} finally {
			closeReader(reader);
		}
	}

	private void output(String msg) {
		this.evalResult.append(msg + "\n");
	}
}
