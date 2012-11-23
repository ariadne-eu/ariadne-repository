package net.sourceforge.minor.lucene.core.indexer;

import org.apache.lucene.index.IndexWriter;

public interface IndexInserterDelegate {
	
	void insert(IndexWriter writer) throws Exception;
}
