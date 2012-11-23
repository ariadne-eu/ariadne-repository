package net.sourceforge.minor.lucene.core.searcher;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;

public class ReaderContainer {
	
	private IndexReader reader;
	private int nbSearch;
	
	public ReaderContainer(IndexReader reader) {
		this.reader = reader;
		this.nbSearch = 0;
	}
	
	public boolean isClosable(){
		return nbSearch == 0;
	}
	
	public void close() throws IOException{
		reader.close();
	}
	
	public void incNbSearch(){
		this.nbSearch++;
	}
	
	public void decNbSearch(){
		this.nbSearch--;
	}
	
	public IndexReader getReader() {
		return reader;
	}
	
	public void setReader(IndexReader reader) {
		this.reader = reader;
	}

	@Override
	public final boolean equals(Object o) {
		if ( !(o instanceof ReaderContainer) ) return false;
		ReaderContainer rc = (ReaderContainer)o;
		if ( !(rc.getReader().equals(this.getReader())) ) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		return reader.hashCode();
	}
	
	
	
}
