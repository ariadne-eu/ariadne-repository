package org.ariadne_eu.utils.lucene.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.ariadne_eu.utils.mace.MACEUtils;
import org.eun.lucene.core.indexer.document.DocumentHandler;
import org.eun.lucene.core.indexer.document.DocumentHandlerException;
import org.jdom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class MACELOMHandler extends DocumentHandler {
	
	private static Logger log = Logger.getLogger(MACELOMHandler.class);

	private static final String[] MIN_MAX = { "min", "max" };
	/** A buffer for each XML element */
	private StringBuffer elementBuffer = new StringBuffer();
	private String purpose = "", taxonPathSource, purposeFieldName, tpSourceFieldName, taxonPathId, tpIdFieldName, source, catalog, identifier;
	private HashMap<String, String> attributeMap = new HashMap<String, String>();
	private String branche = "";
	private Document doc;
	private String contents;
	private final String BRANCH_SEPARATOR = ".";
	private final String ATT_SEPARATOR = ".";
	private final String EQUAL_SEPARATOR = "=";
	private HashMap<String, Element> classificationValues;
	private Vector<Element> taxonPath; 
	private boolean isCompetency = false;
	private String competencyID = "";
	private String domainID = "";
	private int competencyCount = 0;
	private int maxEQF = 0;
	private int minEQF = 0;

	public Document getDocument(InputStream is) throws DocumentHandlerException {

		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser parser = spf.newSAXParser();
			parser.parse(is, this);
			return doc;
		} catch (IOException e) {
			log.error("getDocument: ", e);
			throw new DocumentHandlerException("Cannot parse XML document", e);
		} catch (ParserConfigurationException e) {
			log.error("getDocument: ", e);
			throw new DocumentHandlerException("Cannot parse XML document", e);
		} catch (SAXException e) {
			log.error("getDocument: ", e);
			throw new DocumentHandlerException("Cannot parse XML document", e);
		} 
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
	public void startElement(String uri, String localName, String qName,Attributes atts) throws SAXException {
		branche += qName.toLowerCase();

		elementBuffer.setLength(0);
		attributeMap.clear();// No need for a map 

		if (atts.getLength() > 0) {
			attributeMap = new HashMap<String, String>();

			for (int i = 0; i < atts.getLength(); i++) {
				attributeMap.put(atts.getQName(i), atts.getValue(i));

				if (!atts.getQName(i).equals("uniqueElementName")) {
					if (atts.getQName(i).equalsIgnoreCase("xmlns")|| atts.getQName(i).equalsIgnoreCase("xsi:schemaLocation")) {
						String fieldName = "untokenized." + atts.getQName(i);
//						doc.add(new Field(fieldName.toLowerCase(), atts.getValue(i).toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX
						fieldName = atts.getQName(i);
//						doc.add(new Field(fieldName.toLowerCase(), atts.getValue(i).toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX

					} else {
						String tmpBranche = branche.substring(0, branche.length());
						//remove the NS+colons on any element		
						if (tmpBranche.contains(":")) {
							tmpBranche = tmpBranche.replaceAll("(\\w+):", "");
						}
						String fieldName = tmpBranche + "" + ATT_SEPARATOR + "" + atts.getQName(i);
//						doc.add(new Field(fieldName.toLowerCase(), atts.getValue(i).toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX

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
		List labels;
		String tmpBranche = branche.substring(0, branche.length() - 1);
		
		//remove the NS+colons on any element		
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

		// Attributes for string element ... (ex. Save the field by language)
		if (qName.equalsIgnoreCase("string")) {
			Iterator iter = attributeMap.keySet().iterator();
			while (iter.hasNext()) {
				String attName = ((String) iter.next()).toLowerCase();
				String attValue = ((String) attributeMap.get(attName)).toLowerCase();
//				String fieldName = tmpBranche + "" + ATT_SEPARATOR + "" + attName + "" + EQUAL_SEPARATOR + "" + attValue;
				String fieldName = tmpBranche + "" + ATT_SEPARATOR + "" + attValue;
				doc.add(new Field(fieldName.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.ANALYZED));// XXX
			}
		}

		// Hardcoded for LOM XML specifications -->
		// Classification ...
		if (tmpBranche.matches(".*classification\\.((purpose)|(taxonpath)).*")) {
			if (tmpBranche.endsWith("classification.purpose.source")) {
			} else if (tmpBranche.endsWith("classification.purpose.value")) {
				purpose = elementBuffer.toString();
				
				if (purpose.equalsIgnoreCase("competency"))
					isCompetency = true;
				else 
					isCompetency = false;
				doc.add(new Field(tmpBranche, purpose, Field.Store.YES,Field.Index.ANALYZED));// XXX
				
			} else if (tmpBranche.endsWith("classification.taxonpath.source.string")) {
				taxonPathSource = elementBuffer.toString().trim();
				if (taxonPathSource.equalsIgnoreCase("MACE Competence Catalogue"))
					isCompetency = true;
				
			} else if (tmpBranche.endsWith("classification.taxonpath.taxon.id")) {
				tpIdFieldName = tmpBranche;
				taxonPathId = elementBuffer.toString();
				if (isCompetency) {
					if (competencyCount == 0) {
						doc.add(new Field(tmpBranche+".domain", elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
						domainID = elementBuffer.toString();
						competencyCount++;
					} else if (competencyCount == 1){
						doc.add(new Field(tmpBranche+".competency", elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
						competencyID = elementBuffer.toString();
						competencyCount++;
					}
				} 
			} else if (tmpBranche.endsWith("classification.taxonpath.taxon.entry.string")) {
				if (isCompetency) {
					doc.add(new Field(tmpBranche, elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.ANALYZED));
				}
				else if (taxonPathId != null) {
					classificationValues = MACEUtils.getClassification();

					if (classificationValues.containsKey(taxonPathId)) {
						Element classificationValue = classificationValues.get(taxonPathId);
						getMaceClassTaxonPath(classificationValue);
						for (Iterator iterator = taxonPath.iterator(); iterator.hasNext();) {
							Element item = (Element) iterator.next();
							doc.add(new Field(tpIdFieldName, item.getAttributeValue("id").toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
							labels = item.getChildren("label");
							for (Iterator iterator2 = labels.iterator(); iterator2.hasNext();) {
								Element label = (Element) iterator2.next();
								doc.add(new Field(tmpBranche, label.getText().toLowerCase(), Field.Store.YES,Field.Index.ANALYZED));
								contents = contents.concat(" "+ (label.getText()).trim());
							}
						}

					} else {
						log.info("The classification value for the id:'"+ taxonPathId + "'was not found!");
					}
				} 
			} else if(tmpBranche.endsWith(".classification.taxonpath.taxon.mineqf") || tmpBranche.endsWith(".classification.taxonpath.taxon.maxeqf")) {
				if (competencyCount == 2 || competencyCount == 3) {
					if(tmpBranche.endsWith(".classification.taxonpath.taxon.mineqf"))
						minEQF = Integer.parseInt(elementBuffer.toString());
					else if (tmpBranche.endsWith(".classification.taxonpath.taxon.maxeqf"))
						maxEQF = Integer.parseInt(elementBuffer.toString());
					competencyCount++;
//				} else if (competencyCount == 4) {
				} 
				if (competencyCount == 4) {
					if(tmpBranche.endsWith(".classification.taxonpath.taxon.mineqf"))
						minEQF = Integer.parseInt(elementBuffer.toString());
					else if (tmpBranche.endsWith(".classification.taxonpath.taxon.maxeqf"))
						maxEQF = Integer.parseInt(elementBuffer.toString());
					doc.add(new Field(tmpBranche.replaceAll("mineqf", "").replaceAll("maxeqf", "") + "eqf.range", minEQF+ "_" + maxEQF, Field.Store.YES,Field.Index.NOT_ANALYZED));
					for (int i = minEQF; i <= maxEQF; i++) {
						doc.add(new Field(tmpBranche.replaceAll("mineqf", "").replaceAll("maxeqf", "") + "eqf", Integer.toString(i), Field.Store.YES,Field.Index.NOT_ANALYZED));
						doc.add(new Field(tmpBranche.replaceAll("mineqf", "").replaceAll("maxeqf", "") + "competency.eqf", competencyID + "_" + Integer.toString(i), Field.Store.YES,Field.Index.NOT_ANALYZED));
						doc.add(new Field(tmpBranche.replaceAll("mineqf", "").replaceAll("maxeqf", "") + "domain.eqf", domainID + "_" + Integer.toString(i), Field.Store.YES,Field.Index.NOT_ANALYZED));
					}
					competencyCount = 0;
					
				}
				isCompetency = false;
			}
		}
		// Title
		else if (tmpBranche.matches(".*title.*")) {
			if (tmpBranche.endsWith("title.string")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.ANALYZED));// XXX
				doc.add(new Field(tmpBranche.toLowerCase() + ".exact",elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX

			}
		}
		// Contribute
		else if (tmpBranche.matches(".*contribute\\.((role)|(entity)|(date)).*")) {
			if (tmpBranche.endsWith("contribute.role.source")) {
				source = elementBuffer.toString().trim();
				doc.add(new Field(tmpBranche.toLowerCase(), source.toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX

			} else if (tmpBranche.endsWith("contribute.role.value")) {
				source += EQUAL_SEPARATOR + "" + elementBuffer.toString().trim();// TODO
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX

			} else if (tmpBranche.endsWith("contribute.entity")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.ANALYZED));// XXX

			} else if (tmpBranche.endsWith("contribute.date.datetime")) {

				// para poder soportar busquedas con rangos
				String date = elementBuffer.toString().toLowerCase().trim().replaceAll("-", "").replaceAll("t", "").replaceAll(":", "").replaceAll("\\.", "").replaceAll("z","");
				if (date.length() > 15)
					date = date.substring(0, 15);
				//				
				doc.add(new Field(tmpBranche.toLowerCase(), date.toLowerCase(),Field.Store.YES, Field.Index.NOT_ANALYZED));// XXX

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
				String attValue = ((String) attributeMap.get(attName)).toLowerCase();

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
						doc.add(new Field(fieldName.toLowerCase(), ageRange[z].toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX
					}
				}
			}

		}
		// resource. Catalog + entry
		else if (tmpBranche.matches(".*resource.identifier\\.((catalog)|(entry))")) {
			if (tmpBranche.endsWith("identifier.catalog")) {
				identifier = "catalog" + EQUAL_SEPARATOR + "" + elementBuffer.toString().trim();
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX

			} else if (tmpBranche.endsWith("identifier.entry")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX
				// doc.add(new
				// Field(tmp2Branche+""+BRANCH_SEPARATOR+""+indentifier+""+BRANCH_SEPARATOR+"entry",elementBuffer.toString().trim(),
				// Field.Store.YES, Field.Index.NOT_ANALYZED));//XXX
				String fieldName = tmp2Branche + "" + BRANCH_SEPARATOR + "" + identifier + "" + BRANCH_SEPARATOR + "entry";
				doc.add(new Field(fieldName.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX
			}
		}
		// Catalog + entry
		else if (tmpBranche.matches(".*identifier\\.((catalog)|(entry))")) {
			if (tmpBranche.endsWith("identifier.catalog")) {
				catalog = elementBuffer.toString().trim().replace("\n", "").toLowerCase();
				doc.add(new Field(tmpBranche.toLowerCase(), catalog.toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX

			} else if (tmpBranche.endsWith("identifier.entry")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().trim().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));
				doc.add(new Field(tmpBranche.toLowerCase() + BRANCH_SEPARATOR + "exact" , elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));
			}
		}
		// technical.format
		else if (tmpBranche.matches(".*technical.format.*")) {
//			String format = elementBuffer.toString().toLowerCase().replace('/', '\\');
			String format = elementBuffer.toString().toLowerCase().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), format, Field.Store.YES, Field.Index.NOT_ANALYZED));// XXX
		}
		// general.description.string
		else if (tmpBranche.matches(".*general.description.string")) {
			String format = elementBuffer.toString().toLowerCase().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), format, Field.Store.YES, Field.Index.ANALYZED));// XXX
		}
		// general.keyword.string
		else if (tmpBranche.matches(".*general.keyword.string")) {
			String format = elementBuffer.toString().toLowerCase().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), format, Field.Store.YES, Field.Index.ANALYZED));// XXX
		}
		// rights.description.string
		else if (tmpBranche.matches(".*rights.description.string")) {
			String format = elementBuffer.toString().toLowerCase().trim();
			doc.add(new Field(tmpBranche.toLowerCase(), format, Field.Store.YES, Field.Index.ANALYZED));// XXX
			doc.add(new Field(tmpBranche.toLowerCase()+".exact", format, Field.Store.YES, Field.Index.NOT_ANALYZED));// XXX
		}
		// LearningResourceType + value
		else if (tmpBranche.matches(".*learningresourcetype.value.*")) {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.ANALYZED));
		}
		// Source - value -> more general case so it has to be tested at the end
		// !
		else if (tmpBranche.matches(".*((source)|(value))")) {
			if (tmpBranche.endsWith("source")) {
				source = "source" + EQUAL_SEPARATOR + elementBuffer.toString().trim();
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX

			} else if (tmpBranche.endsWith("value")) {
				doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX
				String fieldName = tmp2Branche + "" + BRANCH_SEPARATOR + "" + source + BRANCH_SEPARATOR + "value";
//				doc.add(new Field(fieldName.toLowerCase(), elementBuffer.toString().toLowerCase().trim(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX
			}
		}
		// In all the other cases add a field !
		else {
			doc.add(new Field(tmpBranche.toLowerCase(), elementBuffer.toString().toLowerCase(), Field.Store.YES,Field.Index.NOT_ANALYZED));// XXX
		}
		// <---
		// to store the contents without metatags
		contents = contents.concat(" " + elementBuffer.toString().toLowerCase());
		elementBuffer.setLength(0);
	}
	
	
	
	private void getMaceClassTaxonPath(Element item) {
		Element parent;
		taxonPath = new Vector();
		taxonPath.add(item);
		while ((item.getParentElement()).getParentElement() != null) {
			parent = item.getParentElement().getParentElement();
			taxonPath.add(parent);
			item = parent;
		}
	}

	public static void main(String args[]) throws Exception {
		MACELOMHandler handler = new MACELOMHandler();
		String filePath = "/Sandbox/temp/AriadneWS/mdstore/oai_archdaily.com_5991.xml"; 
		Document doc = handler.getDocument(new FileInputStream(new File(filePath)));
		List fields = doc.getFields();
		for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field) iterator.next();
			System.out.println(field.name() + " :: " + field.stringValue());

		}

	}
}
