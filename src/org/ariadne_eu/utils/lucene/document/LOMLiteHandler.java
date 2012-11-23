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

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.eun.lucene.core.indexer.document.DocumentHandler;
import org.eun.lucene.core.indexer.document.DocumentHandlerException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class LOMLiteHandler extends DocumentHandler {
	
	private static Logger log = Logger.getLogger(LOMLiteHandler.class);

	private static final String[] MIN_MAX = { "min", "max" };
	/** A buffer for each XML element */
	private StringBuffer elementBuffer = new StringBuffer();
	private HashMap<String, String> attributeMap = new HashMap<String, String>();
	private String branche = "";
	private Document doc;
	private String contents;
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
		doc.add(new Field("lom.solr", "all", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
	}

	/*
	 * Save the attribute in a map to reuse it when the element ends (only used
	 * for the last element of a branch) Add an attribute field Incremental
	 * string creation to represent the current branch parsed
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		branche += qName.toLowerCase();

		elementBuffer.setLength(0);
		attributeMap.clear();// No need for a map :D

		
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

		// Attributes for string element ... (ex. Save the field by language)
		if (qName.equalsIgnoreCase("string")) {
			Iterator iter = attributeMap.keySet().iterator();
			while (iter.hasNext()) {
				String attName = ((String) iter.next()).toLowerCase();
				String attValue = ((String) attributeMap.get(attName)).toLowerCase();
				String fieldName = tmpBranche + "" + ATT_SEPARATOR + "" + attName + "" + EQUAL_SEPARATOR + "" + attValue;
			}
		}

		// Hardcoded for LOM XML specifications -->
		// Classification ...

                //  23/10/12 Change for supporting classification related facets @NaturalEurope
                if (tmpBranche.matches(".*classification.*")) {
			String format = elementBuffer.toString().toLowerCase().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), format, Field.Store.YES, Field.Index.NOT_ANALYZED));// XXX
		}          
                /* -------  23/10/12 Change for supporting classification facet	@NaturalEurope
                if (tmpBranche.matches(".*classification\\.((purpose)|(taxonpath)).*")) {
                        if (tmpBranche.endsWith("classification.taxonpath.taxon.entry.string")) {
				doc.add(new Field(tmpBranche, elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.ANALYZED));// XXX
			 }
			 contents = contents.concat(" " + elementBuffer.toString().toLowerCase());                   
		}*/



		// Title
		else if (tmpBranche.matches(".*title.*")) {
			if (tmpBranche.endsWith("title.string")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim(), Field.Store.YES,Field.Index.ANALYZED));// XXX
			}
			contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		}
		// Catalog + entry
		else if (tmpBranche.matches(".*general.identifier\\.((catalog)|(entry))")) {
			if (tmpBranche.endsWith("identifier.catalog")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX

			} else if (tmpBranche.endsWith("identifier.entry")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
			}
			contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		}
                // Metametadata Catalog + entry
		else if (tmpBranche.matches(".*metametadata.identifier\\.((catalog)|(entry))")) {
			if (tmpBranche.endsWith("identifier.catalog")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX

			} else if (tmpBranche.endsWith("identifier.entry")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
			}
			contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		}

                //    Metametadata role.source + role.value
                else if (tmpBranche.matches(".*metametadata.contribute.role\\.((source)|(value))")) {
			if (tmpBranche.endsWith("role.source")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX

			} else if (tmpBranche.endsWith("role.value")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
			}
			contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		}

                // rights
		else if (tmpBranche.matches(".*rights.*")) {
			String format = elementBuffer.toString().toLowerCase().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), format, Field.Store.YES, Field.Index.NOT_ANALYZED));// XXX
		}

		// technical.format
		else if (tmpBranche.matches(".*technical.format.*")) {
			String format = elementBuffer.toString().toLowerCase().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), format, Field.Store.YES, Field.Index.NOT_ANALYZED));// XXX
		}
		// technical.location
		else if (tmpBranche.matches(".*technical.location.*")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));// XXX
		}
                // technical.duration
		else if (tmpBranche.matches(".*technical.duration")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));// XXX
		}
                
		// general.description.string ---> general.*
		else if (tmpBranche.matches(".*general.*")) {
			String format = elementBuffer.toString().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), format, Field.Store.YES, Field.Index.ANALYZED));// XXX
			contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		}

           /*     // general.keyword.string
		else if (tmpBranche.matches(".*general.keyword.string")) {
			String format = elementBuffer.toString().toLowerCase().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), format, Field.Store.YES, Field.Index.ANALYZED));// XXX
			contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		}*/
		// learningresourcetype.value
		else if (tmpBranche.matches(".*learningresourcetype.value.*")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
			contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		}
		// interactivitytype.value
		else if (tmpBranche.matches(".*interactivitytype.value.*")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
			contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		}
		// interactivitylevel.value
		else if (tmpBranche.matches(".*interactivitylevel.value.*")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
			contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		}
		// intendedenduserrole.value
		else if (tmpBranche.matches(".*intendedenduserrole.value.*")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
			contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		}
		// typicalagerange.string
		else if (tmpBranche.matches(".*typicalagerange.string.*")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
			contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		}
		// context.value
		else if (tmpBranche.matches(".*context.value.*")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
			contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		}
		// general.language
		else if (tmpBranche.matches(".*general.language")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES, Field.Index.NOT_ANALYZED));// XXX
			contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		}

		elementBuffer.setLength(0);
	}
	

	public static void main(String args[]) throws Exception {
		LOMLiteHandler handler = new LOMLiteHandler();
		Document doc = handler.getDocument(new FileInputStream(new File("/Sandbox/temp/AriadneWS/mdstoreARIADNE/ARIADNE/BLKLKP325.xml")));
		List fields = doc.getFields();
		for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field) iterator.next();
			System.out.println(field.name() + " :: " + field.stringValue());

		}

	}
}
