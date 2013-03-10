package edu.nyu.cs.cs2580.doc;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import edu.nyu.cs.cs2580.common.Delimiter;

/**
 * Converter of document class.
 * 
 * @author dawu
 * 
 */
public class SerializeUtil {

	public static SerializeUtil instance() {
		return new SerializeUtil();
	}

	/**
	 * docCount#termCount#totalTermFrequency#_dictionary
	 * 
	 * @param corpus
	 * @return
	 */
	public String asRecord(Corpus corpus) {
		StringBuffer buff = new StringBuffer();
		// docCount
		buff.append(corpus.docCount + Delimiter.SHARP);
		// termCount
		buff.append(corpus.termCount + Delimiter.SHARP);
		// total Freq
		buff.append(corpus.totalTermFrequency + Delimiter.SHARP);
		// dictionary
		buff.append(asRecord(corpus._dictionary));
		return buff.toString();
	}

	private StringBuffer asRecord(Map<String, Term> dict) {
		StringBuffer buff = new StringBuffer();
		// do not have to save the keys, for keys are tokens within terms
		Collection<Term> terms = dict.values();
		for (Term term : terms) {
			buff.append(asRecord(term) + Delimiter.COLON);
		}
		return buff;
	}

	private StringBuffer asRecord(Term term) {
		StringBuffer buff = new StringBuffer();
		buff.append(term.id + Delimiter.SPACE);
		buff.append(term.token + Delimiter.SPACE);
		buff.append(term.corpusFrequency + Delimiter.SPACE);
		List<Integer> postingList = term.postingList;
		buff.append(asListRecord(postingList));
		return buff;
	}

	public Corpus toCorpus(String corpusString) {
		Corpus corpus = new Corpus();
		Scanner s = new Scanner(corpusString);
		s.useDelimiter(Delimiter.SHARP);
		// doc count
		String docCount = s.next();
		corpus.docCount = Integer.parseInt(docCount);
		// term count
		String termCount = s.next();
		corpus.termCount = Integer.parseInt(termCount);
		// totalTermFrequency
		String totalTermFrequency = s.next();
		corpus.totalTermFrequency = Integer.parseInt(totalTermFrequency);
		// dictionary
		String dictString = s.next();
		corpus._dictionary = toDictRecord(dictString, corpus);
		s.close();
		return corpus;
	}

	private Map<String, Term> toDictRecord(String dictString, Corpus corpus) {
		Scanner s = new Scanner(dictString);
		Map<String, Term> dict = new HashMap<String, Term>();
		s.useDelimiter(Delimiter.COLON);
		while (s.hasNext()) {
			String termString = s.next();
			Term term = toTermRecord(termString);
			// connect term and corpus
			term.setCorpus(corpus);
			dict.put(term.getToken(), term);
		}
		s.close();
		return dict;
	}

	private Term toTermRecord(String termString) {
		Scanner s = new Scanner(termString);
		Integer id = Integer.parseInt(s.next());
		String token = s.next();
		Integer corpusFrequency = Integer.parseInt(s.next());
		List<Integer> postingList = toListRecord(s.nextLine());
		// set fields
		Term t = new Term(id, token);
		t.corpusFrequency = corpusFrequency;
		t.postingList = postingList;
		s.close();
		return t;
	}

	/**
	 * Converts uncompressed document to string for persistence.
	 * <p>
	 * As below format indicates, any descendant of this class must override
	 * this method, as well as {{@link #fromRecord()} at the same time, to
	 * ensure correct persistence behavior.
	 * 
	 * <pre>
	 *  DocId#title#newViews#titleTokens#bodyTokens#termFrequency#termOffsets
	 * </pre>
	 * 
	 * Fields are separated by #, and within each field, element of list at
	 * first level are separated by " "(SPACE), while by ;(COLON) at second
	 * level. For each key-value pair, key and value are separated by ,(COMMA).
	 * <br/>
	 * 
	 * For the reason that # is used as delimiter, title field CANNOT contain #
	 * character. All # character must be either filtered out during indexing
	 * phase, or encoded. Here we choose to URLEncode title field using charset
	 * UTF8. If UTF8 is not supported, we simply replace # with _(Underscore) to
	 * ensure robustness. In the later case, however, there would be no way to
	 * decode the # sign back since we already lose that information.<br/>
	 * 
	 * For example, a document contains below information:
	 * <ul>
	 * <li>docId: 1</li>
	 * <li>title: Hello#World</li>
	 * <li>numViews: 3</li>
	 * <li>titleTokens: 1 2 3</li>
	 * <li>bodyTokens: 1 2 3 4</li>
	 * <li>termFrequency: <1,2> <2,2> <3,2> <4,1></li>
	 * <li>termOffsets: <1,[1,2]>, <2,[3,4]>, <3,[5,6]>, <4,[7]></li>
	 * </ul>
	 * 
	 * This method generates below string:
	 * 
	 * <pre>
	 * 1#Hello%23World#3#1 2 3#1 2 3 4 #1,2 2,2 3,2 4,1 #1,1 2;2,3 4;3,5 6;4,7
	 * </pre>
	 * 
	 * @return record of current document in string format
	 * 
	 * @see edu.nyu.cs.cs2580.indexer.io.DocumentIO#flush()
	 * @see #fromRecord()
	 */
	public String asRecord(Document doc) {
		StringBuffer buff = new StringBuffer();
		// docId
		buff.append(doc.docId + Delimiter.SHARP);
		// title field
		String title = doc.title;
		try {
			title = URLEncoder.encode(title, "UTF8");
		} catch (UnsupportedEncodingException e) {
			title = title.replaceAll(Delimiter.SHARP, "_");
		}
		buff.append(title + Delimiter.SHARP);
		// num of views
		buff.append(doc.numViews + Delimiter.SHARP);
		// title tokens
		StringBuffer titleTokens = asListRecord(doc.titleTokens);
		buff.append(titleTokens + Delimiter.SHARP);
		// body tokens
		StringBuffer bodyTokens = asListRecord(doc.bodyTokens);
		buff.append(bodyTokens + Delimiter.SHARP);
		// termFrequency
		StringBuffer tf = asMapRecord(doc.termFrequency);
		buff.append(tf + Delimiter.SHARP);
		// termOffsets
		StringBuffer to = asMapListRecord(doc.termOffsets);
		buff.append(to);
		return buff.toString();
	}

	// TODO
	private StringBuffer asMapListRecord(Map<Integer, List<Integer>> aMap) {
		StringBuffer buff = new StringBuffer();
		Iterator<Entry<Integer, List<Integer>>> it = aMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, List<Integer>> e = it.next();
			StringBuffer aList = asListRecord(e.getValue());
			buff.append(e.getKey() + Delimiter.COMMA + aList);
			buff.append(Delimiter.COLON);
		}
		return buff;
	}

	private StringBuffer asMapRecord(Map<Integer, Integer> aMap) {
		StringBuffer buff = new StringBuffer();
		Iterator<Entry<Integer, Integer>> it = aMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Integer> e = it.next();
			buff.append(e.getKey() + Delimiter.COMMA + e.getValue());
			buff.append(Delimiter.SPACE);
		}
		return buff;
	}

	private StringBuffer asListRecord(List<Integer> aList) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < aList.size(); i++) {
			if (i > 0) {
				buff.append(Delimiter.SPACE);
			}
			buff.append(aList.get(i));
		}
		return buff;
	}

	/**
	 * Reassemble document object from string representation. The string
	 * representation usually generated by {{@link #asRecord()} at persistence.<br/>
	 * 
	 * Parameter has below format:
	 * 
	 * <pre>
	 *  DocId#title#newViews#titleTokens#bodyTokens#termFrequency#termOffsets
	 * </pre>
	 * 
	 * @param docString
	 *            document string representation.
	 * @return document object
	 * @see #asRecord()
	 */
	public Document toDocument(String docString) {
		Scanner s = new Scanner(docString);
		s.useDelimiter(Delimiter.SHARP);
		String docId = s.next();
		String title = s.next();
		// construct document with id
		Document doc = new Document(Integer.parseInt(docId));
		// title field
		try {
			doc.title = URLDecoder.decode(title, "UTF8");
		} catch (UnsupportedEncodingException e) {
			doc.title = title;
		}
		// numViews
		String numViews = s.next();
		doc.numViews = Integer.parseInt(numViews);
		// title tokens
		String titleTokenString = s.next();
		doc.titleTokens = toListRecord(titleTokenString);
		// body tokens
		String bodyTokenString = s.next();
		doc.bodyTokens = toListRecord(bodyTokenString);
		// term frequency
		String tfString = s.next();
		doc.termFrequency = toMapRecord(tfString);
		// term offset
		String toString = s.next();
		doc.termOffsets = toMapListRecord(toString);
		s.close();
		return doc;
	}

	private Map<Integer, List<Integer>> toMapListRecord(String toString) {
		Map<Integer, List<Integer>> res = new HashMap<Integer, List<Integer>>();
		Scanner s = new Scanner(toString);
		s.useDelimiter(Delimiter.COLON);
		while (s.hasNext()) {
			String[] pair = s.next().split(Delimiter.COMMA);
			Integer termId = Integer.parseInt(pair[0]);
			List<Integer> offsets = toListRecord(pair[1]);
			res.put(termId, offsets);
		}
		s.close();
		return res;
	}

	private Map<Integer, Integer> toMapRecord(String tfString) {
		Map<Integer, Integer> res = new HashMap<Integer, Integer>();
		Scanner s = new Scanner(tfString);
		while (s.hasNext()) {
			String[] pair = s.next().split(Delimiter.COMMA);
			Integer termId = Integer.parseInt(pair[0]);
			Integer freq = Integer.parseInt(pair[1]);
			res.put(termId, freq);
		}
		s.close();
		return res;
	}

	private List<Integer> toListRecord(String titleTokenString) {
		List<Integer> result = new ArrayList<Integer>();
		Scanner s = new Scanner(titleTokenString);
		while (s.hasNext()) {
			String i = s.next();
			result.add(Integer.parseInt(i));
		}
		s.close();
		return result;
	}

}
