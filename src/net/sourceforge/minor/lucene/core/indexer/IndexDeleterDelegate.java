package net.sourceforge.minor.lucene.core.indexer;

import org.apache.lucene.index.IndexWriter;

public interface IndexDeleterDelegate {
	
	void delete(IndexWriter writer) throws Exception;
}
