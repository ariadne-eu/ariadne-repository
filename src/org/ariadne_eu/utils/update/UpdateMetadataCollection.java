package org.ariadne_eu.utils.update;
import java.io.StringReader;

import org.apache.log4j.Logger;
import org.ariadne_eu.metadata.delete.DeleteMetadataFactory;
import org.ariadne_eu.metadata.insert.InsertMetadataFactory;
import org.ariadne_eu.oai.utils.OaiUtils;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;


public class UpdateMetadataCollection {

	private static UpdateMetadataCollection instance = null;
	
	private static Logger logger = Logger.getLogger(UpdateMetadataCollection.class);


	public static UpdateMetadataCollection getInstance() {
		if(instance == null) {
			instance = new UpdateMetadataCollection();
		}
		return instance;
	}

	/*
     * NOTE: Collection is not implemented!
     * 
     * */
	public void publishMetadata(String metadata) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		try {
			
			/*Metadata Collection*/
			Namespace ns_metadataCollection = Namespace.getNamespace("mc","http://www.imsglobal.org/services/lode/imsloreg_v1p0");
			XPath ids_metadataCollection = XPath.newInstance("//mc:metadataCollection/mc:identifier/mc:entry");
			ids_metadataCollection.addNamespace(ns_metadataCollection);
			/*-------------------*/
			
			org.jdom.Document xmlDoc = builder.build(new StringReader(metadata));

			String id = null;

			Element foundId = (Element) ids_metadataCollection.selectSingleNode(xmlDoc);
			if(foundId != null) {
				id = foundId.getTextTrim();				
			}
			logger.info("Found MetadataCollection id: " + id);
			
			if (id == null) {
				/*protocol*/
				Namespace ns_protocol = Namespace.getNamespace("p","http://www.imsglobal.org/services/lode/imsloreg_v1p0");
				XPath ids_protocol = XPath.newInstance("//p:protocol/p:identifier/p:entry");
				ids_protocol.addNamespace(ns_protocol);
				/*-------------------*/
				foundId = (Element) ids_protocol.selectSingleNode(xmlDoc);
				if(foundId != null) id = foundId.getTextTrim();		
				logger.info("Found protocol id: " + id);
				if (id == null) throw new Exception("No Id found !!!");
			}

			logger.info("Pushing " + id);

			InsertMetadataFactory.insertMetadata(id, OaiUtils.parseLom2XmlstringNoXmlHeader(xmlDoc.getRootElement()),"ARIADNE");
						
			logger.info("Successfully pushed : " + id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}
	public void deleteMetadata(String id) throws Exception {
		try {
			logger.info("Deleting " + id);

			DeleteMetadataFactory.deleteMetadata(id);

			logger.info("Successfully deleted : " + id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}
}
