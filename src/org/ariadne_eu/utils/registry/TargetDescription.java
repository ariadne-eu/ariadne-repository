package org.ariadne_eu.utils.registry;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class TargetDescription {
	Identifier _identifier;
	ProtocolIdentifier _protocolIdentifier;
	Protocol _protocol;
	String _location;
	ProtocolImplementationDescription _protocolImplementationDescription;
	
	public TargetDescription(){
		_identifier = new Identifier();
		_protocolIdentifier = new ProtocolIdentifier();
		_protocol= new Protocol();	
		_protocolImplementationDescription = new ProtocolImplementationDescription();
	}
	
	public TargetDescription(Identifier identifier, ProtocolIdentifier protocolIdentifier, String location){
		_identifier=identifier;
		_protocolIdentifier=protocolIdentifier;
		_location=location;		
	}
	
	public TargetDescription(Identifier identifier, Protocol protocol, String location){
		_identifier=identifier;
		_protocol=protocol;
		_location=location;		
	}
	
	public void setIdentifier(Identifier identifier){
		_identifier=identifier;		
	}
	
	public void setProtocolIDentifier(ProtocolIdentifier protocolIdentifier){
		_protocolIdentifier = protocolIdentifier;
	}
	
	public void setProtocol(Protocol protocol){
		_protocol=protocol;
	}
	
	public void setLocation(String location){
		_location=location;		
	}
	
	public void setProtocolImplementationDescription(ProtocolImplementationDescription protocolImplementationDescription){
		_protocolImplementationDescription=protocolImplementationDescription;
	}
	
	public Identifier getIdentifier(){
		return _identifier;		
	}
	
	public ProtocolIdentifier getProtocolIdentifier(){
		return _protocolIdentifier;
	}
	
	public Protocol getProtocol(){
		return _protocol;
	}
	
	public String getLocation(){
		return _location;		
	}
	
	public ProtocolImplementationDescription getProtocolImplementationDescription(){
		return _protocolImplementationDescription;
	}
	
	public void parseXMLTargetDescription(Element targetDescription,Namespace ns){
		_identifier.parseXMLIdentifier(targetDescription.getChild("identifier",ns), ns);
		_protocolIdentifier.parseXMLProtocolIdentifier(targetDescription.getChild("protocolIdentifier",ns), ns);
		_location = targetDescription.getChild("location",ns).getText();
		if (_protocolIdentifier.getEntry().contains("oai-pmh")) _protocolImplementationDescription.parseXMLOaiPmh(targetDescription.getChild("protocolImplementationDescription",ns), ns);
	}
	
	public void parseXMLProtocol(String result){
		SAXBuilder builder = new SAXBuilder();
		Reader in = new StringReader(result);
		org.jdom.Document doc;
		try {
			doc = builder.build(in);
			Namespace ns = Namespace.getNamespace("http://www.imsglobal.org/services/lode/imsloreg_v1p0");
			List<Element> list = doc.getRootElement().getChildren();
			_protocol.parseXMLProtocol(list.get(0), ns);			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public String getXMLTargetDescription(String tab){
		String xml = "";
		xml = tab+ "<target>\n" +
				tab+ "\t<targetDescription>\n" +
				tab+ _identifier.getXMLIdentifier(tab+"\t\t") +
				tab+ _protocolIdentifier.getXMLProtocolIdentifier(tab+"\t\t") +
				tab+ "\t\t<location>"+_location+"</location>\n" +
				tab+ _protocolImplementationDescription.getXMLDescription(tab+"\t\t") +
				tab+ "\t\t</targetDescription>\n" +
				tab+ "\t</target>\n";
		return xml;		
	}
	
}
