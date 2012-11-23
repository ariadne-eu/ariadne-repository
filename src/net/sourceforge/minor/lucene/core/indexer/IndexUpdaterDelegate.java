package net.sourceforge.minor.lucene.core.indexer;

import org.apache.lucene.index.IndexWriter;

public interface IndexUpdaterDelegate {
	
	void update(IndexWriter writer) throws Exception;
}
