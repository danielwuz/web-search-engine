package edu.nyu.cs.cs2580.doc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Vector;

// @CS2580: This is a simple implementation that you will be changing
// in homework 2.  For this homework, don't worry about how this is done.
public class Document {
	public final int _docid;

	// <K,V> = <word, index in vocabulary>
	private static HashMap<String, Integer> _dictionary = new HashMap<String, Integer>();

	// <V> = <vocabulary>
	private static Vector<String> _rdictionary = new Vector<String>();

	// <K,V> = <word, frequency in unique doc>
	private static HashMap<Integer, Integer> _df = new HashMap<Integer, Integer>();

	// <K,V> = <word, frequency in corpus>
	private static HashMap<Integer, Integer> _tf = new HashMap<Integer, Integer>();

	// <K,V> = <word, natural term freq in current document>
	private HashMap<Integer, Integer> _ntf = new HashMap<Integer, Integer>();

	// whole collection size
	private static int _total_tf = 0;

	private Vector<Integer> _body;
	private Vector<Integer> _title;
	private String _titleString;
	private int _numviews;

	public static int documentFrequency(String s) {
		return _dictionary.containsKey(s) ? _df.get(_dictionary.get(s)) : 0;
	}

	public static int termFrequency(String s) {
		return _dictionary.containsKey(s) ? _tf.get(_dictionary.get(s)) : 0;
	}

	public static int termFrequency() {
		return _total_tf;
	}

	public Document(int did, String content) {
		Scanner s = new Scanner(content).useDelimiter("\t");

		_titleString = s.next();
		// title vector
		_title = new Vector<Integer>();
		// body vector
		_body = new Vector<Integer>();

		readTermVector(_titleString, _title);
		readTermVector(s.next(), _body);

		HashSet<Integer> unique_terms = new HashSet<Integer>();
		//only use body vector
		/*
		for (int i = 0; i < _title.size(); ++i) {
			int idx = _title.get(i);
			unique_terms.add(idx);
			// count tf
			int old_tf = _tf.get(idx);
			_tf.put(idx, old_tf + 1);
			_total_tf++;
		}
		*/
		for (int i = 0; i < _body.size(); ++i) {
			int idx = _body.get(i);
			unique_terms.add(idx);
			// count tf
			int old_tf = _tf.get(idx);
			_tf.put(idx, old_tf + 1);
			_total_tf++;
		}
		// natural term frequency
		for (int i = 0; i < _body.size(); ++i) {
			int idx = _body.get(i);
			// count ntf
			Integer old_ntf = _ntf.get(idx);
			old_ntf = (old_ntf == null) ? 0 : old_ntf;
			_ntf.put(idx, old_ntf + 1);
		}
		for (Integer idx : unique_terms) {
			if (_df.containsKey(idx)) {
				// count document frequency
				// each document only increase this count by 1
				int old_df = _df.get(idx);
				_df.put(idx, old_df + 1);
			}
		}

		_numviews = Integer.parseInt(s.next());
		_docid = did;
	}

	public String get_title_string() {
		return _titleString;
	}

	public int get_numviews() {
		return _numviews;
	}

	/**
	 * Title vector. Associate with each doc
	 * 
	 * @return
	 */
	public Vector<String> get_title_vector() {
		return getTermVector(_title);
	}

	/**
	 * Body vector. Associate with each doc
	 * 
	 * @return
	 */
	public Vector<String> get_body_vector() {
		return getTermVector(_body);
	}

	private Vector<String> getTermVector(Vector<Integer> tv) {
		Vector<String> retval = new Vector<String>();
		for (int idx : tv) {
			retval.add(_rdictionary.get(idx));
		}
		return retval;
	}

	private void readTermVector(String raw, Vector<Integer> tv) {
		Scanner s = new Scanner(raw);
		while (s.hasNext()) {
			String term = s.next();
			int idx = -1;
			if (_dictionary.containsKey(term)) {
				idx = _dictionary.get(term);
			} else {
				idx = _rdictionary.size();
				_rdictionary.add(term);
				_dictionary.put(term, idx);
				_tf.put(idx, 0);
				_df.put(idx, 0);
			}
			tv.add(idx);
		}
		s.close();
		return;
	}

	/**
	 * If corpus contains given word
	 * 
	 * @param term
	 *            word
	 * @return true if given word is in collection, otherwise return false;
	 */
	public static boolean contains(String term) {
		return _dictionary.containsKey(term);
	}

	@Override
	public String toString() {
		return "Document [_docid=" + _docid + ", _titleString=" + _titleString
				+ ", _numviews=" + _numviews + "]";
	}

	public int getBodyLength() {
		return _body.size();
	}

	public int getNaturalTermFreq(String term) {
		// if current document doesn't contain term
		if (!Document.contains(term)) {
			return 0;
		}
		int index = _dictionary.get(term);
		Integer ntf = _ntf.get(index);
		return (ntf == null) ? 0 : ntf;
	}
}
