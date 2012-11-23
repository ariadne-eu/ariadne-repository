package org.ariadne_eu.oai.server.lucene.crosswalk;

import java.util.Properties;
/*
import org.apache.lucene.document.Document;
import org.ariadne.oai.utils.OaiDCUtils;

import org.jdom.Element;
import org.jdom.Namespace;
import org.oclc.oai.server.crosswalk.Crosswalk;
import org.oclc.oai.server.verb.CannotDisseminateFormatException;

public class Lucene2oai_dc extends Crosswalk {
	
	protected static Namespace dcns = Namespace.getNamespace("http://purl.org/dc/elements/1.1/");
	protected static Namespace xsi = Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");

	public Lucene2oai_dc(Properties properties) {
		super("http://dublincore.org/schemas/xmls/simpledc20021212.xsd");

	}

	public String createMetadata(Object nativeItem) throws CannotDisseminateFormatException {
		//Cast the nativeItem to your object
		Document doc = (Document)nativeItem; 
		
		Element dc = new Element("dc",dcns);
		dc.setAttribute("schemaLocation","http://purl.org/dc/elements/1.1/ http://dublincore.org/schemas/xmls/simpledc20021212.xsd",xsi);
		
		//set the General Identifier
		Element identifier = OaiDCUtils.newElement("identifier", dc);
		String identifierString = doc.getField("identifier").stringValue();
		OaiDCUtils.addString(identifierString, identifier);
		
		//set the General Title
		Element title = OaiDCUtils.newElement("title", dc);
		String titleString = doc.getField("title").stringValue();
		OaiDCUtils.addString(titleString, title);
				
		//set the Educational learning resource type
		Element type = OaiDCUtils.newElement("type", dc);
		String typeString = doc.getField("learningResourceType").stringValue();
		OaiDCUtils.addString(typeString, type);
		
		String result = "";
		result = OaiDCUtils.parseDC2Xmlstring(dc);
		return result;
	}

	public boolean isAvailableFor(Object arg0) {
		return true;
	}

}*/
