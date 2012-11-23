package net.sourceforge.minor.lucene.core.indexer;


public interface IndexUpdater {
	
	void update(IndexUpdaterDelegate update) throws Exception;
	
}
