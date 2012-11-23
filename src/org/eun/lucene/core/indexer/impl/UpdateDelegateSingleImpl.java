package org.eun.lucene.core.indexer.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import net.sourceforge.minor.lucene.core.indexer.IndexUpdaterDelegate;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.ariadne_eu.utils.lucene.document.LOMHandler;
import org.eun.lucene.core.indexer.document.DocumentHandlerException;


public class UpdateDelegateSingleImpl implements IndexUpdaterDelegate {
	
	private File oldFile, newFile;
	private String oldKey, newKey;
	
	public UpdateDelegateSingleImpl(String oldKey, File _oldFile, String newKey, File _newFile){
		this.newFile = _newFile;
		this.oldFile = _oldFile;
		this.oldKey = oldKey;
		this.newKey = newKey;
	}
	
	public void update(IndexWriter writer) throws IOException {
		LOMHandler handler = new LOMHandler();
		Document doc;
		try {
			doc = handler.getDocument(new FileInputStream(newFile));
//			TODO Don't forget to search on the correct fields (ex. contents, Personne ...)
			doc.add(new Field("key", newKey, Field.Store.YES, Field.Index.UN_TOKENIZED ));
			doc.add(new Field("filepath", newFile.getCanonicalPath(), Field.Store.YES, Field.Index.TOKENIZED ));
			doc.add(new Field("filename", newFile.getName(), Field.Store.YES, Field.Index.TOKENIZED ));
			String line;
			StringBuilder sFile = new StringBuilder();
			BufferedReader bufReader = new BufferedReader(new FileReader(newFile.getCanonicalPath()));
	    	
	    	while((line = bufReader.readLine()) != null){
	    		if (line.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) continue;
	    		sFile.append(line+"\n");
	    	}
//			doc.add(new Field("contents", sFile.toString(), Field.Store.YES, Field.Index.TOKENIZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
			doc.add(new Field("md", sFile.toString(), Field.Store.YES, Field.Index.TOKENIZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
			
			//TODO doc.add(new Field("date.insert", doc.get("date.insert"), Field.Store.YES, Field.Index.UN_TOKENIZED));
			
			doc.add(new Field("date.update", DateTools.dateToString(new Date(), DateTools.Resolution.DAY), Field.Store.YES, Field.Index.UN_TOKENIZED));

//			if (doc.get("xmlns") == null){
//				//TODO check if it's in the correct format
//				doc.add(new Field("xmlns", "http://ltsc.ieee.org/xsd/LOM", Field.Store.YES, Field.Index.TOKENIZED));
//			}
//			TODO change filename to key + use oldkey!
			writer.updateDocument(new Term("key", oldKey), doc);//TODO change to the key
			//TODO change filename to key + use oldkey!
			
			
		} catch (DocumentHandlerException e) {
			e.printStackTrace();
		}
		
	}

}
