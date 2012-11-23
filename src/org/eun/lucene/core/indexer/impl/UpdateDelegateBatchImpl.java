package org.eun.lucene.core.indexer.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.sourceforge.minor.lucene.core.indexer.IndexUpdaterDelegate;

import org.apache.lucene.index.IndexWriter;


public class UpdateDelegateBatchImpl implements IndexUpdaterDelegate {

	private Map<String[], File[]> mFiles;
	
	//TODO check constructor param
	
	public UpdateDelegateBatchImpl(Map<String[], File[]> _mFiles){
		this.mFiles = _mFiles;
	}
	
	public void update(IndexWriter writer) throws IOException {
		for (String[] sKey : mFiles.keySet()){
			
			if(sKey.length != 2 && mFiles.get(sKey).length != 2){
				throw new InternalError("Size != 2. You must specify 2 keys and 2 files (old and new)");
			}
			
			new UpdateDelegateSingleImpl( sKey[0], mFiles.get(sKey)[0], sKey[1], mFiles.get(sKey)[1] ).update(writer);
		}
	}

}
