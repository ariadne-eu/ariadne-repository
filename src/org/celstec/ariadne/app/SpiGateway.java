package org.celstec.ariadne.app;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;
import org.ariadne_eu.content.insert.InsertContentFactory;
import org.ariadne_eu.metadata.insert.InsertMetadataException;
import org.ariadne_eu.metadata.insert.InsertMetadataFactory;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.safehaus.uuid.EthernetAddress;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;



//Implement!!

public class SpiGateway {
	
	private static Logger log = Logger.getLogger(SpiGateway.class);

	public String submitMetadata(String authorizationToken, String metadataIdentifier, String resourceIdentifier, Element metadataInstance, String metadataSchemaId, String collection) {
		try {
			log.info("submitMetadata:identifier=" + metadataIdentifier + ",sessionID=" + authorizationToken);
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getPrettyFormat());
			InsertMetadataFactory.insertMetadata(metadataIdentifier, out.outputString(metadataInstance), "TEST");
			return metadataIdentifier;
		} catch (InsertMetadataException e) {
			log.error("submitMetadata: ", e);
			return "ERROR";
		}
	}

	public String submitResource(String authorizationToken, String resourceIdentifier, Object resource, String packageType, String contentType, String collection, String filename) {
		log.info("submitMetadata:identifier=" + resourceIdentifier + ",sessionID=" + authorizationToken+ ",packageType=" + packageType+ ",contentType=" + contentType);
		
		InsertContentFactory.insertContent(resourceIdentifier, new DataHandler(resource,contentType), filename,contentType);		

		return null;
	}
	
	public static String generateIdentifier() {
		UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
		EthernetAddress ethernetAddress = uuidGenerator.getDummyAddress();
		UUID uuid = uuidGenerator.generateTimeBasedUUID(ethernetAddress);

		log.info("createIdentifier:identifier=" + uuid.toString());
		return uuid.toString();
	}

}
