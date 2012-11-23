package org.eun.lucene.core.indexer.impl;

import java.io.File;
import java.io.IOException;

import net.sourceforge.minor.lucene.core.indexer.IndexDeleterDelegate;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;


public class DeleteDelegateSingleImpl implements IndexDeleterDelegate {
	
	private File f;
	private String key;
	
	public DeleteDelegateSingleImpl(String _key, File _f){
		this.f = _f;
		this.key = _key;
	}
	
	public void delete(IndexWriter writer) throws IOException {
//		TODO change filename to key + use key!
		writer.deleteDocuments(new Term("key", key));//TODO change to the key + use key!
	}

}
