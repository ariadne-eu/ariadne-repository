/**
 * 
 */
package org.ariadne_eu.utils.lucene.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.ariadne_eu.utils.VCardUtils;
import org.eun.lucene.core.indexer.document.DocumentHandler;
import org.eun.lucene.core.indexer.document.DocumentHandlerException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author gonzalo
 *
 */
public class ICOPERLOMHandler extends DocumentHandler {
	
	private static Logger log = Logger.getLogger(ICOPERLOMHandler.class);
	
	private static final String[] MIN_MAX = { "min", "max" };
	/** A buffer for each XML element */
	private StringBuffer elementBuffer = new StringBuffer();
	private String purpose = "", taxonPathSource, purposeFieldName, tpSourceFieldName, taxonPathId, tpIdFieldName, catalog, identifier, source, role, entity, lrtsource;
	private HashMap<String, String> attributeMap = new HashMap<String, String>();
	private int contributorFlag;
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
		lrtsource = "";
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

		if (atts.getLength() > 0) {
			attributeMap = new HashMap<String, String>();

			for (int i = 0; i < atts.getLength(); i++) {
				attributeMap.put(atts.getQName(i), atts.getValue(i));

				if (!atts.getQName(i).equals("uniqueElementName")) {
					if (atts.getQName(i).equalsIgnoreCase("xmlns")|| atts.getQName(i).equalsIgnoreCase("xsi:schemaLocation")) {
						String fieldName = "untokenized." + atts.getQName(i);
						doc.add(new Field(fieldName.toLowerCase(), atts.getValue(i).toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
						fieldName = atts.getQName(i);
						doc.add(new Field(fieldName.toLowerCase(), atts.getValue(i).toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));

					} else {
						String tmpBranche = branche.substring(0, branche.length());
						//remove the NS+colons on any element		
						if (tmpBranche.contains(":")) {
							tmpBranche = tmpBranche.replaceAll("(\\w+):", "");
						}
						String fieldName = tmpBranche + "" + ATT_SEPARATOR + "" + atts.getQName(i);
						doc.add(new Field(fieldName.toLowerCase(), atts.getValue(i).toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));

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

		// Attributes for string element ... (ex. Save the field by language)
		if (qName.equalsIgnoreCase("string")) {
			Iterator iter = attributeMap.keySet().iterator();
			while (iter.hasNext()) {
				String attName = ((String) iter.next()).toLowerCase();
				String attValue = ((String) attributeMap.get(attName)).toLowerCase();
				String fieldName = tmpBranche + "" + ATT_SEPARATOR + "" + attName + "" + EQUAL_SEPARATOR + "" + attValue;
				//GAP: elimino esto de los iguales en el field name proq no puedo hacerlo con plql
				//doc.add(new Field(fieldName.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.ANALYZED));
			}
		}

		// Hardcoded for LOM XML specifications -->
		// Classification ...
		if (tmpBranche.matches(".*classification\\.((purpose)|(taxonpath)).*")) {
			if (tmpBranche.endsWith("classification.purpose.source")) {

			} else if (tmpBranche.endsWith("classification.purpose.value")) {
				purpose = elementBuffer.toString().trim().toLowerCase().replaceAll(" ", "").replaceAll("\\(.*\\)", "").replaceAll("[a-z]\\.[0-9]", "").replaceAll("\\.[0-9]","");
				purposeFieldName = tmpBranche + ATT_SEPARATOR + "" + purpose;
				
				purpose = elementBuffer.toString().toLowerCase().replaceAll("\\(.*\\)", "").replaceAll("[a-z]\\.[0-9]", "").replaceAll("\\.[0-9]","").trim();
				doc.add(new Field(tmpBranche, purpose, Field.Store.YES,Field.Index.ANALYZED));
				
			} else if (tmpBranche.endsWith("classification.taxonpath.source.string")) {
				taxonPathSource = elementBuffer.toString().trim().toLowerCase();
				doc.add(new Field(tmpBranche, taxonPathSource, Field.Store.YES,Field.Index.NOT_ANALYZED));
				
				
			} else if (tmpBranche.endsWith("classification.taxonpath.taxon.id")) {

				taxonPathId = elementBuffer.toString().toLowerCase().replaceAll(" ", "").replaceAll("\\(.*\\)", "").replaceAll("[a-z]\\.[0-9]", "").replaceAll("\\.[0-9]","").trim();
				tpIdFieldName = tmpBranche + ATT_SEPARATOR + "" + taxonPathId;
				
				if (taxonPathSource.equalsIgnoreCase("CSO-FoE"))
					doc.add(new Field(tmpBranche + BRANCH_SEPARATOR + "cso-foe", taxonPathId, Field.Store.YES,Field.Index.NOT_ANALYZED));
				
				
				//GAP: lo a�ado para hacer la prueba con solr
				taxonPathId = elementBuffer.toString().trim().toLowerCase().replaceAll("\\(.*\\)", "").replaceAll("[a-z]\\.[0-9]", "").replaceAll("\\.[0-9]","");
				doc.add(new Field(tmpBranche, taxonPathId, Field.Store.YES,Field.Index.ANALYZED));
				
			} else if (tmpBranche.endsWith("classification.taxonpath.taxon.entry.string")) {
				
				doc.add(new Field(tmpBranche, elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.ANALYZED));
			}
		}
		// learningoutcome
		/*else if (tmpBranche.matches(".*educational.learningoutcome.identifier.entry*")) {
			String id = elementBuffer.toString().toLowerCase().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
			
			String response = "{\"results\":[{\"id\":\"0b8be91f4000b049091aea00504c4a09\",\"title\":\"vibrators\"}]}";
			
			JSONObject responseJson;
			try {
				responseJson = new JSONObject(response);
				JSONArray results = responseJson.getJSONArray("results");
				JSONObject result = results.getJSONObject(0);
				doc.add(new Field("learningoutcome.title.string",result.get("title").toString(), Field.Store.YES,Field.Index.ANALYZED));
				System.out.println();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
			System.out.println(id);
		}*/
		// Title
		else if (tmpBranche.matches(".*title.*")) {
			if (tmpBranche.endsWith("title.string")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.ANALYZED));
			}
		}
		// Contribute
		else if (tmpBranche.matches(".*metametadata.contribute\\.((role)|(entity)|(date)).*")) {
			if (tmpBranche.endsWith("contribute.role.source")) {
				source = elementBuffer.toString().trim();
				doc.add(new Field(tmpBranche.toLowerCase(), source.toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));

			} else if (tmpBranche.endsWith("contribute.role.value")) {
				contributorFlag++;

				source += EQUAL_SEPARATOR + ""+ elementBuffer.toString().trim();// TODO
				role = elementBuffer.toString().toLowerCase().trim();
				
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));
				
				if (role.equalsIgnoreCase("provider") && contributorFlag ==2) 
					doc.add(new Field("lom.metametadata.contribute.entity.provider", entity.toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
				
				if (contributorFlag == 2)
					contributorFlag = 0;
			} else if (tmpBranche.endsWith("contribute.entity")) {
				contributorFlag++;	
				
				if (role.equalsIgnoreCase("provider")) {
					VCardUtils vcUtil = new VCardUtils();
					entity = vcUtil.getFN(elementBuffer.toString().trim(), false);
				}
				
				
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));
				if (role.equalsIgnoreCase("provider") && contributorFlag ==2) 
					doc.add(new Field("lom.metametadata.contribute.entity.provider", entity.toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));

				if (contributorFlag == 2)
					contributorFlag = 0;
			} else if (tmpBranche.endsWith("contribute.date.datetime")) {
				String fieldname = tmp2Branche + "" + EQUAL_SEPARATOR + ""+ source;
				doc.add(new Field(fieldname.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));

				// para poder soportar busquedas con rangos
				String date = elementBuffer.toString().toLowerCase().trim().replaceAll("-", "").replaceAll("t", "").replaceAll(":", "").replaceAll("\\.", "").replaceAll("z","");
				if (date.length() > 15)
					date = date.substring(0, 15);
				//				
				doc.add(new Field(tmp2Branche.toLowerCase(), date.toLowerCase(),Field.Store.YES, Field.Index.NOT_ANALYZED));

			}
		}
		// Age
		// <educational.typicalAgeRange.string>12-15
		// <string language="en">12-15</string>
		// <string language="x-t-lre">12-15</string>
		// </typicalAgeRange>
		else if (tmpBranche.matches(".*educational.typicalagerange.string")) {
			Iterator iter = attributeMap.keySet().iterator();
			while (iter.hasNext()) {
				String attName = ((String) iter.next()).toLowerCase();
				String attValue = ((String) attributeMap.get(attName))
						.toLowerCase();

				if (!attValue.equalsIgnoreCase("x-t-lre"))
					continue;

				String[] ageRange = elementBuffer.toString().trim().split("-");

				if (ageRange.length <= 2) {

					for (int z = 0; z < ageRange.length; z++) {

						if (ageRange[z].matches("(\\d)*")) {

							if (ageRange[z].length() == 1) {
								ageRange[z] = "00" + ageRange[z];

							} else if (ageRange[z].length() == 2) {
								ageRange[z] = "0" + ageRange[z];
							}
						}
						String fieldName = tmp2Branche + "." + MIN_MAX[z];
						doc.add(new Field(fieldName.toLowerCase(), ageRange[z].toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
					}
				}
			}

		}
		// resource. Catalog + entry
		else if (tmpBranche.matches(".*resource.identifier\\.((catalog)|(entry))")) {
			if (tmpBranche.endsWith("identifier.catalog")) {
				identifier = "catalog" + EQUAL_SEPARATOR + "" + elementBuffer.toString().trim();
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));

			} else if (tmpBranche.endsWith("identifier.entry")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));
				String fieldName = tmp2Branche + "" + BRANCH_SEPARATOR + "" + identifier + "" + BRANCH_SEPARATOR + "entry";
				doc.add(new Field(fieldName.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));
			}
		}
		
		// technical.format
		else if (tmpBranche.matches(".*technical.format.*")) {
			String format = elementBuffer.toString().toLowerCase().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), format, Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		// technical.location
		else if (tmpBranche.matches(".*technical.location.*")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));// XXX
		}
		// general. Catalog + entry
		else if (tmpBranche.matches(".*general.identifier\\.((catalog)|(entry))")) {
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
		// general.description.string
		else if (tmpBranche.matches(".*general.description.string")) {
			String format = elementBuffer.toString().toLowerCase().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), format, Field.Store.YES, Field.Index.ANALYZED));
		}
		// general.keyword.string
		else if (tmpBranche.matches(".*general.keyword.string")) {
			String format = elementBuffer.toString().toLowerCase().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), format, Field.Store.YES, Field.Index.ANALYZED));
		}
		// LearningResourceType + source
		else if (tmpBranche.matches(".*learningresourcetype.source.*")) {
			lrtsource = elementBuffer.toString().toLowerCase();
		}
		// LearningResourceType + value
		else if (tmpBranche.matches(".*learningresourcetype.value.*")) {
			if (lrtsource.contains("i"))
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
		}
		// rights.description.string
		else if (tmpBranche.matches(".*general.language")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		// Source - value -> more general case so it has to be tested at the end
		else if (tmpBranche.matches(".*((source)|(value))")) {
			if (tmpBranche.endsWith("source")) {
				source = "source" + EQUAL_SEPARATOR
						+ elementBuffer.toString().trim();
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer
						.toString().toLowerCase().trim(), Field.Store.YES,
						Field.Index.NOT_ANALYZED));

			} else if (tmpBranche.endsWith("value")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer
						.toString().toLowerCase().trim(), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				String fieldName = tmp2Branche + "" + BRANCH_SEPARATOR + ""
						+ source + BRANCH_SEPARATOR + "value";
				doc.add(new Field(fieldName.toLowerCase(), elementBuffer
						.toString().toLowerCase().trim(), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
			}
		}
		// Catalog + entry
		else if (tmpBranche.matches(".*identifier\\.((catalog)|(entry))")) {
			if (tmpBranche.endsWith("identifier.catalog")) {
				catalog = elementBuffer.toString().trim();
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));

			} else if (tmpBranche.endsWith("identifier.entry")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));

				String fieldName = tmp2Branche + "" + BRANCH_SEPARATOR+ "catalog" + BRANCH_SEPARATOR + "entry";
				doc.add(new Field(fieldName.toLowerCase(), catalog + ":"+ elementBuffer.toString().toLowerCase().trim(),Field.Store.YES, Field.Index.ANALYZED));
			}
		}
		// In all the other cases add a field !
		else {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
		}
		// <---
		// to store the contents without meta-tags
		contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		elementBuffer.setLength(0);
	}
	
	private void getICOPERLOD(String id) {
		String response;
		try {
			URL url = new URL("http://ariadne.cs.kuleuven.be/icoper-dev-ws/api/icoper/getLearningOutcomeDefinition?objId=");
			// make connection, use post mode, and send query
			URLConnection urlc = url.openConnection();
			urlc.setDoOutput(true);
			urlc.setAllowUserInteraction(false);
			
			PrintStream ps = new PrintStream(urlc.getOutputStream());
			ps.close();
			// retrieve result
			BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
			String str;
			StringBuffer sb = new StringBuffer();
			while ((str = br.readLine()) != null) {
				sb.append(str);
				sb.append("\n");
			}
			br.close();
			response = sb.toString();
			if (response == null) {
				System.out.println("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = null;
		}
	}

	public static void main(String args[]) throws Exception {
		ICOPERLOMHandler handler = new ICOPERLOMHandler();
		Document doc = handler.getDocument(new FileInputStream(new File("/Sandbox/temp/AriadneWS/mdstore/0a3e9f4a-a709-470a-6e9a-d3e18d975ecf.xml")));
		List fields = doc.getFields();
		for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field) iterator.next();
			System.out.println(field.name() + " :: " + field.stringValue());

		}

	}

}
