package edu.nyu.cs.cs2580.common;

import junit.framework.TestCase;

public class HtmlParser extends TestCase {
	String testHtml = "<html><head>garbage here</head><body><a>test</a>this is some test<p>Hello World</p><br/></body>";

	public void testParseHtml() {
		String res = testHtml.replaceAll("<head>.*</head>", "");
		res = res.replaceAll("<[^>]+>", " ");
		System.out.println(res);
	}
}
