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
public class SWRCHandler extends DocumentHandler {

	private static final String[] MIN_MAX = { "min", "max" };
	/** A buffer for each XML element */
	private StringBuffer elementBuffer = new StringBuffer();
	private String branche = "", id = "", about = "", name = "", contents;
	private Document doc;
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
		doc.add(new Field("contents", contents, Field.Store.YES, Field.Index.ANALYZED));
	}

	/*
	 * Save the attribute in a map to reuse it when the element ends (only used
	 * for the last element of a branch) Add an attribute field Incremental
	 * string creation to represent the current branch parsed
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		branche += qName.toLowerCase();

		String tmpBranche = branche.substring(0, branche.length());
		
		if (tmpBranche.contains(":")) {
			tmpBranche = tmpBranche.replaceAll("(\\w+):", "");
		}
		
		elementBuffer.setLength(0);

		if (tmpBranche.matches(".*person") || tmpBranche.matches(".*publication") || tmpBranche.matches(".*organization")) {
			if (atts.getLength() > 0) {
				for (int i = 0; i < atts.getLength(); i++) {
					if (atts.getQName(i).equalsIgnoreCase("rdf:about")) {
						about = atts.getValue(i);
						doc.add(new Field(tmpBranche.toLowerCase()+".about", about, Field.Store.YES, Field.Index.NOT_ANALYZED));
					} else if (atts.getQName(i).equalsIgnoreCase("rdf:id")) {
						id = atts.getValue(i);
						doc.add(new Field(tmpBranche.toLowerCase()+".id", id, Field.Store.YES, Field.Index.NOT_ANALYZED));
						contents = contents.concat(" " + id);
					}
				}
				
			}
			if(tmpBranche.matches("rdf\\.person.*")) {
				doc.add(new Field("type", "person", Field.Store.YES, Field.Index.NOT_ANALYZED));
			}else if(tmpBranche.matches("rdf\\.publication.*")) {
				doc.add(new Field("type", "publication", Field.Store.YES, Field.Index.NOT_ANALYZED));
			} else if(tmpBranche.matches("rdf\\.organization.*")) {
				doc.add(new Field("type", "organization", Field.Store.YES, Field.Index.NOT_ANALYZED));
			}
		}

		branche += BRANCH_SEPARATOR;
	}

	public void characters(char[] text, int start, int length) {
		elementBuffer.append(text, start, length);
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

		String tmpBranche = branche.substring(0, branche.length() - 1);

		if (tmpBranche.contains(":")) {
			tmpBranche = tmpBranche.replaceAll("(\\w+):", "");
		}

		String tmp2Branche = "";

		if (branche.endsWith(qName.toLowerCase() + "" + BRANCH_SEPARATOR)) {
			branche = branche.substring(0, branche.length() - qName.length() - 1);
			if (!branche.equals(""))
				tmp2Branche = branche.substring(0, branche.length() - 1);
		}

		if (elementBuffer.toString().trim().equals("")) {
			return;
		}

		//Person
		if (tmpBranche.matches(".*person\\.((givenname)|(family_name))")) {
			if (tmpBranche.endsWith("givenname")) {
				name = name.concat(elementBuffer.toString() + " ");

			} else if (tmpBranche.endsWith("family_name")) {
				name = name.concat(elementBuffer.toString());
				contents = contents.concat(" " + name);
				if (tmpBranche.startsWith("rdf.person")) 
					doc.add(new Field("rdf.person.name", name, Field.Store.YES, Field.Index.ANALYZED));
				name = "";
			}
		} 
		
		//Publication
		else if (tmpBranche.matches(".*publication.title")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString(), Field.Store.YES, Field.Index.ANALYZED));
			contents = contents.concat(" " + elementBuffer.toString());
		}
		else if (tmpBranche.matches(".*publication.year")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			contents = contents.concat(" " + elementBuffer.toString());
		}
		else if (tmpBranche.matches(".*publication.keywords")) {
			String keywords = elementBuffer.toString().replaceAll(",", " ");
			doc.add(new Field(tmpBranche.toLowerCase(), keywords, Field.Store.YES, Field.Index.ANALYZED));
			contents = contents.concat(" " + keywords);
		}
		else if (tmpBranche.matches(".*publication.spatial")) {
			String spatial = elementBuffer.toString().replaceAll(",", " ");
			doc.add(new Field(tmpBranche.toLowerCase(), spatial, Field.Store.YES, Field.Index.ANALYZED));
			contents = contents.concat(" " + spatial);
		}
		//Affiliation
		else if (tmpBranche.matches(".*organization.fn")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			contents = contents.concat(" " + elementBuffer.toString());
		}
		else if (tmpBranche.matches(".*organization.adr.description.*")) {
			if (tmpBranche.endsWith("locality")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString(), Field.Store.YES, Field.Index.ANALYZED));
				contents = contents.concat(" " + elementBuffer.toString());

			} else if (tmpBranche.endsWith("country-name")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString(), Field.Store.YES, Field.Index.ANALYZED));
				contents = contents.concat(" " + elementBuffer.toString());
			}			
		}

//		doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES, Field.Index.NOT_ANALYZED));// XXX
		// to store the contents without metatags
		// contents = contents.concat(" " +
		// elementBuffer.toString().toLowerCase());
		elementBuffer.setLength(0);
	}

	public static void main(String args[]) throws Exception {
		SWRCHandler handler = new SWRCHandler();
		Document doc = handler.getDocument(new FileInputStream(new File("/work/tmp/research.fm/xmls/lirias/persons/gonzalo-parra.xml")));
		List fields = doc.getFields();
		for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field) iterator.next();
			System.out.println(field.name() + " :: " + field.stringValue());

		}

	}

}
