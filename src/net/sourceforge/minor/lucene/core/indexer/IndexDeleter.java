package net.sourceforge.minor.lucene.core.indexer;

public interface IndexDeleter {
	
	void delete(IndexDeleterDelegate delete) throws Exception;
	
}
