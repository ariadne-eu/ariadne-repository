/**
 * 
 */
package org.ariadne_eu.utils.mace;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 * @author gonzalo
 * 
 */
public class MACEUtils {
	private static HashMap<String, Element> classificationValues;
//	private static File classificationFile = new File("/Work/MACE/Vocabulary/MACE_LOM_Category_9_CLASSIFICATION_v5.xml");

	private static File classificationFile = new File(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_HANDLER_MACE));

	private MACEUtils() {
		classificationValues = loadClassification();
	}

	public static HashMap<String, Element> getClassification() {
		if (classificationValues == null) {
			classificationValues = loadClassification();
		}
		return classificationValues;
	}

	public static String mergeLOM(String original, String extra) {

		try {
			Namespace lomNS = Namespace.getNamespace("lom", "http://ltsc.ieee.org/xsd/LOM");
			SAXBuilder builder = new SAXBuilder();
			Reader in = new StringReader(original);
			Document originalLOM = builder.build(in);
			in = new StringReader(extra);
			Document extraLOM = builder.build(in);

			XPath xpKind;
			xpKind = XPath.newInstance("//lom:general//keyword");
			xpKind.addNamespace(lomNS);
			List keywords = xpKind.selectNodes(originalLOM);
			for (Iterator iterator = keywords.iterator(); iterator.hasNext();) {
				Element keyword = (Element) iterator.next();
				System.out.println(keyword.getChildText("string"));

			}

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String enrichWClassification(String xml) {
		// loadClassification();
		boolean competence = false;
		SAXBuilder builder = new SAXBuilder();
		Namespace lomNS = Namespace.getNamespace("lom", "http://ltsc.ieee.org/xsd/LOM");

		List labels;

		try {
			Reader in = new StringReader(xml);
			Document enrichedDoc = builder.build(in);

			Element enrichedRoot = enrichedDoc.getRootElement();

			XPath xpKind;
			xpKind = XPath.newInstance("//lom:classification//lom:taxonPath");
			xpKind.addNamespace(lomNS);
			List taxonPathElmts = xpKind.selectNodes(enrichedRoot);
			Vector newTaxonToAdd = new Vector();

			for (Iterator iterator2 = taxonPathElmts.iterator(); iterator2.hasNext();) {
				Element taxonPath = (Element) iterator2.next();
				if (taxonPath.getChild("source", lomNS) != null) {
					String source = taxonPath.getChild("source", lomNS).getChildText("string", lomNS);
					if (source.contains("Competence") || source.contains("competence")) {
						break;
					}
				}

				List taxonElmts = taxonPath.getChildren("taxon", lomNS);
				Element taxon = new Element("t");
				for (Iterator iterator = taxonElmts.iterator(); iterator.hasNext();) {
					taxon = (Element) iterator.next();
					String id = taxon.getChildText("id", lomNS);
					if (classificationValues.containsKey(id)) {
						Element classificationValue = classificationValues.get(id);
						Vector newClasifValues = getMaceClassTaxonPath(classificationValue);
						// for (Iterator iterator3 = newClasifValues.iterator();
						// iterator3.hasNext();) {
						for (int i = newClasifValues.size() - 1; i >= 0; i--) {
							// Element clasif = (Element) iterator3.next();
							Element clasif = (Element) newClasifValues.get(i);
							Element newTaxon = new Element("taxon", enrichedRoot.getNamespace());
							Element newTaxonId = new Element("id", enrichedRoot.getNamespace()).setText(clasif.getAttributeValue("id"));
							Element newTaxonEntry = new Element("entry", enrichedRoot.getNamespace());
							labels = clasif.getChildren("label");
							for (Iterator iterator3 = labels.iterator(); iterator3.hasNext();) {
								Element label = (Element) iterator3.next();
								Element newTaxonStr = new Element("string", enrichedRoot.getNamespace()).setText(label.getText()).setAttribute("language", label.getAttributeValue("lang"));
								newTaxonEntry.addContent(newTaxonStr);
							}
							// Element newTaxonStr = new
							// Element("string",enrichedRoot.getNamespace()).setText(clasif.getChildText("label")).setAttribute("language",
							// clasif.getchi);
							// newTaxonEntry.addContent(newTaxonStr);
							newTaxon.addContent(newTaxonId);
							newTaxon.addContent(newTaxonEntry);
							newTaxonToAdd.add(newTaxon);
						}
					}
				}
				taxonPath.removeContent(taxon);
				for (Iterator iterator = newTaxonToAdd.iterator(); iterator.hasNext();) {
					Element newTaxon = (Element) iterator.next();
					taxonPath.addContent(newTaxon);
				}
				newTaxonToAdd.clear();

			}
			enrichedRoot.detach();
			Document newEnrichedDoc = new org.jdom.Document(enrichedRoot);
			XMLOutputter outputter = new XMLOutputter();
			Format format = Format.getPrettyFormat();
			outputter.setFormat(format);
			String output = outputter.outputString(newEnrichedDoc);
			return output;
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	private static HashMap<String, Element> loadClassification() {
		classificationValues = new HashMap<String, Element>(0);
		SAXBuilder builder = new SAXBuilder();

		org.jdom.Document xmlDoc;
		try {
			xmlDoc = builder.build(classificationFile);
			Element root = xmlDoc.getRootElement();

			fillHashMap(root.getChild("item"));

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classificationValues;
	}

	private static void fillHashMap(Element item) {
		Element children;
		classificationValues.put(item.getAttributeValue("id"), item);
		children = item.getChild("children");
		if (children != null) {
			List items = children.getContent();
			for (Iterator iterator = items.iterator(); iterator.hasNext();) {
				Object obj = iterator.next();
				if (obj.getClass().getName().equalsIgnoreCase("org.jdom.Element")) {
					Element subItem = (Element) obj;
					fillHashMap(subItem);
				}
			}
		}
	}

	private static String readFile(String filePath) {
		String content = "";
		LineIterator it;
		File file = new File(filePath);
		try {
			it = FileUtils.lineIterator(file, "ISO-8859-1");
			while (it.hasNext()) {
				String line = it.nextLine();
				content = content + line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		LineIterator.closeQuietly(it);
		return content;

	}

	private static Vector<Element> getMaceClassTaxonPath(Element item) {
		Element parent;
		Vector<Element> taxonPath = new Vector<Element>();
		taxonPath.add(item);
		while (((item.getParentElement()).getParentElement()) != null) {
			parent = item.getParentElement().getParentElement();
			taxonPath.add(parent);
			item = parent;
		}
		return taxonPath;
	}

	public static void main(String[] args) {
		String xml = readFile("/Sandbox/temp/AriadneWS/mdstore/oai_archdaily.com_10030.xml");
		loadClassification();
		System.out.println(enrichWClassification(xml));
		;
	}

}
