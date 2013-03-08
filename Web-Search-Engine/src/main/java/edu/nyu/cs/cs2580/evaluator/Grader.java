package edu.nyu.cs.cs2580.evaluator;

import edu.nyu.cs.cs2580.indexer.Indexer;
import edu.nyu.cs.cs2580.indexer.IndexerInvertedCompressed;
import edu.nyu.cs.cs2580.ranker.Ranker;

/**
 * Grading criteria.
 * 
 * Grading will be done via the public APIs for the four main classes:
 *   Indexer, Ranker, Document, Query
 * Do NOT change the public APIs for those classes.
 * 
 * In HW2, we will examine your index implementation through the following
 * tasks. Note that scanning the full set of documents during serving will get
 * ZERO credit.
 *
 *  1) Proper index construction and loading (5 + 5 + 10 points)
 *     Run index construction to construct the index. Then perform the index
 *     loading, and check that the documents and terms are properly loaded.
 *     5 points each for doconly and occurrence indices, 10 points for
 *     compressed index.
 *
 *  2) Conjunctive retrieval for doconly inverted index (20 points)
 *     Given a query, such as [new york], documents containing all terms in the
 *     query are returned.  We will use a hidden set of test queries.
 *
 *  3) Phrase+Conjunctive retrieval for occurrence inverted index (20 points)
 *     Given a query, such as ["new york city" film], documents containing all
 *     the phrases and terms are returned.  We will use a hidden set of test
 *     queries.
 *
 *  4) Phrase+Conjunctive retrieval for compressed inverted index (20 points)
 *     Same as above. We will use a hidden set of test queries. Full credit for
 *     this task will depend on the size of the index you are constructing.
 *
 *  5) Relevance based retrieval with your favorite Ranker and compressed
 *     inverted index (20 points)
 *     We will use a hidden set of test queries to roughly examine the retrieval
 *     relevance of your whole system.
 *
 *  6) Bonus: efficient implementation of {@link Indexer.documentTermFrequency}
 *     within {@link IndexerInvertedCompressed}. (10 points)
 *
 * @author congyu
 */
public class Grader {
  Indexer _indexer;
  Ranker _ranker;

  public Grader() { }

  public void setIndexer(Indexer indexer) {
    _indexer = indexer;
  }

  public void setRanker(Ranker ranker) {
    _ranker = ranker;
  }

  public static void main(String[] args) {
  }
}
