/**
 * 
 */
package org.ariadne_eu.utils.oai;

import org.jdom.Document;
import org.jdom.Element;

import uiuc.oai.OAIException;
import uiuc.oai.OAIRecord;
import uiuc.oai.OAIRepository;

/**
 * @author gonzalo
 *
 */
public class OAIHarvester {
	
	public static Document getrecord(String targetURL, String identifier, String metadataPrefix) throws OAIException {
		OAIRepository repos = new OAIRepository();
		repos.setBaseURL(targetURL);
		OAIRecord item = repos.getRecord(identifier,metadataPrefix);
		Element element = item.getMetadata();
//		Document mdDoc = new Document (element);
		Document mdDoc = element.getDocument() ;
		return mdDoc;
		
	}

}
