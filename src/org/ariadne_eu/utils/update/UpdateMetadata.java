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


public class UpdateMetadata {

	private static UpdateMetadata instance = null;
	
	private static Logger logger = Logger.getLogger(UpdateMetadata.class);


	public static UpdateMetadata getInstance() {
		if(instance == null) {
			instance = new UpdateMetadata();
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
			Namespace ns = Namespace.getNamespace("lom","http://ltsc.ieee.org/xsd/LOM");
			XPath oaiIds = XPath.newInstance("//lom:lom/lom:metaMetadata/lom:identifier/lom:catalog[text()=\"oai\"]/parent::*/lom:entry");
			oaiIds.addNamespace(ns);
			XPath ids = XPath.newInstance("//lom:lom/lom:metaMetadata/lom:identifier/lom:entry");
			ids.addNamespace(ns);


			org.jdom.Document xmlDoc = builder.build(new StringReader(metadata));

			String id = null;

			Element foundOaiId = (Element) oaiIds.selectSingleNode(xmlDoc);
			if(foundOaiId != null) {
				id = foundOaiId.getTextTrim();
			} else {
				Element foundId = (Element) ids.selectSingleNode(xmlDoc);
				if(foundId != null) {
					id = foundId.getTextTrim();
				}
			}
			if (id == null) {
				throw new Exception("No Id found !!!");
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
