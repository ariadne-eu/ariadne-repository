package org.eun.lucene.core.indexer.impl;


import java.io.IOException;

import net.sourceforge.minor.lucene.core.indexer.IndexInserterDelegate;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

public class InsertDelegateDocImpl implements IndexInserterDelegate {
	private Document document;
	
	public InsertDelegateDocImpl(Document document){
		this.document = document;
	}
	
	public void insert(IndexWriter writer) throws IOException {
		writer.addDocument(document);
	}
	
}