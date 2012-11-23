package org.eun.lucene.core.indexer.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import net.sourceforge.minor.lucene.core.indexer.IndexInserterDelegate;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.ariadne_eu.utils.lucene.document.LOMHandler;
import org.eun.lucene.core.indexer.document.DocumentHandlerException;



public class InsertDelegateSingleImpl implements IndexInserterDelegate {
	private File f;
	private String key;
	
	public InsertDelegateSingleImpl(String _key, File _f){
		this.f = _f;
		this.key = _key;
	}
	
	public void insert(IndexWriter writer) throws IOException {
		LOMHandler handler = new LOMHandler();
		Document doc=null;
		try {
			doc = handler.getDocument(new FileInputStream(f));
//			TODO Don't forget to search on the correct fields (ex. contents, Personne ...)
			doc.add(new Field("key", key, Field.Store.YES, Field.Index.UN_TOKENIZED ));
			doc.add(new Field("filepath", f.getCanonicalPath(), Field.Store.YES, Field.Index.TOKENIZED ));
			doc.add(new Field("filename", f.getName(), Field.Store.YES, Field.Index.TOKENIZED ));
			//doc.add(new Field("contents", new InputStreamReader(new FileInputStream(f), "UTF-8"), Field.TermVector.YES ));
			doc.add(new Field("date.insert", DateTools.dateToString(new Date(), DateTools.Resolution.DAY), Field.Store.YES, Field.Index.UN_TOKENIZED));
			
			String line;
			StringBuilder sFile = new StringBuilder();
			BufferedReader bufReader = new BufferedReader(new FileReader(f.getCanonicalPath()));
	    	
	    	while((line = bufReader.readLine()) != null){
	    		if (line.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) continue;
	    		sFile.append(line+"\n");
	    	}
	    	
//			doc.add(new Field("contents", sFile.toString(), Field.Store.YES, Field.Index.TOKENIZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
			doc.add(new Field("md", sFile.toString(), Field.Store.YES, Field.Index.TOKENIZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
			
//			if (doc.get("xmlns") == null){
////				TODO check if it's in the correct format
//				doc.add(new Field("xmlns", "http://ltsc.ieee.org/xsd/LOM", Field.Store.YES, Field.Index.TOKENIZED));
//			}
			
			writer.addDocument(doc);
		} catch (DocumentHandlerException e) {
			//log.error("Bad xml");
		    moveBadFile(f);//TODO ?
			//e.printStackTrace();
		}
	}
	
	private void moveBadFile(File f) {
	    File dir = new File("C:\\Benoit\\workspaces\\tests_workspace\\XML\\lom\\lom2_bad");
	    
	    boolean success = f.renameTo(new File(dir, f.getName()));
	    if (!success) {
	    	System.out.println("failed");
	    }
	}
	
}