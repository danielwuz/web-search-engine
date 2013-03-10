package edu.nyu.cs.cs2580.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import junit.framework.TestCase;

public class HtmlParser extends TestCase {
	String testHtml = "<html><HEAD>garbage here</head><body><script>sss</script><a>test</a>this is some test<p>Hello World</p><br/><script>blabla</script></body>";

	public void testParseHtml() {
		String res = testHtml.replaceAll("(?i)<head>.*</head>", "");
		res = testHtml.replaceAll("(?i)<script>.*?</script>", "");
		res = res.replaceAll("<[^<>]+>", " ");
		//System.out.println(res);
	}

	public void testWiki() {
		Path file = new File("data/wiki/0__year__").toPath();
		try {
			byte[] buffer = Files.readAllBytes(file);
			String content = new String(buffer);
			content = content.replaceAll("\n", " ");
			System.out.println(content);
			content = content.replaceAll("(?im)<head>.*</head>", "");
			content = content.replaceAll("(?im)<script>.*?</script>", "");
			content = content.replaceAll("<[^<>]+/?>", " ");
			content = content.replaceAll("\\s+", " ");
			System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
