package org.celstec.ariadne.app;

import java.util.Iterator;
import java.util.Vector;

import nu.xom.Element;

import org.purl.sword.base.Collection;

public class SpiCollection extends Collection {

	private boolean publishMetadataAvailable = false;
	private boolean publishMetadata;
	private Vector<String> schema = new Vector<String>();

	public void setPublishMetadata(boolean publishMetadata) {
		this.publishMetadataAvailable = true;
		this.publishMetadata = publishMetadata;
		// org.purl.sword.server.ServiceDocumentServlet

	}

	public Element marshall() {
		Element collection = super.marshall();
		collection.addNamespaceDeclaration("spi","http://www.cenorm.be/xsd/SPI");
		if (publishMetadataAvailable) {
			Element publishMdNode = new Element("publishMetadata","http://www.cenorm.be/xsd/SPI");
			publishMdNode.appendChild("" + publishMetadata);
			collection.appendChild(publishMdNode);
		}
		if (schema.size()!=0) {
			Iterator<String> it = schema.iterator();
			while (it.hasNext()){
				Element schemaNode = new Element("metadataSchema","http://www.cenorm.be/xsd/SPI");
		schemaNode.appendChild(it.next());
		collection.appendChild(schemaNode);
			}
			
		}
		return collection;
	}

	public void addMetadataschema(String schema) {
		this.schema.add(schema);
	}
}
