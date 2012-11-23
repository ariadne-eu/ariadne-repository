package org.ariadne_eu.utils.registry;

import org.jdom.Element;
import org.jdom.Namespace;

public class Protocol {

	Identifier _identifier;
	String _name;
	String _version;
	String _protocolDescriptionBindingNamespace;
	String _protocolDescriptionBindingLocation;
	
	public Protocol(){
		_identifier = new Identifier();
	}
	
	public Protocol(Identifier identifier, String name, String version, String protocolDescriptionBindingNamespace, String protocolDescriptionBindingLocation){
		_identifier = identifier;
		_name = name;
		_version = version;
		_protocolDescriptionBindingNamespace = protocolDescriptionBindingNamespace;
		_protocolDescriptionBindingLocation = protocolDescriptionBindingLocation;
	}
	
	public void setIdentifier(Identifier identifier){
		_identifier=identifier;		
	}
	
	public void setName(String name){
		_name=name;		
	}
	
	public void setVersion(String version){
		_version=version;
	}
	
	public void setProtocolDescriptionBindingNamespace(String protocolDescriptionBindingNamespace){
		_protocolDescriptionBindingNamespace=protocolDescriptionBindingNamespace;		
	}
	
	public void setProtocolDescriptionBindingLocation(String protocolDescriptionBindingLocation){
		_protocolDescriptionBindingLocation=protocolDescriptionBindingLocation;		
	}
	
	public Identifier getIdentifier(){
		return _identifier;
	}
	
	public String getName(){
		return _name;
	}
	
	public String getVersion(){
		return _version;
	}
	
	public String getProtocolDescriptionBindingNamespace(){
		return _protocolDescriptionBindingNamespace;		
	}
	
	public String getProtocolDescriptionBindingLocation(){
		return _protocolDescriptionBindingLocation;		
	}
	
	public void parseXMLProtocol(Element protocol,Namespace ns){
		_identifier.parseXMLIdentifier(protocol.getChild("identifier",ns), ns);
		_name = protocol.getChild("name", ns).getText();
		_version = protocol.getChild("version", ns).getText();
		_protocolDescriptionBindingNamespace = protocol.getChild("protocolDescriptionBindingNamespace", ns).getTextTrim();
		_protocolDescriptionBindingLocation =protocol.getChild("protocolDescriptionBindingLocation", ns).getTextTrim();
	}
}
