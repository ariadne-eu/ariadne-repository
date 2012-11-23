/**
 * 
 */
package org.ariadne_eu.utils.lucene.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.eun.lucene.core.indexer.document.DocumentHandler;
import org.eun.lucene.core.indexer.document.DocumentHandlerException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author gonzalo
 *
 */
public class LODHandler extends DocumentHandler {
	
	/** A buffer for each XML element */
	private StringBuffer elementBuffer = new StringBuffer();
	private HashMap<String, String> attributeMap = new HashMap<String, String>();
	private String branche = "", catalog;
	private Document doc;
	private String contents, language;
	private final String BRANCH_SEPARATOR = ".";
	private final String ATT_SEPARATOR = ".";
	private final String EQUAL_SEPARATOR = "=";

	public Document getDocument(InputStream is) throws DocumentHandlerException {

		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser parser = spf.newSAXParser();
			parser.parse(is, this);
		} catch (IOException e) {
			throw new DocumentHandlerException("Cannot parse XML document", e);
		} catch (ParserConfigurationException e) {
			throw new DocumentHandlerException("Cannot parse XML document", e);
		} catch (SAXException e) {
			throw new DocumentHandlerException("Cannot parse XML document", e);
		}

		return doc;
	}

	public void startDocument() {
		doc = new Document();
		contents = new String();
		
		
		
	}
	
	public void endDocument() {
		doc.add(new Field("contents", contents, Field.Store.YES,Field.Index.ANALYZED));
		doc.add(new Field("learningoutcome.solr", "all", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
		if(doc.getField("learningoutcome.type.value") == null)
			doc.add(new Field("learningoutcome.type.value", "unspecified", Field.Store.YES,Field.Index.ANALYZED));
	}

	/*
	 * Save the attribute in a map to reuse it when the element ends (only used
	 * for the last element of a branch) Add an attribute field Incremental
	 * string creation to represent the current branch parsed
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,Attributes atts) throws SAXException {
		branche += qName.toLowerCase();
		
		String tmpBranche = branche.substring(0, branche.length());
		
		if (tmpBranche.contains(":")) {
			tmpBranche = tmpBranche.replaceAll("(\\w+):", "");
		}

		elementBuffer.setLength(0);
		
		
		if (tmpBranche.matches(".*string")) {
			if (atts.getLength() > 0) {
				for (int i = 0; i < atts.getLength(); i++) {
					if (atts.getQName(i).equalsIgnoreCase("language")) {
						language = atts.getValue(i);
						doc.add(new Field(tmpBranche.toLowerCase()+".language", language.trim().toLowerCase(), Field.Store.YES, Field.Index.NOT_ANALYZED));
					}
				}
				
			}			
		}
		
		branche += BRANCH_SEPARATOR;
	}

	public void characters(char[] text, int start, int length) {
		elementBuffer.append(text, start, length);
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

		String tmpBranche = branche.substring(0, branche.length() - 1);
		
		//remove the NS+colons on any element		
		if (tmpBranche.contains(":")) {
			tmpBranche = tmpBranche.replaceAll("(\\w+):", "");
		}
		
		String tmp2Branche = "";
		
		if (branche.endsWith(qName.toLowerCase() + "" + BRANCH_SEPARATOR)) {
			branche = branche.substring(0, branche.length() - qName.length()- 1);
			if (!branche.equals(""))
				tmp2Branche = branche.substring(0, branche.length() - 1);
		}
		if (elementBuffer.toString().trim().equals("")) {
			return;
		}

		if (tmpBranche.matches(".*identifier\\.((catalog)|(entry))")) {
			if (tmpBranche.endsWith("identifier.catalog")) {
				catalog = elementBuffer.toString().trim();
				if (catalog.equalsIgnoreCase("ICOPER"))
					doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));

			} else if (tmpBranche.endsWith("identifier.entry")) {
				if (catalog.equalsIgnoreCase("ICOPER"))
					doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));

				String fieldName = tmp2Branche + "" + BRANCH_SEPARATOR+ "catalog" + BRANCH_SEPARATOR + "entry";
				doc.add(new Field(fieldName.toLowerCase(), catalog + ":"+ elementBuffer.toString().toLowerCase().trim(),Field.Store.YES, Field.Index.ANALYZED));
			}
		}
		else if (tmpBranche.matches(".*title.*")) {
			if (tmpBranche.endsWith("title.string")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.ANALYZED));

			}
		}
		else if (tmpBranche.matches(".*description.string")) {
			String format = elementBuffer.toString().toLowerCase().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), format, Field.Store.YES, Field.Index.ANALYZED));// XXX
		}
		else {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
		}
		
		// to store the contents without metatags
		contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		elementBuffer.setLength(0);
	}
	
	

	public static void main(String args[]) throws Exception {
		LODHandler handler = new LODHandler();
		Document doc = handler.getDocument(new FileInputStream(new File("/Sandbox/temp/AriadneWS/mdstore/460e6908-0255-4d3b-6960-37a076fa606b.xml")));
		List fields = doc.getFields();
		for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field) iterator.next();
			System.out.println(field.name() + " :: " + field.stringValue());

		}

	}

}
