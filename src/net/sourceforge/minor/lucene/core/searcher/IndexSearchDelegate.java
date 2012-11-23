package net.sourceforge.minor.lucene.core.searcher;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;

public interface IndexSearchDelegate {
	
	String result(TopDocs topDocs, IndexSearcher searcher) throws Exception;
}
