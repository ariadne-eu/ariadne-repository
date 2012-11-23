package org.eun.lucene.core.indexer.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.sourceforge.minor.lucene.core.indexer.IndexDeleterDelegate;

import org.apache.lucene.index.IndexWriter;


public class DeleteDelegateBatchImpl implements IndexDeleterDelegate {
	
	private Map<String, File> mFiles;
	
	public DeleteDelegateBatchImpl(Map<String, File> _mFiles){
		this.mFiles = _mFiles;
	}
	
	public void delete(IndexWriter writer) throws IOException {
		for (String sKey : mFiles.keySet()){
			new DeleteDelegateSingleImpl( sKey, mFiles.get(sKey) ).delete(writer);
		}
	}
	
}
