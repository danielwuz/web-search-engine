package edu.nyu.cs.cs2580.doc;

/**
 * Document with score.
 * 
 * @author fdiaz
 * @author congyu
 */
public class ScoredDocument implements Comparable<ScoredDocument> {
	private Document doc;
	private double score;

	public ScoredDocument(Document doc, double score) {
		this.doc = doc;
		this.score = score;
	}

	public String asTextResult() {
		StringBuffer buf = new StringBuffer();
		buf.append(doc.docId).append("\t");
		buf.append(doc.getTitle()).append("\t");
		buf.append(score);
		return buf.toString();
	}

	/**
	 * @CS2580: Student should implement {@code asHtmlResult} for final project.
	 */
	public String asHtmlResult() {
		return "";
	}

	@Override
	public int compareTo(ScoredDocument o) {
		if (this.score == o.score) {
			return 0;
		}
		return (this.score > o.score) ? 1 : -1;
	}

	public String asString() {
		// TODO
		return null;
	}

	public Document getDoc() {
		return doc;
	}

	public double getScore() {
		return score;
	}

	@Override
	public String toString() {
		return "ScoredDocument [doc=" + doc + ", score=" + score + "]";
	}
	
}