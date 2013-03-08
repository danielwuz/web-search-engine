package edu.nyu.cs.cs2580.doc;

import java.io.Serializable;

public class DocumentRaw implements Serializable {

	private static final long serialVersionUID = 4024386870364762294L;

	private String title;

	private String body;

	private int numViews;

	public DocumentRaw() {
		super();
	}

	public DocumentRaw(String title, String body, int numViews) {
		super();
		this.title = title;
		this.body = body;
		this.numViews = numViews;
	}

	public DocumentRaw(String title, String body) {
		this(title, body, 0);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getNumViews() {
		return numViews;
	}

	public void setNumViews(int numViews) {
		this.numViews = numViews;
	}

	@Override
	public String toString() {
		return "DocumentRaw [title=" + title + ", numViews=" + numViews + "]";
	}

}
