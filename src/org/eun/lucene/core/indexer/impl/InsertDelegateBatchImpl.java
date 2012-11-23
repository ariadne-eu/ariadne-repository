package org.eun.lucene.core.indexer.impl;


import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.sourceforge.minor.lucene.core.indexer.IndexInserterDelegate;
import net.sourceforge.minor.lucene.core.utils.Check;

import org.apache.lucene.index.IndexWriter;



public class InsertDelegateBatchImpl implements IndexInserterDelegate {
	
	private Map<String, File> mFiles;
	
	public InsertDelegateBatchImpl(Map<String, File> _mFiles){
		Check.checkObject(_mFiles);
		this.mFiles = _mFiles;
	}
	
	public void insert(IndexWriter writer) throws IOException {
		for (String sKey : mFiles.keySet()){
			new InsertDelegateSingleImpl(sKey, mFiles.get(sKey)).insert(writer);
		}
	}

}
