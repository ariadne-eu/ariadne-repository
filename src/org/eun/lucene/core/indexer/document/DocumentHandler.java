package org.eun.lucene.core.indexer.document;

import java.io.InputStream;

import org.apache.lucene.document.Document;
import org.xml.sax.helpers.DefaultHandler;

//public interface DocumentHandler {
public abstract class DocumentHandler extends DefaultHandler {

  /**
   * Creates a Lucene Document from an InputStream.
   * This method can return <code>null</code>.
   *
   * @param is the InputStream to convert to a Document
   * @return a ready-to-index instance of Document
   */
//  Document getDocument(InputStream is) throws DocumentHandlerException;
	public abstract Document getDocument(InputStream is) throws DocumentHandlerException;
}


