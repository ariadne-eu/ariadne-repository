package org.ariadne_eu.oai.server.lucene.crosswalk;

import java.util.Properties;

import org.apache.lucene.document.Document;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.oclc.oai.server.crosswalk.Crosswalk;
import org.oclc.oai.server.verb.CannotDisseminateFormatException;

public class Lucene2oai_reg extends Crosswalk {

	protected String fullLomField = "";
	
	public Lucene2oai_reg(Properties properties) {
		super("http://www.imsglobal.org/services/lode/imsloreg_v1p0 http://fire.eun.org/xsd/registry/imsloreg_v1p0.xsd");
		String classname = "Lucene2oai_reg";
		fullLomField = properties.getProperty(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_MDFIELD);
		if (fullLomField == null) {
		    throw new IllegalArgumentException(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_MDFIELD + " is missing from the properties file");
		}
	}

	public String createMetadata(Object nativeItem) throws CannotDisseminateFormatException {
		//Cast the nativeItem to your object
		Document doc = (Document)nativeItem; 
		String lom = "";
		if (doc.getField(fullLomField) != null)
			lom = doc.getField(fullLomField).stringValue();

		return lom;
	}

	public boolean isAvailableFor(Object arg0) {
		return true;
	}

}
