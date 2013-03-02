package edu.nyu.cs.cs2580.doc;

// @CS2580: this class should not be changed.
public class ScoredDocument implements Comparable<ScoredDocument> {
	public int _did;
	public String _title;
	public Double _score;

	public ScoredDocument(int did, String title, double score) {
		_did = did;
		_title = title;
		_score = score;
	}

	public String asString() {
		return new String(Integer.toString(_did) + "\t" + _title + "\t"
				+ _score);
	}

	@Override
	public String toString() {
		return "ScoredDocument [_did=" + _did + ", _title=" + _title
				+ ", _score=" + _score + "]";
	}

	@Override
	public int compareTo(ScoredDocument o) {
		if (this._score > o._score) {
			return -1;
		} else if (this._score < o._score) {
			return 1;
		} else {
			return 0;
		}
	}

}
