package org.ariadne_eu.metadata.resultsformat;


import net.sourceforge.minor.lucene.core.searcher.IndexSearchDelegate;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ResultDelegateRFMXmlList implements IndexSearchDelegate {
	private static Logger log = Logger.getLogger(ResultDelegateRFMXmlList.class);

    private int start;
    private int max;

    public ResultDelegateRFMXmlList(int start, int max) {
        this.start = start;
        this.max = max;
    }

    public String result(TopDocs topDocs, IndexSearcher searcher) throws Exception {
	    Document doc;

	    StringBuilder sBuild = new StringBuilder();
	    sBuild.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"/repository/rsfm.xsl\"?>\n<results cardinality=\"" + topDocs.totalHits + "\">\n");
	    
	    String luceneHandler = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_HANDLER);
	    
	    ScoreDoc[] hits = topDocs.scoreDocs;
	    for (int i = start-1; i < topDocs.totalHits && (max < 0 || i < start-1+max); i++) {
        	doc = searcher.doc(hits[i].doc);
        	log.debug(doc.get("key") + " = " + hits[i].score);
	    	if (!luceneHandler.equalsIgnoreCase("org.ariadne_eu.utils.lucene.document.LOMLiteHandler")) {
	    		sBuild.append(doc.get("md"));
            } else {
            	sBuild.append(createLOM (doc));
            }
        }

	    sBuild.append("</results>");
    	return sBuild.toString();
	}

    private String createLOM (Document doc) {
		try {
			Namespace lomNS = Namespace.getNamespace("","http://ltsc.ieee.org/xsd/LOM");
			Namespace lomxsiNS = Namespace.getNamespace("xsi" , "http://www.w3.org/2001/XMLSchema-instance");
			Element root = new Element("lom", lomNS);
			root.addNamespaceDeclaration(lomxsiNS);
			org.jdom.Document lomDocument = new org.jdom.Document();
			
			Element general = new Element("general", lomNS);
			//identifier
			Element identifier = new Element("identifier", lomNS);
			Element identifierEntry = new Element("entry", lomNS).setText(doc.get("lom.general.identifier.entry"));
			identifier.addContent(identifierEntry);
			general.addContent(identifier);
			//title
			Element title = new Element("title", lomNS);
			Element titleString = new Element("string", lomNS).setText(doc.get("lom.general.title.string"));
			title.addContent(titleString);
			general.addContent(title);
			//description
			Element description = new Element("description", lomNS);
			Element descriptionString = new Element("string", lomNS).setText(doc.get("lom.general.description.string"));
			description.addContent(descriptionString);
			general.addContent(description);
			//keywords
			String keywords[] = doc.getValues("lom.general.keyword.string");
			for (int i = 0; i < keywords.length; i++) {
				Element keyword = new Element("keyword", lomNS);
				Element keywordString = new Element("string", lomNS).setText(keywords[i]);
				keyword.addContent(keywordString);
				general.addContent(keyword);
			}
			
			root.addContent(general);
			
			//location
			Element technical = new Element("technical", lomNS);
			Element location = new Element("location", lomNS).setText(doc.get("lom.technical.location"));
			technical.addContent(location);
			root.addContent(technical);

			lomDocument.setRootElement(root);
			
			XMLOutputter outputter = new XMLOutputter();
			Format format = Format.getPrettyFormat().setOmitDeclaration(true);
			outputter.setFormat(format);
			return (outputter.outputString(lomDocument));
			
		} catch (Exception e) {
			log.error("createLOM: ", e);
            e.printStackTrace();
		} 
		return null;
	}

}
