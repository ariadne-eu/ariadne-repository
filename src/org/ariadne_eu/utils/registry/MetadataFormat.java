package org.ariadne_eu.utils.registry;

import org.jdom.Element;
import org.jdom.Namespace;

public class MetadataFormat {
	
	String _metadataPrefix;
	String _schema;
	String _metadataNamespace;
	
	public MetadataFormat(){
		
	}
	
	public MetadataFormat(String metadataPrefix, String schema, String metadataNamespace){
		_metadataPrefix=metadataPrefix;
		_schema=schema;
		_metadataNamespace=metadataNamespace;
	}
	
	public void setMetadataPrefix(String metadataPrefix){
		_metadataPrefix=metadataPrefix;
	}
	
	public void setSchema(String schema){
		_schema=schema;
	}
	
	public void setMetadataNameSpace(String metadataNamespace){
		_metadataNamespace=metadataNamespace;		
	}
	
	public String getMetadataPrefix(){
		return _metadataPrefix;
	}
	
	public String getSchema(){
		return _schema;
	}
	
	public String getMetadataNamespace(){
		return _metadataNamespace;		
	}
	
	public void parseXMLMetadataFormat(Element metadataFormat, Namespace ns){
		_metadataPrefix = metadataFormat.getChildText("metadataPrefix",ns);
		_schema = metadataFormat.getChildText("schema", ns);
		_metadataNamespace = metadataFormat.getChildText("metadataNamespace");
	}
	
	public String getXMLMetadataFormat(String tab){
		String xml = "";
		xml = tab+"<metadataFormat>\n" +
				tab+"\t<metadataPrefix>"+_metadataPrefix+"</metadataPrefix>\n" +
				tab+"\t<schema>"+_schema+"</schema>\n" +
				tab+"\t<metadataNamespace>"+_metadataNamespace+"</metadataNamespace>\n" +
				tab+"</metadataFormat>\n";
		return xml;
	}
}