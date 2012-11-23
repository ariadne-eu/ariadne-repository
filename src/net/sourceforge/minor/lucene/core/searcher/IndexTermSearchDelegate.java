package net.sourceforge.minor.lucene.core.searcher;


import org.apache.lucene.index.TermEnum;

public interface IndexTermSearchDelegate {
	
	String result(TermEnum terms) throws Exception;
}
