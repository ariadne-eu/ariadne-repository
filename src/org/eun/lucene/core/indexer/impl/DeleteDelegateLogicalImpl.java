package org.eun.lucene.core.indexer.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import net.sourceforge.minor.lucene.core.indexer.IndexDeleterDelegate;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

public class DeleteDelegateLogicalImpl implements IndexDeleterDelegate {
	
	private File f;
	private String key;
	
	public DeleteDelegateLogicalImpl(String _key, File _f){
		this.f = _f;
		this.key = _key;
	}
	
	public void delete(IndexWriter writer) throws IOException {
		Document doc = new Document();
		
		//TODO Don't forget to search on the correct fields (ex. contents, Personne ...)
		doc.add(new Field("key", key, Field.Store.YES, Field.Index.UN_TOKENIZED ));
		doc.add(new Field("filepath", f.getCanonicalPath(), Field.Store.YES, Field.Index.TOKENIZED ));
		doc.add(new Field("filename", f.getName(), Field.Store.YES, Field.Index.TOKENIZED ));
		doc.add(new Field("date.delete", DateTools.dateToString(new Date(), DateTools.Resolution.DAY), Field.Store.YES, Field.Index.UN_TOKENIZED));

		if (doc.get("lom.xmlns") == null){
			//TODO check if it's in the correct format
			doc.add(new Field("lom.xmlns", "http://ltsc.ieee.org/xsd/LOM", Field.Store.YES, Field.Index.TOKENIZED));
		}

		writer.updateDocument(new Term("key", key), doc);

	}

}
