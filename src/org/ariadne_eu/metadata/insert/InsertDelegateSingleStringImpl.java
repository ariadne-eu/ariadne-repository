package org.ariadne_eu.metadata.insert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

import net.sourceforge.minor.lucene.core.indexer.IndexInserterDelegate;

import org.apache.log4j.Logger;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.ariadne_eu.utils.mace.MACEUtils;
import org.eun.lucene.core.indexer.document.DocumentHandler;
import org.eun.lucene.core.indexer.document.DocumentHandlerException;
import org.eun.lucene.core.indexer.document.HandlerFactory;

public class InsertDelegateSingleStringImpl implements IndexInserterDelegate {
	private static Logger log = Logger.getLogger(InsertDelegateSingleStringImpl.class);
	
	private String metadata;
	private String key;
	private String[] collection;

	public InsertDelegateSingleStringImpl(String _key, String _metadata, String[] _collection){
		this.metadata = _metadata;
		this.key = _key;
		this.collection = _collection;
	}
	
	public void insert(IndexWriter writer) throws IOException {
		
		DocumentHandler handler = HandlerFactory.getDocumentHandlerImpl();		
		Document doc=null;
		try {
			
			String insertMetadata = metadata;
            if (metadata.startsWith("<?")) {
                insertMetadata = metadata.substring(metadata.indexOf("?>")+2);
            }
			
			doc = handler.getDocument(new ByteArrayInputStream(metadata.getBytes("UTF-8")));
			
			doc.add(new Field("key", key, Field.Store.YES, Field.Index.NOT_ANALYZED ));
			for (String collection : this.collection) {
				doc.add(new Field("collection", collection.toLowerCase(), Field.Store.YES, Field.Index.NOT_ANALYZED ));
			}
			doc.add(new Field("date.insert", DateTools.dateToString(new Date(), DateTools.Resolution.MILLISECOND), Field.Store.YES, Field.Index.NOT_ANALYZED));
            
            String luceneHandler = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_HANDLER);
            if (luceneHandler.equalsIgnoreCase("org.ariadne_eu.utils.lucene.document.MACELOMHandler")) {
            	MACEUtils.getClassification();
            	String exml = MACEUtils.enrichWClassification(insertMetadata);
            	exml = exml.substring(38); //to remove the opening xml element
            	doc.add(new Field("maceenrichedlom", exml, Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
            } 
            if (!luceneHandler.equalsIgnoreCase("org.ariadne_eu.utils.lucene.document.LOMLiteHandler")) {
            	doc.add(new Field("md", insertMetadata, Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
            }

//			writer.addDocument(doc);
            Term term = new Term("key", key);
            writer.setMergeFactor(15);
            writer.setRAMBufferSizeMB(48);
            writer.updateDocument(term, doc);
		} catch (DocumentHandlerException e) {
			log.error("insert: ", e);
		}
	}

}
